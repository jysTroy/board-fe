package org.maengle.file.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.services.FileDownloadService;
import org.maengle.file.services.FileInfoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/file", "/file"})
public class FileController {

	private final FileDownloadService fileDownloadService;
	private final FileInfoService fileInfoService;


	@GetMapping("/download/{seq}")
	public void download(@PathVariable Long seq) {
		fileDownloadService.process(seq);
	}


	// 브라우저에 있는 이미지 원본을 직접 불러와 보여주도록 하기 위해 정의
	@GetMapping("/image/{seq}")
	public ResponseEntity<byte[]> showImage(@PathVariable("seq") Long seq) {
		FileInfo item = fileInfoService.get(seq);

		String contentType = item.getContentType();
		byte[] bytes = null;
		File file = new File(item.getFilePath());
		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
			bytes = bis.readAllBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf(contentType));

		return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
	}

}
