package org.maengle.board.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.maengle.board.entities.Board;
import org.maengle.board.entities.BoardData;
import org.maengle.board.entities.Comment;
import org.maengle.board.services.*;
import org.maengle.board.services.configs.BoardConfigInfoService;
import org.maengle.board.validators.BoardValidator;
import org.maengle.board.validators.CommentValidator;
import org.maengle.file.constants.FileStatus;
import org.maengle.file.services.FileInfoService;
import org.maengle.global.annotations.ApplyCommonController;
import org.maengle.global.exceptions.script.AlertException;
import org.maengle.global.libs.Utils;
import org.maengle.global.search.ListData;
import org.maengle.member.libs.MemberUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@ApplyCommonController
@RequiredArgsConstructor
@RequestMapping("/board")
// 싱글톤 객체라 다른 사용자가 접근하면 문제가됨(값이 바뀜) 그래서 세션 이용
@SessionAttributes({"board"})
public class BoardController {

    private final Utils utils;
    private final MemberUtil memberUtil;
    private final BoardConfigInfoService configInfoService;
    private final BoardUpdateService updateService;
    private final BoardInfoService infoService;
    private final BoardDeleteService deleteService;
    private final BoardViewCountService viewCountService;
    private final BoardAuthService authService;
    private final FileInfoService fileInfoService;
    private final BoardValidator boardValidator;
    private final CommentValidator commentValidator;
    private final CommentUpdateService commentUpdateService;
    private final CommentInfoService commentInfoService;
    private final PasswordEncoder encoder;
    private final HttpSession session;
    private final HttpServletRequest request;

    // 게시글 목록
    @GetMapping("/list/{bid}")
    public String list(@PathVariable("bid") String bid, @ModelAttribute BoardSearch search, Model model) {
        commonProcess(bid, "list", model);

        ListData<BoardData> data = infoService.getList(bid, search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return "front/board/list";
    }

    // 게시글 작성
    @GetMapping("/write/{bid}")
    public String write(@PathVariable("bid") String bid, RequestBoard form, Model model) {
        commonProcess(bid, "write", model);
        form.setBid(bid);
        form.setGid(UUID.randomUUID().toString());

        if (memberUtil.isLogin()) {
            form.setPoster(memberUtil.getMember().getName());
        }

        return "front/board/write";
    }

    // 게시글 수정
    @GetMapping("/update/{seq}")
    public String update(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "update", model);
        RequestBoard form = infoService.getForm(seq);
        model.addAttribute("requestBoard", form);;

        return "front/board/update";
    }

    // 게시글 저장
    @PostMapping("/save")
    public String save(@Valid RequestBoard form, Errors errors, Model model, @SessionAttribute("board") Board board) {
        String mode = form.getMode();
        commonProcess(form.getBid(), mode, model);

        boardValidator.validate(form, errors);

        if (errors.hasErrors()) {
            String gid = form.getGid();
            form.setEditorImages(fileInfoService.getList(gid, "editor", FileStatus.ALL));
            form.setAttachFiles(fileInfoService.getList(gid, "attach", FileStatus.ALL));

            return "front/board/" + mode;
        }

        // 게시글 저장 처리
        BoardData item = updateService.process(form);
        String redirectUrl = board.isAfterWritingRedirect() ? "view/" + item.getSeq() : "list/" + form.getBid();

        return "redirect:/board/" + redirectUrl;
    }

