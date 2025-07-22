package org.maengle.file.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.exceptions.FileNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Lazy
@Service
@RequiredArgsConstructor
public class FileDownloadService {
	private final FileInfoService infoService;
	private final HttpServletResponse response;

	public void process(Long seq) {
		FileInfo item = infoService.get(seq);

		// 파일이 확인 후 없으면 예외처리
		File file = new File(item.getFilePath());
		if (!file.exists()) {
			throw new FileNotFoundException();
		}

		// 파일명 가져와서 파일명을 UTF-8 형식으로 변환, ISO_8859_1로 깨짐 방지
		String fileName = new String(item.getFileName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

		// 파일 형식을 가져오고 contentType이 있으면 그대로 쓰고 없으면 application/octet-stream
		// application/octet-stream -> contentType의 가장 큰 분류
		String contentType = item.getContentType();
		contentType = StringUtils.hasText(contentType) ? contentType : "application/octet-stream";

		// fis로 파일읽기, bis로 성능 개선
		try (FileInputStream fis = new FileInputStream(file);
			 BufferedInputStream bis = new BufferedInputStream(fis)) {
			// 헤더 출력
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			response.setContentType(contentType);
			// 브라우저 캐시 방지 처리
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setIntHeader("Expires", 0);
			// 파일 크기 지정
			response.setContentLengthLong(file.length());

			// 바디 출력
			OutputStream out = response.getOutputStream();
			out.write(bis.readAllBytes());


		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
