package com.github.sunguk0810.assignment.domain.auth.service;

import com.github.sunguk0810.assignment.domain.auth.dto.request.UserRegisterRequest;
import com.github.sunguk0810.assignment.domain.auth.repository.UserRepository;
import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import com.github.sunguk0810.assignment.global.constant.ErrorType;
import com.github.sunguk0810.assignment.global.constant.GenderType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // 테스트용 정보 및 프로필 설정
    private static final String TS_USER_EMAIL = "user01@user.com";
    private static final String TS_USER_PASSWORD = "abc123qwe!";
    private static final String TS_USERNAME = "유저1";
    private static final String TS_USER_NICKNAME = "하늘을나는다람쥐";
    private static final LocalDate TS_USER_BIRTH_DATE = LocalDate.of(1990, 1, 1);
    private static final GenderType TS_USER_GENDER = GenderType.MAN;
    private static final String TS_USER_MOBILE_NO = "010-1234-1234";
    private static final Double TS_USER_HEIGHT = 190D;
    private static final Double TS_USER_WEIGHT = 90D;

    private static final UserRegisterRequest.Profile TS_USER_PROFILE;

    static {
        TS_USER_PROFILE = UserRegisterRequest.Profile.builder()
                .nickname(TS_USER_NICKNAME)
                .birthDate(TS_USER_BIRTH_DATE)
                .gender(TS_USER_GENDER)
                .mobileNo(TS_USER_MOBILE_NO)
                .height(TS_USER_HEIGHT)
                .weight(TS_USER_WEIGHT)
                .build();
    }


    @Test
    @DisplayName("[TS-USER-01] 회원가입 성공")
    public void userService_회원가입_성공(){
        // given
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email(TS_USER_EMAIL)
                .password(TS_USER_PASSWORD)
                .username(TS_USERNAME)
                .profiles(TS_USER_PROFILE)
                .build();

        // when
        String recordKey = userService.register(request);

        // then
        boolean isExist = userRepository.existsById(recordKey);
        assertTrue(isExist);

    }

    @Test
    @DisplayName("[TS-USER-02] 회원가입 실패 - 이메일 중복")
    public void userService_회원가입_실패(){
        // given (이미 존재하는 이메일로 테스트)
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("admin@admin.com")
                .password(TS_USER_PASSWORD)
                .username(TS_USERNAME)
                .profiles(TS_USER_PROFILE)
                .build();

        // when & then
        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorType.EMAIL_DUPLICATION.getMessage());

    }

}