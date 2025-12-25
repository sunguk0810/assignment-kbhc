package com.github.sunguk0810.assignment.domain.auth.entity;

import com.github.sunguk0810.assignment.domain.company.entity.Company;
import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

//https://stir.tistory.com/158
@Entity
@Table(name = "user",
        comment = "사용자 테이블",
        uniqueConstraints = {
         @UniqueConstraint(columnNames = {"email"}, name = "UK_USER_EMAIL"),
         @UniqueConstraint(columnNames = {"company_id"}, name = "UK_USER_COMPANY_ID")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @Column(comment = "사용자 구분키")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String recordKey;

    @Column(comment = "이메일", nullable = false)
    private String email;

    @Column(comment = "이름", length = 100, nullable = false)
    private String username;

    @Column(comment = "비밀번호", nullable = false)
    private String hashedPassword;

    @OneToOne
    @JoinColumn(name="company_id", nullable = false)
    private Company company;

    @Column(comment = "활성화여부", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;

    @Column(comment = "마지막로그인일자")
    private LocalDateTime lastLoginAt;

    @Builder
    public User(String email, String username, String hashedPassword, Company company, Boolean isActive) {
        this.email = email;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.company = company;
        this.isActive = isActive;
        this.lastLoginAt = LocalDateTime.now();
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(recordKey, user.recordKey) && Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(hashedPassword, user.hashedPassword) && Objects.equals(company, user.company) && Objects.equals(isActive, user.isActive) && Objects.equals(lastLoginAt, user.lastLoginAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordKey, email, username, hashedPassword, company, isActive, lastLoginAt);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("recordKey='" + recordKey + "'")
                .add("email='" + email + "'")
                .add("username='" + username + "'")
                .add("hashedPassword='" + hashedPassword + "'")
                .add("company=" + company)
                .add("isActive=" + isActive)
                .add("lastLoginAt=" + lastLoginAt)
                .toString();
    }

}
