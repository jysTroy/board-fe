package org.maengle.board.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.board.controllers.RequestComment;
import org.maengle.board.entities.BoardData;
import org.maengle.board.entities.Comment;
import org.maengle.board.entities.QComment;
import org.maengle.board.repositories.BoardDataRepository;
import org.maengle.board.repositories.CommentRepository;
import org.maengle.member.libs.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class CommentUpdateService {
    private final CommentRepository commentRepository;
    private final BoardInfoService boardInfoService;
    private final BoardDataRepository boardDataRepository;
    private final HttpServletRequest request;
    private final MemberUtil memberUtil;

    public Comment process(RequestComment form) {
        Long seq = form.getSeq();
        Comment item;

        if (seq != null && (item = commentRepository.findById(seq).orElse(null)) != null) {
            // 기존 댓글 수정
        } else {
            // 새 댓글 작성
            item = new Comment();
            item.setMember(memberUtil.getMember());

            BoardData boardData = boardInfoService.get(form.getBoardDataSeq());
            item.setItem(boardData);

            item.setIp(request.getRemoteAddr());
            item.setUa(request.getHeader("User-Agent"));
        }

        item.setCommenter(form.getCommenter());
        item.setContent(form.getContent());

        commentRepository.saveAndFlush(item);

        // 댓글 갯수 업데이트
        updateCommentCount(form.getBoardDataSeq());

        return item;
    }

    /**
     * 게시글별 댓글 갯수 업데이트
     * @param boardDataSeq
     */
    public void updateCommentCount(Long boardDataSeq) {
        QComment comment = QComment.comment;
        long total = commentRepository.count(comment.item.seq.eq(boardDataSeq));

        BoardData item = boardDataRepository.findById(boardDataSeq).orElse(null);
        if (item != null) {
            item.setCommentCount((int)total);
            boardDataRepository.saveAndFlush(item);
        }
    }
}

