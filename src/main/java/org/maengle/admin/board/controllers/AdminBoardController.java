package org.maengle.admin.board.controllers;

import org.maengle.admin.global.controllers.CommonController;
import org.maengle.global.search.CommonSearch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/board")
public class AdminBoardController extends CommonController {

    @Override
    @ModelAttribute("mainCode")
    public String mainCode() {
        return "board";
    }

    // 게시판 목록
    @GetMapping({"", "/list"})
    public String list(@ModelAttribute CommonSearch search, Model model) {

        // 보드 인포서비스 만드는 공사중
//        ListData<Board> data =

        return "admin/board/list";
    }

}
