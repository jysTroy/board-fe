package org.maengle.board.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.board.entities.Board;
import org.maengle.board.services.BoardListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardListController {

    private final BoardListService boardListService;

    @GetMapping("/board/all")
    public String showAllBoards(Model model) {
        List<Board> boards = boardListService.getAllBoards();
        model.addAttribute("boards", boards);
        return "front/board/all"; // 뷰 파일로 연결
    }
}
