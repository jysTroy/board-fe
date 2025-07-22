package org.maengle.file.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.file.services.ThumbnailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/thumb")
public class ThumbnailController {

    private final ThumbnailService thumbnailService;

    @GetMapping("/create")
    public String createThumbnail(
            @RequestParam(required = false)
            Long seq,

            @RequestParam(required = false)
            String url,

            @RequestParam(defaultValue = "50")
            int width,

            @RequestParam(defaultValue = "50")
            int height,

            @RequestParam(defaultValue = "false")
            boolean crop) {
        RequestThumb requestThumb = new RequestThumb();
        requestThumb.setSeq(seq);
        requestThumb.setUrl(url);
        requestThumb.setWidth(width);
        requestThumb.setHeight(height);
        requestThumb.setCrop(crop);

        return thumbnailService.create(requestThumb);
    }
}
