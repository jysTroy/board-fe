package org.maengle.file.services;

import org.junit.jupiter.api.Test;
import org.maengle.file.controllers.RequestUpload;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.repositories.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Rollback(value = false)
@SpringBootTest
@Transactional
public class FileUploadServiceTest {

    @Autowired
    private FileUploadService uploadService;

    @Autowired
    private FileInfoRepository repository;

    @Test
    public void test() {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "image".getBytes()
        );

        RequestUpload upload = new RequestUpload();
        String gid = UUID.randomUUID().toString();
        upload.setFiles(new MockMultipartFile[]{file});
        upload.setGid(gid);
        upload.setLocation("test_location");
        upload.setSingle(false);
        upload.setImageOnly(true);


        List<FileInfo> result = uploadService.uploadProcess(upload);


        assertNotNull(result);
        assertEquals(1, result.size());

        FileInfo uploaded = result.get(0);
        assertEquals("test.jpg", uploaded.getFileName());
        assertTrue(uploaded.getContentType().startsWith("image"));
        assertTrue(repository.findById(uploaded.getSeq()).isPresent());

        System.out.println("업로드 파일 번호: " + uploaded.getSeq());
        System.out.println("그룹id: " + gid);
    }
}