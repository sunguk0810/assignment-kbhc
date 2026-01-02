package com.github.sunguk0810.assignment.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * 사용자 로그인 요청 데이터를 처리하기 위한 DTO 클래스입니다.
 *
 * 클라이언트로부터 이메일과 비밀번호를 전달받아 인증을 수행하는 데 사용됩니다.
 * 유효성 검증을 통해 이메일 형식과 비밀번호 규칙을 확인합니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Schema(description = "사용자 로그인 요청 DTO")
public class UserLoginRequest {
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
     * 사용자 비밀번호 (Raw Data)
     * <p>
     * 8~20자 이내여야 하며, 영문/숫자/특수문자 중 2종류 이상을 조합해야 합니다.
     * </p>
     */
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?:(?=.*[a-zA-Z])(?=.*\\d)|(?=.*[a-zA-Z])(?=.*\\W)|(?=.*\\d)(?=.*\\W))[a-zA-Z\\d\\W]{8,20}$",
            message = "비밀번호는 8~20자의 영문, 숫자, 특수문자 중 2종류 이상을 조합해야 합니다.")
    @Schema(description = "비밀번호", example = "abc123qwe!")
    private String password;
}
