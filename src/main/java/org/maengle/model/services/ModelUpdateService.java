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
    private final ModelInfoService modelInfoService;

    public Model process(RequestModel form) {
        Long seq = form.getSeq();

        Model item = (seq == null || seq < 1L)
                ? new Model()
                : modelRepository.findById(seq).orElseGet(Model::new);

        if (seq == null || seq.equals("add")) {

            item.setMid(form.getMid());
        }

        item.setSeq(form.getSeq());
        item.setMid(form.getMid());
        item.setName(form.getName());
        item.setDescription(form.getDescription());

        modelRepository.saveAndFlush(item);

        //ModelInfoService.processDone(form.getMid());

    }
}
