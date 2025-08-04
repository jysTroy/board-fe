package org.maengle.model.services;

import lombok.RequiredArgsConstructor;
import org.maengle.admin.model.controllers.RequestModel;
import org.maengle.file.services.FileUploadService;
import org.maengle.model.entities.Model;
import org.maengle.model.repositories.ModelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelUpdateService {
    private final ModelRepository modelRepository;
    private final FileUploadService fileUploadService;
    private final ModelMapper mapper;

    // 모델 등록,수정 화면에서 입력한 값을 requestModel에 담아서 넘겨줌
    public Model process(RequestModel form) {
        Model item = mapper.map(form, Model.class);

        modelRepository.saveAndFlush(item);

        // 파일 업로드 완료 처리
        fileUploadService.processDone(form.getGid());

        return item;

    }
}
