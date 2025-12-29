package com.github.sunguk0810.assignment.domain.health.repository;


import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthMeasureInfoRepository extends JpaRepository<HealthMeasureInfo, Long> {
    @Modifying(clearAutomatically = true)
    @Query(value = """
            INSERT IGNORE INTO health_measure_infos( record_key
                                                   , measure_type
                                                   , from_date
                                                   , to_date
                                                   , measure_log_id
                                                   , measure_detail
                                                   , created_at
                                                   , updated_at
                                                   , created_by
                                                   , updated_by
            )
            VALUES (:#{#info.userInfo.recordKey}
                  , :#{#info.measureType.name()}
                  , :#{#info.period.from}
                  , :#{#info.period.to}
                  , :#{#info.measureLogId}
                  , :#{#detailJson}
                  , CURRENT_TIMESTAMP
                  , CURRENT_TIMESTAMP
                  , :#{#info.userInfo.recordKey}
                  , :#{#info.userInfo.recordKey}
            )
            """, nativeQuery = true)
    int saveInfo(@Param("info") HealthMeasureInfo info, @Param("detailJson") String detailJson);
}
