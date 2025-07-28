package org.maengle.admin.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestModel {
    private Long seq;
    private String mid;

    @NotBlank
    private String name;
    private String description;
}
