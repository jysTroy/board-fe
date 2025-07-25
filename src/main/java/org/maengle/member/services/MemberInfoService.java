package org.maengle.member.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.global.search.ListData;
import org.maengle.global.search.Pagination;
import org.maengle.file.services.FileInfoService;
import org.maengle.member.MemberInfo;
import org.maengle.member.constants.Authority;
import org.maengle.member.controllers.MemberSearch;
import org.maengle.member.entities.Member;
import org.maengle.member.entities.QMember;
import org.maengle.member.repositories.MemberRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Order.desc;

@Lazy
@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository repository;
    private final HttpServletRequest request;
    private final FileInfoService fileInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //QMember _member = QMember.member;
        Member member = repository.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException(username));

        Authority authority = Objects.requireNonNullElse(member.getAuthority(), Authority.MEMBER);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(authority.name()));

        addInfo(member); // 회원 정보 정보 처리

        return MemberInfo.builder()
                .userId(member.getUserId())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();
    }

    // 회원 목록
    public ListData<Member> getList(MemberSearch search) {
        int page = Math.max(search.getPage(), 1); // 최소 페이지
        int limit = search.getLimit(); // 페이지당 항목 수
        limit = limit < 1 ? 20 : limit; // 기본값 20넣었음

        // 쿼리DSL쓸려고 준비한거임
        QMember member = QMember.member;
        BooleanBuilder andBuilder = new BooleanBuilder();

        String sopt = search.getSopt(); // 검색 옵션
        String skey = search.getSkey(); // 검색 키워드

        // sopt 없으면 ALL처리 했음
        sopt = StringUtils.hasText(sopt) ? sopt.toUpperCase() : "ALL";

        // 키워드 검색
        // 쿼리DSL 넣어서 이렇게 만드는거임
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();
            StringExpression fields = null;
            if (sopt.equals("NAME")) {
                fields = member.name;
            } else if (sopt.equals("EMAIL")) {
                fields = member.email;
            } else if (sopt.equals("MOBILE")) {
                fields = member.mobile;
            } else if (sopt.equals("ID")) {
                fields = member.userId;
            } else {
                fields = member.name.concat(member.email)
                        .concat(member.mobile).concat(member.userId);
            }
            andBuilder.and(fields.contains(skey));
        }

        // 권한 조건 넣은거임
        // authorities가 비어있으면 andbuilder부분을 띄어넘기때문에
        // 널값이 아닐 때를 같이 집어넣었음(널과 빈 값은 다른거임)
        List<Authority> authorities = search.getAuthorities();
        if (authorities != null && !authorities.isEmpty()) {
            andBuilder.and(member.authority.in(authorities));
        }

        // 페이징 처리
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<Member> data = repository.findAll(andBuilder, pageable);
        List<Member> items = data.getContent();
        long total = data.getTotalElements();
        Pagination pagination = new Pagination(page, (int)total, 10, limit, request);

        return new ListData<>(items, pagination);
    }

    /**
     * 회원 정보 추가 처리
     * @param member
     */
    private void addInfo(Member member) {
        member.setProfileImage(fileInfoService.get(member.getGid()));
    }
}