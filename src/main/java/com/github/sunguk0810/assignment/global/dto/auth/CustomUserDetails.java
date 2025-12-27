package com.github.sunguk0810.assignment.global.dto.auth;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Spring Security의 인증 객체(Principal)로 사용되는 어댑터 클래스입니다.
 * <p>
 * 도메인 엔티티인 {@link User}를 래핑(Wrapping)하여
 * Spring Security가 이해할 수 있는 {@link UserDetails} 인터페이스 형태로 변환합니다.
 * </p>
 *
 * @see UserDetails
 * @see User
 */
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class CustomUserDetails implements UserDetails {
    /**
     * 인증된 사용자의 실제 도메인 엔티티 정보
     */
    private User user;

    /**
     * User 엔티티를 기반으로 UserDetails 객체를 생성합니다.
     *
     * @param user 인증된 사용자 엔티티
     */
    public CustomUserDetails(User user) {
        this.user = user;
    }
    /**
     * 사용자가 보유한 권한(Authority) 목록을 반환합니다.
     * <p>
     * User 엔티티의 {@code RoleType}을 {@link SimpleGrantedAuthority}로 변환합니다.
     * (예: "ROLE_USER")
     * </p>
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();

        authorityList.add(new SimpleGrantedAuthority(user.getRoleType().name()));
        return authorityList;
    }

    @Override
    public @Nullable String getPassword() {
        return user.getHashedPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * 계정 잠김 여부를 반환합니다.
     * <p>
     * {@code user.isActive}가 false인 경우 잠긴 계정으로 취급합니다.
     * (비즈니스 로직에 따라 isEnabled와 구분하여 사용 가능)
     * </p>
     * @return true(잠기지 않음), false(잠김)
     */
    @Override
    public boolean isAccountNonLocked() {
        return user.getIsActive();
    }

    /**
     * 계정 만료 여부
     * <p>만료 기능이 없으므로 항상 true 반환</p>
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 비밀번호 만료 여부
     * <p>만료 기능이 없으므로 항상 true 반환</p>
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부
     * <p>
     * Spring Security의 기본 활성화 체크입니다.
     * 현재 로직상 isActive를 isAccountNonLocked에 매핑했으므로 여기는 true로 둡니다.
     * </p>
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
