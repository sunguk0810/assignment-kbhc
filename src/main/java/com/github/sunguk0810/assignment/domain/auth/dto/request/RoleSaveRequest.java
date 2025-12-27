package com.github.sunguk0810.assignment.domain.auth.dto.request;

import com.github.sunguk0810.assignment.domain.auth.constant.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
/**
 * 권한(Role) 정보를 등록하거나 수정할 때 사용하는 요청 DTO 클래스입니다.
 * <p>
 * 클라이언트로부터 권한의 유형, 표시 이름, 설명을 전달받아
 * </p>
 *
 * @see RoleType
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleSaveRequest {
    /**
     * 권한 유형 (Enum)
     * <p>
     * 필수 입력값입니다. (예: {@code ROLE_USER}, {@code ROLE_ADMIN})
     * </p>
     * @see RoleType
     */
    @NotNull(message = "권한 유형은 필수 입력값입니다.")
    private RoleType roleType;

    /**
     * 권한 표시 이름
     * <p>
     * 필수 입력값이며, 화면에 표시될 권한의 명칭입니다.
     * </p>
     */
    @NotBlank(message = "권한 명은 필수 입력값입니다.")
    private String roleName;

    /**
     * 권한에 대한 상세 설명
     * <p>
     * 선택 입력값입니다. 권한의 용도나 적용 범위를 기술합니다.
     * </p>
     */
    private String description;
}
