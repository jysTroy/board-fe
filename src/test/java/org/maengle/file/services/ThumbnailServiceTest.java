package org.maengle.file.services;

import org.junit.jupiter.api.Test;
import org.maengle.file.controllers.RequestThumb;
import org.maengle.file.controllers.RequestUpload;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.repositories.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.UUID;

@SpringBootTest
public class ThumbnailServiceTest {

    @Autowired
    private ThumbnailService thumbnailService;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private FileUploadService uploadService;

    @Test
    void test1() {
        // 실제 파일 업로드 없이 테스트용으로 파일 객체 생성
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "image".getBytes()
        );

        RequestUpload upload = new RequestUpload(); // 업로드 요청 객체 생성

        upload.setFiles(new MockMultipartFile[]{file}); // MockMultipartFile 배열로 업로드
        upload.setGid(UUID.randomUUID().toString()); // 업로드 그룹 ID 생성 후 세팅
        upload.setLocation("C:/uploads"); // 업로드할 위치 지정
        upload.setSingle(false); // 다중 업로드 여부 설정 false가 다중 업로드
        upload.setImageOnly(true); // 이미지 파일만 허용

        List<FileInfo> result = uploadService.uploadProcess(upload); // 업로드 서비스 호출
        FileInfo uploaded = result.get(0); // 업로드 파일중 첫번째 파일 정보 가져오기

        // 썸네일 생성
        RequestThumb request = new RequestThumb(); // 썸네일 생성 요청 객체 생성
        request.setSeq(uploaded.getSeq()); // 썸네일 생성 원본 파일의 시퀀스 번호 세팅
        request.setWidth(100);
        request.setHeight(100);
        request.setCrop(true);

        // 썸네일 서비스 호출, 썸네일 생성 후 URL 반환
        String thumbnailUrl = thumbnailService.create(request);
        System.out.println("생성된 썸네일 URL: " + thumbnailUrl);
    }
}
