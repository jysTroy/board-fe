package org.maengle.model.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.admin.Model.controllers.RequestModel;
import org.maengle.file.services.FileInfoService;
import org.maengle.model.entities.Model;
import org.maengle.model.exceptions.ModelNotFoundException;
import org.maengle.model.repositories.ModelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelInfoService {

	private final ModelMapper modelMapper;
	private final ModelRepository modelRepository;
	private final FileInfoService fileInfoService;
	private final HttpServletRequest request;

	// 모델 id로 검색
	public Model get(Long seq) {

		Model item = modelRepository.findById(seq).orElseThrow(ModelNotFoundException::new);
		addInfo(item);

		return item;
	}

	// 상품 조회
	public RequestModel getForm(Long seq) {
		Model item = get(seq);
		return modelMapper.map(item, RequestModel.class);
	}



	// 상품 추가 정보 처리
	private void addInfo(Model item) {
		// 업로드한 파일 처리
		String mid = item.getMid();
		item.setMainImages(fileInfoService.getList(mid, "main"));
		item.setListImages(fileInfoService.getList(mid, "list"));
	}


}
