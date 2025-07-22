package org.maengle.file.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.file.services.FileDownloadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/file", "/file"})
public class FileController {

	private final FileDownloadService fileDownloadService;


	@GetMapping("/download/{seq}")
	public void download(@PathVariable Long seq) {
		fileDownloadService.process(seq);
	}
}
