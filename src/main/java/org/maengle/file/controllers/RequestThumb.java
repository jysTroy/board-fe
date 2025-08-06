package org.maengle.file.controllers;

import lombok.Data;

@Data
public class RequestThumb {
    private Long seq;
    private int width;
    private int height;
    private boolean crop; // 이미지 자름 여부
    private String url;
}