    // 게시글 보기
    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq, Model model, @SessionAttribute("board") Board board) {
        commonProcess(seq, "view", model);

        if (board.isShowViewList()) { // 게시글 보기 하단에 목록 노출
            BoardSearch search = new BoardSearch();
            ListData<BoardData> data = infoService.getList(board.getBid(), search);

            model.addAttribute("items", data.getItems());
            model.addAttribute("pagination", data.getPagination());
            model.addAttribute("boardSearch", search);
        }

        // 게시글 조회수 업데이트
        viewCountService.update(seq);

        // 댓글 기본값 처리
        if (board != null && board.isComment()) {
            if (board.isCommentable()) { // 댓글 작성 가능 여부
                RequestComment commentForm = new RequestComment();
                if (memberUtil.isLogin()) { // 로그인 상태라면 로그인한 회원 이름으로 초기값
                    commentForm.setCommenter(memberUtil.getMember().getName());
                }
                commentForm.setBoardDataSeq(seq);
                commentForm.setMode("comment_write");
                model.addAttribute("requestComment", commentForm);
            }

            // 댓글 목록
            model.addAttribute("comments", commentInfoService.getList(seq));
        }


        return "front/board/view";
    }

    @PostMapping("/comment")
    public String comment(@Valid RequestComment form, Errors errors, Model model) {

        commentValidator.validate(form, errors);

        if (errors.hasErrors()) {
            for (Map.Entry<String, List<String>> entry : utils.getErrorMessages(errors).entrySet()) {
                String message = entry.getValue().getFirst();
                throw new AlertException(message, HttpStatus.BAD_REQUEST);
            }
        }
        // 댓글 작성 처리
        Comment item = commentUpdateService.process(form);

        // 댓글 작성이 완료되면 부모창을 새로고침
//        model.addAttribute("script", String.format("parent.location.replace('%s/board/view/%s#comment-%s')", form.getBoardDataSeq(), item.getSeq()));
        model.addAttribute("script", "parent.location.reload();");
        return "common/_execute_script";
    }

    // 댓글 수정
    @GetMapping("/comment/{seq}")
    public String commentUpdate(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "comment_update", model);

        RequestComment form = commentInfoService.getForm(seq);
        model.addAttribute("requestComment", form);

        return "front/board/comment_update";
    }



    // 게시글 삭제
    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "delete", model);
        deleteService.process(seq);

        Board board = (Board)model.getAttribute("board");

        return "redirect:/board/list/" + board.getBid();
    }


    /**
     * bid 기준의 공통 처리
     *  - 게시글 설정조회가 공통 처리
     *
     * @param bid
     * @param mode
     * @param model
     */
    private void commonProcess(String bid, String mode, Model model) {
        Board board = configInfoService.get(bid);

        authService.check(mode, bid); // 글쓰기, 글목록에서의 권한 체크

        mode = StringUtils.hasText(mode) ? mode : "list";

        List<String> addCommonScript = new ArrayList<>();
        List<String> addCss = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        String pageTitle = board.getName(); // 게시판 명

        String skin = board.getSkin();
        addCss.add("board/style"); // 스킨과 상관없는 공통 스타일
        addCss.add(String.format("board/%s/style", skin)); // 스킨별 스타일

        addScript.add("board/common"); // 스킨 상관없는 공통 자바스크립트

        if (mode.equals("write") || mode.equals("update")) { // 등록, 수정
            if (board.isAttachFile() || (board.isImageUpload() && board.isEditor())) {
                addCommonScript.add("fileManager");
            }

            if (board.isEditor()) { // 에디터를 사용하는 경우, CKEDITOR5 스크립트를 추가
                addCommonScript.add("ckeditor5/ckeditor");
            }

            addScript.add(String.format("board/%s/form", skin)); // 스킨별 양식 관련 자바스크립트
        } else if (mode.equals("view")) { // 게시글 보기
            BoardData item = (BoardData)model.getAttribute("item");
            pageTitle = item.getSubject() + " - " + pageTitle;
        }

        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("addCss", addCss);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("board", board);
        model.addAttribute("mode", mode);
    }

    /**
     * seq 기준의 공통 처리
     *  - 게시글 조회가 공통 처리 ...
     * @param seq
     * @param mode
     * @param model
     */
    private void commonProcess(Long seq, String mode, Model model) {
        BoardData item = null;

        if (mode.equals("comment_update") || mode.equals("comment_delete")) { // 댓글 수정, 삭제일 경우
            Comment comment =commentInfoService.get(seq);
            item = comment.getItem();
            model.addAttribute("comment", comment);
        } else {
            item = infoService.get(seq);
        }
        model.addAttribute("item", item);

        authService.check(mode, seq); // 글보기, 글수정, 댓글 수정, 댓글 삭제 시 권한 체크

        Board board = item.getBoard();
        commonProcess(board.getBid(), mode, model);
    }
}
