package org.maengle.board.services;

import lombok.RequiredArgsConstructor;
import org.maengle.board.entities.BoardData;
import org.maengle.board.repositories.BoardDataRepository;
import org.maengle.file.services.FileDeleteService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardDeleteService {
    private final BoardInfoService infoService;
    private final BoardDataRepository repository;
    private final FileDeleteService deleteService;

    public void process(Long seq) {
        BoardData item = infoService.get(seq);

        String gid = item.getGid();
        repository.delete(item);
        deleteService.process(gid);

        repository.flush();

    }
}