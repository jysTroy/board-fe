package org.maengle.board.services.configs;

import lombok.RequiredArgsConstructor;
import org.maengle.admin.board.controllers.RequestBoard;
import org.maengle.board.entities.Board;
import org.maengle.board.exceptions.BoardNotFoundException;
import org.maengle.board.repositories.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardConfigInfoService {

    private final ModelMapper mapper;
    private final BoardRepository repository;

    // 게시판 설정 한 개 조회
    public Board get(String bid) {
        Board item = repository.findById(bid).orElseThrow(BoardNotFoundException::new);

        addInfo(item); // 추가 정보 공통 처리

        return item;
    }

    // 게시판 설정 수정시 필요한 객체로 조회
    public RequestBoard getForm(String bid)
    {
        Board board = get(bid);

        return mapper.map(board, RequestBoard.class);
    }

    // 게시판 설정 추가 정보 가공 처리
    private void addInfo(Board item) {

    }
}
