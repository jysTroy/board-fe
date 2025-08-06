package org.maengle.member.libs;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.member.MemberInfo;
import org.maengle.member.constants.Authority;
import org.maengle.member.entities.Member;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Lazy
@Component
@RequiredArgsConstructor
public class MemberUtil {
    private final HttpServletRequest request;

    public boolean isLogin() {
        return getMember() != null;
    }

    // admin 여부
    public boolean isAdmin() {
        return isLogin() && getMember().getAuthority() == Authority.ADMIN;
    }

    // 로그인한 회원 정보
    public Member getMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof MemberInfo memberInfo) {
            return memberInfo.getMember();
        }

        return null;
    }

    // 회원 : IP + User-Agent + 회원번호
    public int getUserHash() {
        if (!isLogin()) {
            return -1;
        }
        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");

        return Objects.hash(ip, ua, getMember().getUserUuid());
    }
}
