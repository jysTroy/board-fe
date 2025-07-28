package org.maengle.board.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.board.entities.Board;
import org.maengle.board.services.configs.BoardConfigInfoService;
import org.maengle.global.annotations.ApplyCommonController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@ApplyCommonController
@RequiredArgsConstructor
@RequestMapping("/board")
// 싱글톤 객체라 다른 사용자가 접근하면 문제가됨(값이 바뀜) 그래서 세션 이용
@SessionAttributes({"board"})
public class BoardController {

    private final BoardConfigInfoService configInfoService;

    @ModelAttribute("board")
    public Board getBoard() {
        return new Board();
    }

    // 게시글 목록
    @GetMapping("/list/{bid}")
    public String list(@PathVariable("bid") String bid, @ModelAttribute BoardSearch search, Model model) {
        commonProcess(bid, "list", model);
        return "front/board/list";
    }

    // 게시글 작성
    @GetMapping("/write/{bid}")
    public String write(@PathVariable("bid") String bid, RequestBoard form, Model model) {
        commonProcess(bid, "write", model);
        form.setBid(bid);
        form.setGid(UUID.randomUUID().toString());

        return "front/board/write";
    }

    private void commonProcess(String bid, String mode, Model model) {
        Board board = configInfoService.get(bid);
        mode = StringUtils.hasText(mode) ? mode : "list";

        List<String> addCommonScript = new ArrayList<>();
        List<String> addCss = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        String pageTitle = board.getName(); // 게시판명

        addCss.add("board/style"); //  공통 스타일
        addScript.add("board/common"); // 공통 자바스크립트

        if (mode.equals("write") || mode.equals("update")) { // 등록, 수정
            if (board.isAttachFile() || (board.isImageUpload() && board.isEditor())) {
                addCommonScript.add("fileManager");
            }
        }

        if (board.isEditor()) { // 에디터를 사용하는 경우, CKEDITOR5 스크립트를 추가
            addCommonScript.add("ckeditor/ckeditor");
        }

        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("addCss", addCss);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("board", board);
    }
}
