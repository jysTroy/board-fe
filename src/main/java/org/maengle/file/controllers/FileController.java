package org.maengle.file.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.services.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/file", "/file"}) // 기본 경로를 지정
public class FileController {

	private final FileUploadService fileUploadService;
	private final FileDownloadService fileDownloadService;
	private final FileInfoService fileInfoService;
	private final FileDeleteService fileDeleteService;
	private final ThumbnailService thumbnailService;


	@PostMapping("/upload")
	public List<FileInfo> upload(RequestUpload form, @RequestPart("file")MultipartFile[] files) {
		form.setFiles(files);
		List<FileInfo> items = fileUploadService.uploadProcess(form);

		return items;
	}

	// 파일 리스트 조회 처리
	@GetMapping({"/list/{gid}", "/list/{gid}/{location}"})
	public List<FileInfo> list(@PathVariable("gid") String gid, @PathVariable(name="location", required = false) String location) {
		List<FileInfo> items = fileInfoService.getList(gid, location);

		return items;
	}

	// 파일 한개 조회 처리
	@GetMapping("/info/{seq}")
	public FileInfo info(Long seq) {
		FileInfo item = fileInfoService.get(seq);

		return item;
	}

	// 파일 1개 삭제 처리
	@DeleteMapping("/delete/{seq}")
	public FileInfo delete(@PathVariable("seq") Long seq) {
		FileInfo item = fileDeleteService.deleteProcess(seq);

		return item;
	}

	// gid를 활용해 파일 리스트 삭제 처리
	@DeleteMapping({"/deletes/{gid}","/deletes/{gid}/{location}"})
	public List<FileInfo> deletes(@PathVariable("gid") String gid, @PathVariable(name="location", required = false) String location) {
		List<FileInfo> items = fileDeleteService.process(gid, location);

		return items;

	}


	@GetMapping("/download/{seq}")
	public void download(@PathVariable Long seq) {
		fileDownloadService.process(seq);
	}

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

}
