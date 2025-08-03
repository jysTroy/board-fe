package org.maengle.model.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.admin.model.controllers.RequestModel;
import org.maengle.file.services.FileDeleteService;
import org.maengle.file.services.FileUploadService;
import org.maengle.global.libs.Utils;
import org.maengle.model.entities.Model;
import org.maengle.model.repositories.ModelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelUpdateService {
    private final ModelRepository modelRepository;
    private final FileUploadService fileUploadService;
    private final FileDeleteService fileDeleteService;
    private final HttpServletRequest request;
    private final ModelMapper mapper;
    private final Utils utils;

    // 모델 등록,수정 화면에서 입력한 값을 requestModel에 담아서 넘겨줌
    public Model process(RequestModel form) {
        Model item = mapper.map(form, Model.class);

        modelRepository.saveAndFlush(item);

        // 파일 업로드 완료 처리
        fileUploadService.processDone(form.getGid());

        return item;

    }

    // 모델 삭제 + 관련 파일 삭제
    public void deleteModel(Long seq, String gid) {
        Model model = modelRepository.findById(seq)
                .orElseThrow(() -> new EntityNotFoundException("해당 모델을 찾을 수 없습니다."));

        fileDeleteService.process(gid);     // 파일 실제 삭제
        modelRepository.delete(model);      // 모델 삭제
        modelRepository.flush();            // 즉시 반영
    }

}
