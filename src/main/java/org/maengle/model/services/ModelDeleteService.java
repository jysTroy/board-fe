package org.maengle.model.services;

import lombok.RequiredArgsConstructor;
import org.maengle.file.services.FileDeleteService;
import org.maengle.global.exceptions.script.AlertException;
import org.maengle.global.libs.Utils;
import org.maengle.model.entities.Model;
import org.maengle.model.repositories.ModelRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class ModelDeleteService {

	private final FileDeleteService fileDeleteService;
	private final ModelViewService modelViewService;
	private final ModelRepository modelRepository;
	private final Utils utils;

	/**
	 * 모델 한개 삭제
	 * @param seq
	 */
	public void process(Long seq) {
		Model item = modelViewService.get(seq);

		String gid = item.getGid();

		modelRepository.delete(item);
		modelRepository.flush();

		// 업로드된 파일 정보 삭제
		fileDeleteService.process(gid);
	}

	/**
	 * 관리자에서 모델 목록 삭제
	 * @param idxes
	 */
	public void process(List<Integer> idxes) {
		if (idxes == null || idxes.isEmpty()) {
			throw new AlertException("삭제할 모델을 선택하세요.");
		}

		for (int idx : idxes) {
			Long seq = Long.valueOf(utils.getParam("seq_" + idx));
			process(seq);
		}
	}
}
