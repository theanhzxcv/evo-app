package com.evo.iam.domain;

import com.evo.iam.domain.command.UserActivityCmd;
import com.evo.util.IdUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity {
    private UUID id;
    private String ip;
    private String email;
    private String activity;
    private Instant logAt;

    public UserActivity(UserActivityCmd cmd, HttpServletRequest request) {
        this.id = IdUtils.nextId();
        this.ip = getUserIp(request);
        this.email = cmd.getEmail();
        this.activity = cmd.getActivity();
        this.logAt = Instant.now();
    }

    private String getUserIp(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }
}
