package com.github.sunguk0810.assignment.domain.auth.service;

import com.github.sunguk0810.assignment.domain.auth.repository.UserRepository;
import com.github.sunguk0810.assignment.global.constant.ErrorType;
import com.github.sunguk0810.assignment.global.dto.auth.CustomUserDetails;
import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security의 {@link UserDetailsService}를 구현한 커스텀 인증 서비스입니다.
 * <p>
 * 로그인 시 전달된 사용자 이름(이메일)을 기반으로 DB에서 사용자 정보를 조회하고,
 * 인증 및 권한 부여를 위한 {@link UserDetails} 객체를 반환합니다.
 * </p>
 *
 * @see UserDetailsService
 * @see UserRepository
 */
@Slf4j
@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * 사용자 정보 조회를 위한 리포지토리
     */
    private final UserRepository userRepository;

    /**
     * Spring Security 인증 프로세스 중 사용자 정보를 로드하기 위해 호출되는 메서드입니다.
     * <p>
     * 이메일을 기준으로 사용자를 조회하며, 사용자가 존재하지 않을 경우 예외를 발생시킵니다.
     * </p>
     *
     * @param username 로그인 시 입력한 사용자 아이디 (이메일)
     * @return 인증된 사용자 정보를 담은 {@link UserDetails} 객체
     * @throws BusinessException {@code ErrorType.USER_NOT_FOUND} - 해당 이메일을 가진 사용자가 없을 경우
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = new CustomUserDetails(userRepository
                .findByEmail(username)
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND)));
        return userDetails;
    }

}
