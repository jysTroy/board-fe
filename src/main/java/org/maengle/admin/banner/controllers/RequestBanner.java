package org.maengle.admin.banner.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.maengle.file.entities.FileInfo;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequestBanner {

    private String mode = "add";

    private Long seq;

    @NotBlank
    private String groupCode;

    @NotBlank
    private String bName;

    private String bLink;
    private String target = "_self";

    private long listOrder;

    private boolean active;

    private MultipartFile[] files;

    private FileInfo bannerImage;
}
