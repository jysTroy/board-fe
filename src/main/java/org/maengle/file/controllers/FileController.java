package org.maengle.file.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.maengle.file.services.FileDownloadService;
import org.maengle.file.services.ThumbnailService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/file", "/file"})
public class FileController {

	private final FileDownloadService fileDownloadService;

	private final ThumbnailService thumbnailService;

	/*
	 * RequestThumb 타입으로 썸네일 생성에 필요한 파라미터 전달
	 * response HttpServletResponse를 사용해서 이미지 데이터 출력
	 */
	@GetMapping("/thumb") // 클라이언트가 /thumb 경로로 요청할 때 호출
	public void thumb(RequestThumb form, HttpServletResponse response) {
		String path = thumbnailService.create(form); // ThumbnailService.create 호출, 썸네일 경로 생성 또는 기존 썸네일 경로 반환
		if (!StringUtils.hasText(path)) { // 썸네일 경로(경로 문자열)가 비어 있다면
			return; // 응답 없이 종료
		}

		File file = new File(path); // path 문자열로 File 객체 생성
		try (FileInputStream fis = new FileInputStream(file); // 입력 스트림 생성
			BufferedInputStream bis = new BufferedInputStream(fis)) { // FileInputStream에 버퍼 씌우기

			String contentType = Files.probeContentType(file.toPath()); // 파일 타입을 찾아서
			response.setContentType(contentType); // 웹 응답에 알려주기

			OutputStream out = response.getOutputStream(); // 출력 스트림 가져오기
			out.write(bis.readAllBytes()); // 모든 바이트 데이터 한번에 읽어 클라이언트에게 전송(out.write)

		} catch (IOException e) {
			// 파일 읽기나 전송 중 문제가 생기면 오류 내용을 화면에 출력
			e.printStackTrace();
		}
	}


	@GetMapping("/download/{seq}")
	public void download(@PathVariable Long seq) {
		fileDownloadService.process(seq);
	}
}
