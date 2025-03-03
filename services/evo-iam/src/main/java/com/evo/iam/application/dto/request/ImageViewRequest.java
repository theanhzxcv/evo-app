package com.evo.iam.application.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageViewRequest {
    private Double ratio;
    private Integer width;
    private Integer height;
}
