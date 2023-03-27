package zinsoft.faas.repository;


import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zinsoft.faas.entity.UserDiary;

import java.util.List;
import java.util.Map;

public interface FaasDataRepository extends JpaRepository<UserDiary, Long> {

    @Query(value = "WITH RECURSIVE DT_CNT AS (SELECT DATEDIFF(STR_TO_DATE(:endDt, '%Y%m%d'), STR_TO_DATE(:startDt, '%Y%m%d')) cnt\n" +
            "                                              ), DT_TABLE AS (SELECT :startDt as dt, 1 AS LEVEL\n" +
            "                                                              UNION ALL\n" +
            "                                                              SELECT DATE_FORMAT(DATE_ADD(STR_TO_DATE(:startDt, '%Y%m%d'), INTERVAL DT_TABLE.LEVEL DAY) , '%Y%m%d') dt,\n" +
            "                                                                                                      1 +\n" +
            "                                                                                                      DT_TABLE.LEVEL AS\n" +
            "                                                                                                      LEVEL\n" +
            "                                                                                                      FROM DT_CNT,\n" +
            "                                                                                                      DT_TABLE\n" +
            "                                                                                                      WHERE\n" +
            "                                                                                                      DT_TABLE.LEVEL <=\n" +
            "                                                                                                      DT_CNT.cnt\n" +
            "                                                                                              )\n" +
            "                                                                                          SELECT z.dt cal_dt,\n" +
            "                                                                                          a.cnt user_diary_cnt,\n" +
            "                                                                                          b.cnt user_inout_cnt,\n" +
            "                                                                                          c.cnt user_prd_cnt,\n" +
            "                                                                                          d.cnt user_ship_cnt\n" +
            "                                                                                          FROM DT_TABLE z\n" +
            "                                                                                          LEFT JOIN\n" +
            "                                                                                          (SELECT COUNT(z.user_diary_seq) cnt, z.act_dt day\n" +
            "                                                                                           FROM tf_user_diary z\n" +
            "                                                                                           WHERE z.status_cd = 'N'\n" +
            "                                                                                                     and if(:userId is not null, z.USER_ID = :userId, 1=1)\n" +
            "                                                                                           GROUP BY z.act_dt) a\n" +
            "                                                                                          ON z.dt = a.day\n" +
            "                                                                                          LEFT JOIN\n" +
            "                                                                                          (SELECT COUNT(z.user_inout_seq) cnt, z.trd_dt day\n" +
            "                                                                                           FROM tf_user_inout z\n" +
            "                                                                                           WHERE z.status_cd = 'N'\n" +
            "                                                                                             and if(:userId is not null, z.USER_ID = :userId, 1=1)\n" +
            "                                                                                           GROUP BY z.trd_dt) b\n" +
            "                                                                                          ON z.dt = b.day\n" +
            "                                                                                          LEFT JOIN\n" +
            "                                                                                          (SELECT COUNT(z.user_production_seq) cnt, z.prd_dt day\n" +
            "                                                                                           FROM tf_user_production z\n" +
            "                                                                                           WHERE z.status_cd = 'N'\n" +
            "                                                                                             and if(:userId is not null, z.USER_ID = :userId, 1=1)\n" +
            "                                                                                           GROUP BY z.prd_dt) c\n" +
            "                                                                                          ON z.dt = c.day\n" +
            "                                                                                          LEFT JOIN\n" +
            "                                                                                          (SELECT COUNT(z.user_ship_seq) cnt, z.ship_dt day\n" +
            "                                                                                           FROM tf_user_ship z\n" +
            "                                                                                           WHERE z.status_cd = 'N'\n" +
            "                                                                                             and if(:userId is not null, z.USER_ID = :userId, 1=1)\n" +
            "                                                                                           GROUP BY z.ship_dt) d\n" +
            "                                                                                          ON z.dt = d.day\n" +
            "                                                                                          WHERE\n" +
            "                                                                                          z.dt BETWEEN :startDt AND :endDt\n" +
            "                                                                                          ORDER BY z.dt", nativeQuery = true)
    List<Map<String, Object>> getCalendarData(@Param("userId") String userId, @Param("startDt") String startDt, @Param("endDt") String endDt);

}
