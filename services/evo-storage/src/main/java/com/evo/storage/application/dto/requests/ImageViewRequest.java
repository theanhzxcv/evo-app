package com.evo.storage.application.dto.requests;

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
