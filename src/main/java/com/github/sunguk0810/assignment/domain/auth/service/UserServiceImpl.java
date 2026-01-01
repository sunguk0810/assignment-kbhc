package com.github.sunguk0810.assignment.domain.auth.service;

import com.github.sunguk0810.assignment.domain.auth.constant.RoleType;
import com.github.sunguk0810.assignment.domain.auth.dto.request.UserRegisterRequest;
import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.auth.entity.UserProfile;
import com.github.sunguk0810.assignment.domain.auth.repository.UserRepository;
import com.github.sunguk0810.assignment.global.constant.ErrorType;
import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 관리 및 회원가입 관련 비즈니스 로직을 처리하는 서비스 구현체입니다.
 * <p>
 * 회원가입 시 사용자 계정 생성, 비밀번호 암호화, 프로필 정보 저장 등의
 * 일련의 과정을 트랜잭션 단위로 수행합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 신규 사용자를 등록하고 초기 프로필 정보를 저장합니다.
     * <p>
     * 1. 이메일 중복 검사를 수행합니다.<br>
     * 2. 비밀번호를 암호화하여 사용자 기본 정보({@link User})를 저장합니다.<br>
     * 3. 입력된 상세 정보로 사용자 프로필({@link UserProfile})을 생성 및 연결합니다.
     * </p>
     *
     * @param request 회원가입 요청 데이터 (이메일, 비밀번호, 이름, 신체 정보 등)
     * @return 생성된 사용자의 고유 식별 키 (RecordKey)
     * @throws BusinessException {@link ErrorType#EMAIL_DUPLICATION} 이미 가입된 이메일일 경우 발생
     */
    @Transactional
    @Override
    public String register(UserRegisterRequest request) {
        String email = request.getEmail();
        String plainPassword = request.getPassword();
        String username = request.getUsername();
        UserRegisterRequest.Profile profiles = request.getProfiles();

        // 가입된 이메일이 있으면 오류 발생
        userRepository
                .findByEmail(email)
                .ifPresent(user -> {
                    throw new BusinessException(ErrorType.EMAIL_DUPLICATION);
                });

        String hashedPassword = passwordEncoder.encode(plainPassword);

        User user = User.builder()
                .email(email)
                .username(username)
                .hashedPassword(hashedPassword)
                .roleType(RoleType.ROLE_USER)
                .isActive(true)
                .build();

        userRepository.save(user);


        // 프로필 저장
        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .birthDate(profiles.getBirthDate())
                .gender(profiles.getGender())
                .nickname(profiles.getNickname())
                .mobileNo(profiles.getMobileNo())
                .weight(profiles.getWeight())
                .height(profiles.getHeight())
                .build();
        user.addProfile(userProfile);
        // 프로필 저장
        return user.getRecordKey();
    }


}
