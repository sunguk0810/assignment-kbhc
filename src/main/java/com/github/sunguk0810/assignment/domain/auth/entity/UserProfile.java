package com.github.sunguk0810.assignment.domain.auth.entity;

import com.github.sunguk0810.assignment.global.constant.GenderType;
import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 사용자의 상세 프로필 정보를 관리하는 엔티티 클래스입니다.
 * <p>
 * 사용자의 신체 정보(키, 몸무게) 및 개인 식별 정보(생년월일, 연락처)를 포함합니다.
 * {@code recordKey}를 통해 {@link com.github.sunguk0810.assignment.domain.auth.entity.User} 엔티티와 논리적으로 연결됩니다.
 * </p>
 *
 * @see GenderType
 */
@Entity
@Table(name = "user_profiles",
        comment = "사용자 프로필 테이블",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"record_key"}, name = "UK_USER_PROFILE_RECORD_KEY")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile extends BaseEntity {
    /**
     * 프로필 고유 식별자 (PK)
     * <p>IDENTITY 전략을 사용하여 자동으로 생성됩니다.</p>
     */
    @Id
    @Column(comment = "사용자 프로필 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    /**
     * 사용자 식별 키 (FK 개념)
     * <p>
     * {@code User} 테이블의 식별자와 매핑됩니다.
     * 유니크 제약조건({@code UK_USER_PROFILE_RECORD_KEY})이 있어, 한 명의 사용자는 하나의 프로필만 가질 수 있습니다.
     * </p>
     */
    @JoinColumn(nullable = false, comment = "사용자 구분키")
    private String recordKey;

    /**
     * 생년월일
     */
    @Column(comment = "생년월일")
    private LocalDate birthDate;
    /**
     * 성별
     * @see GenderType
     */
    @Enumerated(EnumType.STRING)
    @Column(comment = "성별")
    private GenderType gender;
    /**
     * 닉네임 (별명)
     */
    @Column(comment = "닉네임")
    private String nickname;
    /**
     * 휴대폰 번호
     */
    @Column(comment = "휴대폰번호")
    private String mobileNo;

    /**
     * 신장 (키)
     * <p>단위: {@code cm}</p>
     */
    @Column(comment = "키 (cm)")
    private Double height;
    /**
     * 체중 (몸무게)
     * <p>단위: {@code kg}</p>
     */
    @Column(comment = "몸무게 (kg)")
    private Double weight;
    /**
     * UserProfile 생성자 (Builder 패턴)
     *
     * @param recordKey 사용자 식별 키
     * @param birthDate 생년월일
     * @param gender    성별 ({@link GenderType})
     * @param nickname  닉네임
     * @param mobileNo  휴대폰 번호
     * @param height    키 (cm)
     * @param weight    몸무게 (kg)
     */
    @Builder
    public UserProfile(String recordKey, LocalDate birthDate, GenderType gender, String nickname, String mobileNo, Double height, Double weight) {
        this.recordKey = recordKey;
        this.birthDate = birthDate;
        this.gender = gender;
        this.nickname = nickname;
        this.mobileNo = mobileNo;
        this.height = height;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(profileId, that.profileId) && Objects.equals(recordKey, that.recordKey) && Objects.equals(birthDate, that.birthDate) && gender == that.gender && Objects.equals(nickname, that.nickname) && Objects.equals(mobileNo, that.mobileNo) && Objects.equals(height, that.height) && Objects.equals(weight, that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId, recordKey, birthDate, gender, nickname, mobileNo, height, weight);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserProfile.class.getSimpleName() + "[", "]")
                .add("profileId='" + profileId + "'")
                .add("recordKey='" + recordKey + "'")
                .add("birthDate=" + birthDate)
                .add("gender=" + gender)
                .add("nickname='" + nickname + "'")
                .add("mobileNo='" + mobileNo + "'")
                .add("height=" + height)
                .add("weight=" + weight)
                .toString();
    }
}
