package com.github.sunguk0810.assignment.domain.health.repository;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.auth.repository.UserRepository;
import com.github.sunguk0810.assignment.domain.health.constant.MeasureType;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class HealthMeasureSummaryRepositoryTest {
    @Autowired
    private HealthMeasureSummaryRepository summaryRepository;

    @Autowired
    private UserRepository userRepository;

    // 테스트용 사용자 데이터
    private static final String TS_USER_RECORD_KEY = "7836887b-b12a-440f-af0f-851546504b13";
    private static final String TS_USER_RECORD_KEY1 = "7b012e6e-ba2b-49c7-bc2e-473b7b58e72e";


    @Test
    @DisplayName("[TS-REPO-01] 월간 요약 집계 (SUM)")
    public void summaryRepository_월간요약집계_SUM(){
        // given
        Optional<User> findUser = userRepository.findById(TS_USER_RECORD_KEY);
        User adminUser = findUser.get();

        MeasureType measureType = MeasureType.STEPS;

        HealthMeasureSummary testData1 = makeFakeData(adminUser, LocalDate.of(2025, 1, 1), measureType, 1L, 1000L);
        HealthMeasureSummary testData2 = makeFakeData(adminUser, LocalDate.of(2025, 1, 2), measureType, 1L, 2000L);
        HealthMeasureSummary testData3 = makeFakeData(adminUser, LocalDate.of(2025, 1, 3), measureType, 1L, 3000L);

        // when
        summaryRepository.saveAll(List.of(testData1, testData2, testData3));

        // then
        List<HealthMeasureSummary> summaryList = summaryRepository.findMonthlyUserSummary(adminUser, measureType, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 3));

        assertThat(summaryList).hasSize(1);
        HealthMeasureSummary janSummary = summaryList.get(0);
        assertThat(janSummary.getSumSteps()).isEqualTo(6000L);
        assertThat(janSummary.getCount()).isEqualTo(3L);
    }



    @Test
    @DisplayName("[TS-REPO-02] 월간 요약 집계 (AVG)")
    public void summaryRepository_월간요약_AVG(){
        // given
        Optional<User> findUser = userRepository.findById(TS_USER_RECORD_KEY);
        User adminUser = findUser.get();

        MeasureType measureType = MeasureType.BLOOD_PRESSURE;

        HealthMeasureSummary testData1 = makeFakeData(adminUser, LocalDate.of(2025, 2, 1), measureType, 1L, 120L);
        HealthMeasureSummary testData2 = makeFakeData(adminUser, LocalDate.of(2025, 2, 10), measureType, 1L, 130L);

        // when
        summaryRepository.saveAll(List.of(testData1, testData2));

        // then
        List<HealthMeasureSummary> summaryList = summaryRepository.findMonthlyUserSummary(adminUser, measureType, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 28));
        assertThat(summaryList).hasSize(1);
        HealthMeasureSummary febSummary = summaryList.get(0);
        assertThat(febSummary.getAvgSystolic()).isEqualTo(125L);


    }

    @Test
    @DisplayName("[TS-REPO-03] 기간 필터링")
    public void summaryRepository_기간필터링(){
        // given
        Optional<User> findUser = userRepository.findById(TS_USER_RECORD_KEY);
        User adminUser = findUser.get();

        MeasureType measureType = MeasureType.STEPS;

        HealthMeasureSummary testData1 = makeFakeData(adminUser, LocalDate.of(2025, 1, 31), measureType, 1L, 1000L);
        HealthMeasureSummary testData2 = makeFakeData(adminUser, LocalDate.of(2025, 2, 15), measureType, 1L, 2000L);
        HealthMeasureSummary testData3 = makeFakeData(adminUser, LocalDate.of(2025, 3, 1), measureType, 1L, 3000L);

        // when
        summaryRepository.saveAll(List.of(testData1, testData2, testData3));

        // then
        List<HealthMeasureSummary> summaryList = summaryRepository.findMonthlyUserSummary(adminUser, measureType, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 2, 28));

        assertThat(summaryList)
                .hasSize(2)
                .extracting(HealthMeasureSummary::getSummaryDate)
                .doesNotContain(LocalDate.of(2025, 3, 1));
    }

    @Test
    @DisplayName("[TS-REPO-04] 타 사용자 데이터 격리")
    public void summaryRepository_타사용자데이터격리(){
        // given
        Optional<User> findUser = userRepository.findById(TS_USER_RECORD_KEY);
        User adminUser = findUser.get();

        MeasureType measureType = MeasureType.STEPS;
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 30);

        // when
        List<HealthMeasureSummary> summaryList = summaryRepository.findMonthlyUserSummary(adminUser, measureType, startDate, endDate);

        // then
        assertThat(summaryList)
                .hasSize(1)
                .extracting(HealthMeasureSummary::getUser)
                .extracting(User::getRecordKey)
                .doesNotContain(TS_USER_RECORD_KEY1);
    }

    private static HealthMeasureSummary makeFakeData(User adminUser, LocalDate summaryDate, MeasureType measureType, Long count, Long result) {
        HealthMeasureSummary.HealthMeasureSummaryBuilder builder = HealthMeasureSummary.builder()
                .user(adminUser)
                .summaryDate(summaryDate)
                .measureType(measureType)
                .count(count);

        if (MeasureType.STEPS.equals(measureType)){
            builder.sumSteps(result);
        } else if (MeasureType.BLOOD_PRESSURE.equals(measureType)){
            builder.avgSystolic(result);
        }
        return builder.build();
    }



}