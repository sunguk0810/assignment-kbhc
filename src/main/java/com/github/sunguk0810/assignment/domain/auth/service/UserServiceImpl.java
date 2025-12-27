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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        // 엔티티 생성

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
