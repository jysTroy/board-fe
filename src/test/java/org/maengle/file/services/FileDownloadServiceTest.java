package org.maengle.file.services;

import org.junit.jupiter.api.Test;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.repositories.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class FileDownloadServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FileInfoRepository fileInfoRepository;

	@Test
	void test() throws Exception {
		// 1. 임시 파일 생성
		File tempFile = File.createTempFile("testfile-", ".txt");
		try (FileWriter fw = new FileWriter(tempFile)) {
			fw.write("단위 테스트용 텍스트");
		}

		// 2. 테스트용 FileInfo 구성
		FileInfo fileInfo = new FileInfo();
		fileInfo.setFilePath(tempFile.getAbsolutePath());
		fileInfo.setFileName("테스트.txt");
		fileInfo.setContentType("text/plain");

		// 3. FileInfoService mocking
		FileInfoService infoService = mock(FileInfoService.class);
		when(infoService.get(1L)).thenReturn(fileInfo);

		// 4. MockHttpServletResponse 사용
		MockHttpServletResponse response = new MockHttpServletResponse();

		// 5. 테스트 대상 서비스 생성
		FileDownloadService service = new FileDownloadService(infoService, response);

		// 6. 테스트 실행
		service.process(1L);

		// 7. 결과 검증
		assertEquals("text/plain", response.getContentType());
		assertTrue(response.getContentAsByteArray().length > 0);
		assertTrue(response.getHeader("Content-Disposition").contains("filename="));
	}

}
