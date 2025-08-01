package org.maengle.board.services.configs;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.admin.board.controllers.RequestBoard;
import org.maengle.board.entities.Board;
import org.maengle.board.entities.QBoard;
import org.maengle.board.exceptions.BoardNotFoundException;
import org.maengle.board.repositories.BoardRepository;
import org.maengle.global.search.CommonSearch;
import org.maengle.global.search.ListData;
import org.maengle.global.search.Pagination;
import org.maengle.member.constants.Authority;
import org.maengle.member.libs.MemberUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardConfigInfoService {

    private final BoardRepository repository;
    private final ModelMapper mapper;
    private final HttpServletRequest request;
    private final MemberUtil memberUtil;


    // 게시판 설정 한 개 조회
    public Board get(String bid) {
        Board item = repository.findById(bid).orElseThrow(BoardNotFoundException::new);

        addInfo(item); // 추가 정보 공통 처리

        return item;
    }

    // 게시판 설정 수정시 필요한 커맨드 객체로 조회
    public RequestBoard getForm(String bid) {
        Board board = get(bid);

        return mapper.map(board, RequestBoard.class);
    }

    // 목록 조회임
    public ListData<Board> getList(CommonSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;

        String sopt = search.getSopt();
        String skey = search.getSkey();

        BooleanBuilder andBuilder = new BooleanBuilder();
        QBoard board = QBoard.board;

        // 키워드 검색 처리 S
        sopt = StringUtils.hasText(sopt) ? sopt.toUpperCase() : "ALL";
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            StringExpression fields = null;
            if (sopt.equals("BID")) {
                fields = board.bid;
            } else if (sopt.equals("NAME")) {
                fields = board.name;
            } else { // 통합 검색 BID + NAME
                fields = board.bid.concat(board.name);
            }

            andBuilder.and(fields.contains(skey));
        }
        // 키워드 검색 처리 E

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<Board> data = repository.findAll(andBuilder, pageable);
        List<Board> items = data.getContent();
        items.forEach(this::addInfo); // 추가정보 처리

        int total = (int)data.getTotalElements();
        Pagination pagination = new Pagination(page, total, 10, limit, request);

        return new ListData<>(items, pagination);

    }

    // 게시판 설정 추가 정보 가공 처리
    private void addInfo(Board item) {

        boolean writeable = true, listable = true, commentable = true;

        // 글쓰기 권한 처리
        Authority writeAuthority = item.getWriteAuthority();
        if (writeAuthority == Authority.MEMBER && !memberUtil.isLogin()) {
            writeable = false;
        } else if (writeAuthority == Authority.ADMIN && !memberUtil.isAdmin()) {
            writeable = false;
        }

        // 글목록 권한 처리
        Authority listAuthority = item.getListAuthority();
        if (listAuthority == Authority.MEMBER && !memberUtil.isLogin()) {
            listable = false;
        } else if (listAuthority == Authority.ADMIN && !memberUtil.isAdmin()) {
            listable = false;
        }

        // 댓글 권한 처리
        Authority commentAuthority = item.getCommentAuthority();
        if (commentAuthority == Authority.MEMBER && !memberUtil.isLogin()) {
            commentable = false;
        } else if (commentAuthority == Authority.ADMIN && !memberUtil.isAdmin()) {
            commentable = false;
        }

        item.setWriteable(writeable);
        item.setListable(listable);
        item.setCommentable(commentable);
    }
}
