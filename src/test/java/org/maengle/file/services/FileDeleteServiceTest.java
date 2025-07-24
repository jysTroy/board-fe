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
        fileInfo.setFileName("test.txt");
        fileInfo.setContentType("image/json");
        fileInfo.setExtension("txt");
        fileInfo.setCreatedBy("tester@test.com");
        fileInfo.setFilePath("/tmp/test.txt");

        FileInfo saved = fileInfoRepository.saveAndFlush(fileInfo);
        Long seq = saved.getSeq();
        System.out.println(seq==1 ? "뷀부레부렙뤠부" : 1);

        // 삭제 처리
        fileDeleteService.deleteProcess(seq);
        boolean exists = fileInfoRepository.findById(seq).isPresent();
        System.out.println(exists);

    }
}
