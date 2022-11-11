package zinsoft.faas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zinsoft.web.common.entity.Code;

import java.util.List;
import java.util.Map;

public interface FaasDataRepository extends JpaRepository<Code, Long> {

    @Query(value = "WITH RECURSIVE DT_CNT AS(\n" +
            "    SELECT DATEDIFF(STR_TO_DATE(:endDt, '%Y%m%d'), STR_TO_DATE(:startDt, '%Y%m%d')) cnt\n" +
            "                        ), DT_TABLE AS (\n" +
            "                        SELECT :startDt as dt, 1 AS LEVEL\n" +
            "                        UNION ALL\n" +
            "                        SELECT DATE_FORMAT(DATE_ADD(STR_TO_DATE(:startDt, '%Y%m%d'), INTERVAL DT_TABLE.LEVEL DAY) , '%Y%m%d') dt,\n" +
            "                                                                1 + DT_TABLE.LEVEL AS LEVEL\n" +
            "                                                                FROM DT_CNT, DT_TABLE\n" +
            "                                                                WHERE DT_TABLE.LEVEL <= DT_CNT.cnt\n" +
            "            )\n" +
            "                                                    SELECT z.dt cal_dt,\n" +
            "                                                    a.cnt user_diary_cnt,\n" +
            "                                                    b.cnt user_inout_cnt,\n" +
            "                                                    c.cnt user_prd_cnt,\n" +
            "                                                    d.cnt user_ship_cnt\n" +
            "                                                    FROM DT_TABLE z\n" +
            "                                                    LEFT JOIN (  SELECT COUNT(z.user_diary_seq) cnt, z.act_dt day\n" +
            "                                                                 FROM tf_user_diary z\n" +
            "                                                                 WHERE z.status_cd = 'N'\n" +
            "                                                                   AND case when :userId is not null and :userId != '' then z.user_id = :userId else 1=1 end\n" +
            "                                                                 GROUP BY z.act_dt) a\n" +
            "                                                    ON z.dt = a.day\n" +
            "                                                    LEFT JOIN (  SELECT COUNT(z.user_inout_seq) cnt, z.trd_dt day\n" +
            "                                                                 FROM tf_user_inout z\n" +
            "                                                                 WHERE z.status_cd = 'N'\n" +
            "                                                                   AND case when :userId is not null and :userId != '' then z.user_id = :userId else 1=1 end\n" +
            "                                                                 GROUP BY z.trd_dt) b\n" +
            "                                                    ON z.dt = b.day\n" +
            "                                                    LEFT JOIN (  SELECT COUNT(z.user_production_seq) cnt, z.prd_dt day\n" +
            "                                                                 FROM tf_user_production z\n" +
            "                                                                 WHERE z.status_cd = 'N'\n" +
            "                                                                   AND case when :userId is not null and :userId != '' then z.user_id = :userId else 1=1 end\n" +
            "                                                                 GROUP BY z.prd_dt) c\n" +
            "                                                    ON z.dt = c.day\n" +
            "                                                    LEFT JOIN (  SELECT COUNT(z.user_ship_seq) cnt, z.ship_dt day\n" +
            "                                                                 FROM tf_user_ship z\n" +
            "                                                                 WHERE z.status_cd = 'N'\n" +
            "                                                                   AND case when :userId is not null and :userId != '' then z.user_id = :userId else 1=1 end\n" +
            "                                                                 GROUP BY z.ship_dt) d\n" +
            "                                                    ON z.dt = d.day\n" +
            "                                                    WHERE z.dt BETWEEN :startDt AND :endDt\n" +
            "                                                    ORDER BY z.dt", nativeQuery = true)
    List<Map<String, Object>> getCalendarData(String userId, String startDt, String endDt);
}
