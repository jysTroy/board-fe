package org.maengle.board.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.maengle.board.controllers.RequestComment;
import org.maengle.board.entities.Comment;
import org.maengle.board.entities.QComment;
import org.maengle.board.exceptions.CommentNotFoundException;
import org.maengle.board.repositories.CommentRepository;
import org.maengle.member.entities.Member;
import org.maengle.member.libs.MemberUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Lazy
@Service
@Transactional
@RequiredArgsConstructor
public class CommentInfoService {
    private final CommentRepository commentRepository;
    private final HttpSession session;
    private final JPAQueryFactory queryFactory;
    private final MemberUtil memberUtil;
    private final ModelMapper mapper;

    /**
     * 댓글 한 개 조회
     * @param seq
     * @return
     */
    public Comment get(Long seq) {
        Comment item = commentRepository.findById(seq).orElseThrow(CommentNotFoundException::new);

        addInfo(item); // 추가 정보 처리

        return item;
    }

    public RequestComment getForm(Long seq) {
        Comment item= get(seq);
        RequestComment form = mapper.map(item, RequestComment.class);
        form.setMode("comment_update");
        form.setBoardDataSeq(item.getItem().getSeq());
        return form;
    }

    /**
     * 게시글별 댓글 목록
     * @param boardDataSeq : 게시글 번호
     * @return
     */
    public List<Comment> getList(Long boardDataSeq) {
        QComment comment = QComment.comment;

        List<Comment> items = queryFactory.selectFrom(comment)
                .leftJoin(comment.member)
                .fetchJoin()
                .where(comment.item.seq.eq(boardDataSeq))
                .orderBy(comment.createdAt.asc())
                .fetch();

        // 추가 정보 처리
        items.forEach(this::addInfo);

        return items;
    }

    /**
     * 추가 정보 처리
     * @param item
     */
    private void addInfo(Comment item) {
        boolean editable = true;

        /**
         * 직접 작성한 게시글 여부
         * 1. 회원
         *      - 댓글을 작성한 회원 번호와 로그인한 회원의 회원번호가 일치하는지
         */
        Member commentMember = item.getMember();

        if (commentMember != null) { // 회원이 작성한 댓글만 처리
            Member member = memberUtil.getMember();

            item.setMine(memberUtil.isLogin() && member.getUserUuid().equals(commentMember.getUserUuid()));

            if (!memberUtil.isAdmin()) {
                editable = item.isMine();
            }
        } else {
            // 비회원 댓글은 처리하지 않음
            item.setMine(false);
            editable = false;
        }

        item.setEditable(editable);
    }
}

