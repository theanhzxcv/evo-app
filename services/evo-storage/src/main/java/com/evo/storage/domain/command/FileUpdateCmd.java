package com.evo.storage.domain.command;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUpdateCmd {
    private String description;
    private String owner;
    private String visibility;
}
