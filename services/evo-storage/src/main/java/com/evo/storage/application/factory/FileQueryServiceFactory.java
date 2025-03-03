package com.evo.storage.application.factory;

import com.evo.storage.application.exception.AppException;
import com.evo.storage.application.exception.ErrorCode;
import com.evo.storage.application.service.impl.query.PrivateFileQueryServiceImpl;
import com.evo.storage.application.service.impl.query.PublicFileQueryServiceImpl;
import com.evo.storage.application.service.FileQueryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FileQueryServiceFactory {

    private final PublicFileQueryServiceImpl publicFileQueryService;
    private final PrivateFileQueryServiceImpl privateFileQueryService;

    public FileQueryServiceFactory(PublicFileQueryServiceImpl publicFileQueryService,
                                 PrivateFileQueryServiceImpl privateFileQueryService) {
        this.publicFileQueryService = publicFileQueryService;
        this.privateFileQueryService = privateFileQueryService;
    }

    public FileQueryService getService() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/api/public/files")) {
            return publicFileQueryService;
        } else if (requestUri.startsWith("/api/private/files")) {
            return privateFileQueryService;
        } else {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
