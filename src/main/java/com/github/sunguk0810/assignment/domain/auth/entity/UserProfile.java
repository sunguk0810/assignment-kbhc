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
    @Id
    @Column(comment = "사용자 프로필 ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String profileId;

    @JoinColumn(nullable = false, comment = "사용자 구분키")
    private String recordKey;

    @Column(comment = "생년월일")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(comment = "성별")
    private GenderType gender;

    @Column(comment = "닉네임")
    private String nickname;

    @Column(comment = "휴대폰번호")
    private String mobileNo;

    @Column(comment = "키 (cm)")
    private Double height;

    @Column(comment = "몸무게 (kg)")
    private Double weight;

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
