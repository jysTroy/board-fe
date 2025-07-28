package org.maengle.model.services;

import lombok.RequiredArgsConstructor;
import org.maengle.model.entities.Model;
import org.maengle.model.exceptions.ModelNotFoundException;
import org.maengle.model.repositories.ModelRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelInfoService {

	private final ModelRepository modelRepository;

	public Model get(Long seq) {

		Model item = modelRepository.findById(seq).orElseThrow(ModelNotFoundException::new);

		return item;
	}
}
