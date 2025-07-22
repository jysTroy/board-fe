package org.maengle.file.services;

import org.junit.jupiter.api.Test;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.repositories.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class FileInfoServiceTest {

    @Autowired
    private FileInfoService service;

    @Autowired
    private FileInfoRepository repository;

    @Test
    void test() {
        // 1. 테스트용 FileInfo 생성
        FileInfo file = new FileInfo();
        file.setGid("testGid");
        file.setFileName("testFile.jpg");
        file.setLocation("uploads/testFile.jpg");
        file.setExtension("jpg");
        file.setContentType("image/jpeg");
        file.setDone(false);

        // 2. 저장
        FileInfo saved = repository.save(file);

        // 3. 서비스로 조회
        FileInfo fetched = service.get(saved.getSeq()); // ID는 seq 필드
        System.out.println(fetched);

    }
}