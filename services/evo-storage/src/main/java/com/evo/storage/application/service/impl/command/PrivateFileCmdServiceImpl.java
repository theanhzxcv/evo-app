package com.evo.storage.application.service.impl.command;

import com.evo.storage.application.dto.requests.FileUpdateRequest;
import com.evo.storage.application.dto.requests.ImageViewRequest;
import com.evo.storage.application.dto.responses.FileResponse;
import com.evo.storage.application.exception.AppException;
import com.evo.storage.application.exception.ErrorCode;
import com.evo.storage.application.mapper.FileDomainMapper;
import com.evo.storage.application.service.FileCmdService;
import com.evo.storage.domain.FileSt;
import com.evo.storage.domain.command.FileCreationCmd;
import com.evo.storage.domain.command.FileUpdateCmd;
import com.evo.storage.domain.repository.FileDomainRepository;
import com.evo.storage.infrastructure.configuration.AllowedFiles;
import com.evo.storage.infrastructure.configuration.HashingUtils;
import com.evo.storage.infrastructure.persistence.entity.FileEntity;
import com.evo.storage.infrastructure.persistence.mapper.FileEntityMapperImpl;
import com.evo.storage.infrastructure.persistence.repository.FileEntityRepository;
import com.evo.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PrivateFileCmdServiceImpl implements FileCmdService {

    @Value("${file.storage-directory}")
    private String STORAGE_DIRECTORY;

    private final AllowedFiles allowedFiles;
    private final HashingUtils hashingUtils;
    private final FileDomainMapper fileDomainMapper;
    private final FileDomainRepository fileDomainRepository;
    private final FileEntityMapperImpl fileEntityMapper;
    private final FileEntityRepository fileEntityRepository;

    @Override
    public FileResponse upload(MultipartFile file, String description) throws IOException {
        String originalName = file.getOriginalFilename();
        String contentType = file.getContentType();
        validateFile(originalName, contentType);

        String fileExtension = getFileExtension(originalName);
        boolean isValidExtension = allowedFiles.getALLOWED_IMAGES_EXTENSIONS().contains(fileExtension) ||
                allowedFiles.getALLOWED_DOCUMENTS_EXTENSIONS().contains(fileExtension) ||
                allowedFiles.getALLOWED_CODES_EXTENSIONS().contains(fileExtension) ||
                allowedFiles.getALLOWED_ARCHIVES_EXTENSIONS().contains(fileExtension);

        if (!isValidExtension) {
            throw new AppException(ErrorCode.UNSUPPORTED_EXTENSION);
        }

        String hashFileName = hashingUtils.hashWithMD5(originalName);
        String fileType = determineTargetDirectory(fileExtension);
        Path targetPath = createTargetDirectory(fileType);
        File fileToSave = resolveTargetFile(targetPath, hashFileName);
        Files.copy(file.getInputStream(), fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
        String filePath = fileToSave.getPath();

        FileCreationCmd cmd = fileDomainMapper.createFrom(file, description);
        cmd.setHashNameMd5(hashFileName);
        cmd.setPath(filePath);
        cmd.setType(fileType);
        cmd.setExtension(fileExtension);
        cmd.setOwner(SecurityUtils.getCurrentUser().orElse("anonymous"));
        cmd.setVisibility("PRIVATE");

        FileSt fileSt = new FileSt(cmd);
        fileDomainRepository.save(fileSt);

        return fileDomainMapper.responseFrom(fileSt);
    }

    @Override
    public List<FileResponse> uploadMultipleFile(List<MultipartFile> files, String description) throws IOException {
        List<FileResponse> fileResponses = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                FileResponse response = upload(file, description);
                fileResponses.add(response);
            } catch (RuntimeException ex) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        return fileResponses;
    }

    @Override
    public String uploadProfileImage(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String contentType = file.getContentType();
        validateFile(originalName, contentType);

        String fileExtension = getFileExtension(originalName);
        boolean isValidImageExtension = allowedFiles.getALLOWED_IMAGES_EXTENSIONS().contains(fileExtension);

        if (!isValidImageExtension) {
            throw new AppException(ErrorCode.UNSUPPORTED_EXTENSION);
        }

        String hashFileName = hashingUtils.hashWithMD5(originalName);
        String fileType = determineTargetDirectory(fileExtension);
        Path targetPath = createTargetDirectory(fileType);
        File fileToSave = resolveTargetFile(targetPath, hashFileName);
        Files.copy(file.getInputStream(), fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
        String filePath = fileToSave.getPath();

        FileCreationCmd cmd = fileDomainMapper.uploadAvatarFrom(file);
        cmd.setHashNameMd5(hashFileName);
        cmd.setPath(filePath);
        cmd.setType(fileType);
        cmd.setExtension(fileExtension);
        cmd.setOwner(SecurityUtils.getCurrentUser().orElse("anonymous"));
        cmd.setVisibility("PRIVATE");

        FileSt fileSt = new FileSt(cmd);
        fileDomainRepository.save(fileSt);

        return fileSt.getId().toString();
    }

    @Override
    public byte[] viewImage(String image, ImageViewRequest request) throws IOException {
        FileSt fileSt = fileEntityRepository.findByOriginalNameAndNotDeleted(image)
                .map(fileEntityMapper::toDomain)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_FOUND));

        String fileExtension = getFileExtension(image);
        boolean isValidImageExtension = allowedFiles.getALLOWED_IMAGES_EXTENSIONS().contains(fileExtension);

        if (!isValidImageExtension) {
            throw new AppException(ErrorCode.UNSUPPORTED_EXTENSION);
        }

        File file = new File(fileSt.getPath());
        if (!file.exists()) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        }

        BufferedImage originalImage = ImageIO.read(file);
        BufferedImage processedImage = processImage(originalImage,
                request.getRatio(),
                request.getWidth(),
                request.getHeight());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(processedImage, "jpeg", outputStream);

        return outputStream.toByteArray();
    }

    @Override
    public FileResponse update(String fileName, FileUpdateRequest request) {
        FileSt fileSt = fileEntityRepository.findByOriginalNameAndNotDeleted(fileName)
                .map(fileEntityMapper::toDomain)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_FOUND));

        FileUpdateCmd cmd = new FileUpdateCmd();
        fileDomainMapper.updateFrom(cmd, request);
        fileSt.update(cmd);

        fileDomainRepository.save(fileSt);

        return fileDomainMapper.responseFrom(fileSt);
    }

    @Override
    public File download(String fileName) throws IOException {
        FileSt fileSt = fileEntityRepository.findByOriginalNameAndNotDeleted(fileName)
                .map(fileEntityMapper::toDomain)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_FOUND));

        String fileExtension = getFileExtension(fileName);
        boolean isValidImageExtension = allowedFiles.getALLOWED_IMAGES_EXTENSIONS().contains(fileExtension);
        if (!isValidImageExtension) {
            throw new AppException(ErrorCode.UNSUPPORTED_EXTENSION);
        }

        String fileType = determineTargetDirectory(fileExtension);
        Path targetPath = createTargetDirectory(fileType);
        File fileToDownload = new File(fileSt.getPath());

        if (!Objects.equals(fileToDownload.getParent(), targetPath.toString())) {
            throw new AppException(ErrorCode.INVALID_FILE_NAME);
        }

        if (!fileToDownload.exists()) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        }

        return fileToDownload;
    }

    @Override
    public FileResponse delete(String fileName) {
        FileSt fileSt = findFile(fileName);

        if (fileSt.getDeleted()) {
            throw new AppException(ErrorCode.FILE_ALREADY_DELETED);
        }
        fileSt.delete();
        fileDomainRepository.save(fileSt);

        return fileDomainMapper.responseFrom(fileSt);
    }

    @Override
    public FileResponse undelete(String fileName) {
        FileSt fileSt = findFile(fileName);

        if (!fileSt.getDeleted()) {
            throw new AppException(ErrorCode.FILE_ALREADY_RESTORED);
        }
        fileSt.undelete();
        fileDomainRepository.save(fileSt);

        return fileDomainMapper.responseFrom(fileSt);
    }

    private FileSt findFile (String fileName) {
        FileEntity fileEntity = fileEntityRepository.findByOriginalName(fileName)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_FOUND));

        return fileEntityMapper.toDomain(fileEntity);
    }

    private void validateFile(String originalName, String contentType) {
        if (fileEntityRepository.findByOriginalName(originalName).isPresent()) {
            throw new AppException(ErrorCode.FILE_ALREADY_EXISTS);
        }

        if (!isValidContentType(contentType)) {
            throw new AppException(ErrorCode.UNSUPPORTED_FILE_TYPE);
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        }
        return fileName.substring(dotIndex + 1).toLowerCase();
    }

    boolean isValidContentType(String contentType) {
        return allowedFiles.getALLOWED_CONTENT_TYPES().contains(contentType);
    }

    private String determineTargetDirectory(String fileExtension) {
        List<String> imagesExtensions = allowedFiles.getALLOWED_IMAGES_EXTENSIONS();
        List<String> docsExtensions = allowedFiles.getALLOWED_DOCUMENTS_EXTENSIONS();
        List<String> codesExtensions = allowedFiles.getALLOWED_CODES_EXTENSIONS();
        List<String> archivesExtensions = allowedFiles.getALLOWED_ARCHIVES_EXTENSIONS();

        String lowerCaseExtension = fileExtension.toLowerCase();
        if (imagesExtensions.contains(lowerCaseExtension)) {
            return "images";
        } else if (docsExtensions.contains(lowerCaseExtension)) {
            return "documents";
        } else if (codesExtensions.contains(lowerCaseExtension)) {
            return "code";
        } else if (archivesExtensions.contains(lowerCaseExtension)) {
            return "archives";
        } else {
            return "other";
        }
    }

    private Path createTargetDirectory(String fileType) {
        LocalDate currentDate = LocalDate.now();
        String datePath = currentDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        Path targetPath = Path.of(STORAGE_DIRECTORY, fileType, datePath).toAbsolutePath();
        File dir = targetPath.toFile();

        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Failed to create directory: " + targetPath);
        }

        return targetPath;
    }

    private File resolveTargetFile(Path targetPath, String fileName) {
        File targetFile = new File(targetPath + File.separator + fileName);

        if (!Objects.equals(targetFile.getParent(), targetPath.toString())) {
            throw new AppException(ErrorCode.INVALID_FILE_NAME);
        }

        return targetFile;
    }

    private BufferedImage processImage(BufferedImage originalImage, Double ratio, Integer width, Integer height) {
        if (ratio != null) {
            width = (int) (originalImage.getWidth() * ratio);
            height = (int) (originalImage.getHeight() * ratio);
        } else if (width != null && height != null) {
        } else if (width != null) {
            height = (int) (originalImage.getHeight() * ((double) width / originalImage.getWidth()));
        } else if (height != null) {
            width = (int) (originalImage.getWidth() * ((double) height / originalImage.getHeight()));
        } else {
            return originalImage;
        }

        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }
}
