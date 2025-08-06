package org.maengle.member;

import lombok.Builder;
import lombok.Data;
import org.maengle.member.entities.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
public class MemberInfo implements UserDetails {

    private String userId;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    //계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        LocalDateTime expired = member.getExpired();

        return expired == null || expired.isAfter(LocalDateTime.now());
    }

    // 계정 정지 상태 ( true : 계정 정지 || false : 정상 사용 가능)
    @Override
    public boolean isAccountNonLocked() {
        return !member.isAccountLocked();
    }

    // pw 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        LocalDateTime date = member.getCredentialChangedAt();

        // 비밀 번호 만료 일수 : 6개월(180일)
        LocalDateTime date180 = LocalDateTime.now().minusDays(180L);

        // 현재 시간을 기점으로 비밀 번호가 변경된 뒤로 180일이 지났으면 비밀 번호 만료
        return date != null && date.isAfter(date180);
    }

    // 계정 탈퇴 여부 ( true : 계정 사용 중 || false : 계정 탈퇴)
    @Override
    public boolean isEnabled() {
        return member.getDeletedAt() == null;
    }
}
