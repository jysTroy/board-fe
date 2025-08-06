package org.maengle.board.services;

import lombok.RequiredArgsConstructor;
import org.maengle.board.entities.Board;
import org.maengle.board.repositories.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardListService {
    private final BoardRepository boardRepository;

    // 전체 게시판 목록 서비스
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }
}