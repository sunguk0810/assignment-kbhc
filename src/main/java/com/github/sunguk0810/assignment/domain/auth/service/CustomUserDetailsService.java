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
 * Spring Security의 {@link UserDetailsService}를 구현한 커스텀 인증 서비스 클래스입니다.
 * <p>
 * 사용자 로그인 시 입력받은 이메일을 기반으로 데이터베이스에서 사용자 정보를 조회하여
 * Spring Security 인증 프로세스에 필요한 {@link UserDetails} 구현체를 생성합니다.
 * </p>
 *
 * <h3>주요 기능</h3>
 * <ul>
 *     <li>이메일 기반 사용자 계정 조회</li>
 *     <li>조회된 엔티티를 {@link CustomUserDetails}로 래핑하여 반환</li>
 *     <li>미존재 사용자에 대한 예외 처리</li>
 * </ul>
 */
@Slf4j
@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 사용자 이름(이메일)을 기반으로 사용자 상세 정보를 로드합니다.
     * <p>
     * 데이터베이스의 {@code users} 테이블에서 해당 이메일을 사용하는 사용자를 검색하며,
     * 성공 시 인증 객체를 반환하고 실패 시 비즈니스 예외를 발생시킵니다.
     * </p>
     *
     * @param username 로그인 시 입력한 사용자 이메일
     * @return 인증된 사용자 정보를 담은 {@link CustomUserDetails} 객체
     * @throws BusinessException {@link ErrorType#USER_NOT_FOUND} 해당 이메일의 사용자가 존재하지 않을 경우
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(userRepository
                .findByEmail(username)
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND)));
    }

}
