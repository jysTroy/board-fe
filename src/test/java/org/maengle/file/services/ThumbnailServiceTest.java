package org.maengle.file.services;

import org.junit.jupiter.api.Test;
import org.maengle.file.controllers.RequestThumb;
import org.maengle.file.repositories.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ThumbnailServiceTest {

    @Autowired
    private ThumbnailService thumbnailService;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Test
    void test1() {
        Long seq = 1L;

        RequestThumb request = new RequestThumb();
        request.setSeq(seq);
        request.setWidth(100);
        request.setHeight(100);
        request.setCrop(true);

        String thumbnailUrl = thumbnailService.create(request);
        System.out.println("생성된 썸네일 URL: " + thumbnailUrl);
    }

    @Test
    void test2() {
        String url = "/file/1.jpg";

        RequestThumb request = new RequestThumb();
        request.setUrl(url);
        request.setWidth(150);
        request.setHeight(150);
        request.setCrop(false);

        String thumbnailUrl = thumbnailService.create(request);
        System.out.println("생성된 썸네일 URL: " + thumbnailUrl);
    }
}
