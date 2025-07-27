package org.maengle.model;

import org.junit.jupiter.api.Test;
import org.maengle.admin.model.RequestModel;
import org.maengle.model.entities.Model;
import org.maengle.model.repositories.ModelRepository;
import org.maengle.model.services.ModelUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ModelUpdateServiceTest {

    @Autowired
    private ModelUpdateService modelUpdateService;

    @Autowired
    private ModelRepository modelRepository;

    @Test
    void test1() {
        RequestModel form = new RequestModel();
        form.setMid("model_001");
        form.setName("테스트모델");
        form.setDescription("설명");

        Model saved = modelUpdateService.process(form);

        System.out.println("== 저장 된 모델 ==");
        System.out.println(saved);
        System.out.println("== 생성일자 ===");
        System.out.println(saved.getCreatedAt());

        Model found = modelRepository.findById(saved.getSeq()).orElse(null);
        assert found != null;
        assert found.getName().equals("테스트모델");
    }

    @Test
    void test2() {
        Model origin = new Model();
        origin.setMid("model_002");
        origin.setName("원래이름");
        origin.setDescription("원래설명");
        modelRepository.saveAndFlush(origin);

        RequestModel form = new RequestModel();
        form.setSeq(origin.getSeq());
        form.setMid("model_002");
        form.setName("수정된이름");
        form.setDescription("수정된설명");

        Model updated = modelUpdateService.process(form);
        System.out.println("== 수정된모델 ==");
        System.out.println(updated);
        assert updated.getName().equals("수정된이름");
    }

}
