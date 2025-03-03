package com.evo.storage.application.factory;

import com.evo.storage.application.exception.AppException;
import com.evo.storage.application.exception.ErrorCode;
import com.evo.storage.application.service.FileCmdService;
import com.evo.storage.application.service.impl.command.PrivateFileCmdServiceImpl;
import com.evo.storage.application.service.impl.command.PublicFileCmdServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FileCmdServiceFactory {

    private final PublicFileCmdServiceImpl publicFileCmdService;
    private final PrivateFileCmdServiceImpl privateFileCmdService;

    public FileCmdServiceFactory(PublicFileCmdServiceImpl publicFileCmdService,
                                 PrivateFileCmdServiceImpl privateFileCmdService) {
        this.publicFileCmdService = publicFileCmdService;
        this.privateFileCmdService = privateFileCmdService;
    }

    public FileCmdService getService() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/api/public/files")) {
            return publicFileCmdService;
        } else if (requestUri.startsWith("/api/private/files")) {
            return privateFileCmdService;
        } else {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
