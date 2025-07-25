package org.maengle.model.services;

import lombok.RequiredArgsConstructor;
import org.maengle.admin.model.RequestModel;
import org.maengle.model.entities.Model;
import org.maengle.model.repositories.ModelRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelUpdateService {
    private final ModelRepository modelRepository;

    // 모델 등록/수정 화면에서 입력한 값을 requestModel에 담아서 넘겨줌
    public Model process(RequestModel form) {
        Long seq = form.getSeq(); // seq로 구분

        // seq 값이 없거나 0보다 작으면 새 모델 객체 생성, 아니면 기존 모델 불러오기
        Model item = (seq == null || seq < 1L)
                ? new Model()
                : modelRepository.findById(seq).orElseGet(Model::new);

        // 모델 등록일 경우에만 고유 MID 설정
        if (seq == null || seq.equals("add")) {
            item.setMid(form.getMid());
        }

        // 공통 저장 정보
        item.setSeq(form.getSeq());
        item.setMid(form.getMid());
        item.setName(form.getName());
        item.setDescription(form.getDescription());

        modelRepository.saveAndFlush(item); // DB 저장, 업로드 완료 처리 미완성

        return item;

    }
}
