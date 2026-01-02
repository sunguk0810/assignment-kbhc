package com.github.sunguk0810.assignment.domain.auth.dto.request;

import com.github.sunguk0810.assignment.global.constant.GenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 회원가입 요청 시 클라이언트로부터 전달받는 DTO 클래스입니다.
 * <p>
 * 계정 인증에 필요한 필수 정보(이메일, 비밀번호)와
 * 선택적인 프로필 상세 정보({@link Profile})를 포함합니다.
 * </p>
 *
 * @see Profile
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "사용자 회원가입 DTO")
public class UserRegisterRequest {
    /**
     * 사용자 이메일 (로그인 아이디)
     * <p>
     * 필수 입력값이며, 이메일 형식을 준수해야 합니다.
     * </p>
     */
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "올바른 이메일 형식이 아닙니다.")
    @Schema(description = "이메일", example = "admin@admin.com")
    private String email;

    /**
     * 사용자 실명
     * <p>
     * 필수 입력값이며, 최소 2글자 이상이어야 합니다.
     * </p>
     */
    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(min = 2, message = "이름은 2글자 이상이어야 합니다.")
    @Schema(description = "이름", example = "김이박")
    private String username;

    /**
     * 사용자 비밀번호 (Raw Data)
     * <p>
     * 8~20자 이내여야 하며, 영문/숫자/특수문자 중 2종류 이상을 조합해야 합니다.
     * </p>
     */
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?:(?=.*[a-zA-Z])(?=.*\\d)|(?=.*[a-zA-Z])(?=.*\\W)|(?=.*\\d)(?=.*\\W))[a-zA-Z\\d\\W]{8,20}$",
            message = "비밀번호는 8~20자의 영문, 숫자, 특수문자 중 2종류 이상을 조합해야 합니다.")
    @Schema(description = "비밀번호", example="abc123qwe!")
    private String password;

    /**
     * 사용자 프로필 상세 정보 객체
     * <p>
     * {@code @Valid} 어노테이션을 통해 중첩된 프로필 객체의 유효성도 함께 검증합니다.
     * </p>
     */
    @NotNull(message = "프로필 정보는 필수입니다.")
    @Valid
    @Schema(description = "프로필 정보")
    private Profile profiles;

    /**
     * 회원가입 시 함께 입력되는 프로필 상세 정보 클래스입니다.
     * <p>
     * 신체 정보, 연락처 등 부가적인 개인정보를 담고 있습니다.
     * </p>
     *
     * @see GenderType
     */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Profile {
        /**
         * 생년월일
         * <p>필수 입력 항목입니다.</p>
         */
        @NotNull(message = "생년월일은 필수 입력값입니다.")
        @Schema(description = "생년월일", example = "1990-01-01")
        private LocalDate birthDate;

        /**
         * 성별
         * <p>필수 입력 항목입니다.</p>
         * @see GenderType
         */
        @NotNull(message = "성별은 필수 입력값입니다.")
        @Schema(description = "성별", examples = {"MAN", "WOMAN"})
        private GenderType gender;

        /**
         * 닉네임 (별명)
         * <p>필수 입력 항목입니다.</p>
         */
        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        @Schema(description = "닉네임", example = "하늘을나는다람쥐")
        private String nickname;

        /**
         * 휴대전화 번호
         * <p>필수 입력 항목입니다.</p>
         */
        @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
        @Schema(description = "휴대폰 번호", example="010-1234-1234")
        private String mobileNo;

        /**
         * 신장 (키)
         * <p>단위: {@code cm}</p>
         */
        @Schema(description = "키 (cm)", example="184")
        private Double height;

        /**
         * 체중 (몸무게)
         * <p>단위: {@code kg}</p>
         */
        @Schema(description = "몸무게 (kg)", example="70.4")
        private Double weight;
    }
}
