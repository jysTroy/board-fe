package org.maengle.file.services;

import org.junit.jupiter.api.Test;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.repositories.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
@Transactional
public class FileDeleteServiceTest {

    @Autowired
    private FileDeleteService fileDeleteService;

    @Autowired
    private FileInfoService fileInfoService;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Test
    void test() {
        // 임시 데이터
        FileInfo fileInfo = new FileInfo();
        fileInfo.setGid(UUID.randomUUID().toString());
        fileInfo.setFileName("test.jpg");
        fileInfo.setContentType("image/jpeg");
        fileInfo.setExtension("jpg");
        fileInfo.setCreatedBy("tester@test.com");
        fileInfo.setFilePath("/tmp/test.jpg");

        FileInfo saved = fileInfoRepository.saveAndFlush(fileInfo);
        Long seq = saved.getSeq();
        System.out.println(seq);

        // 삭제 처리
        fileDeleteService.deleteProcess(seq);
        boolean exists = fileInfoRepository.findById(seq).isPresent();
        System.out.println(exists);

    }
}
