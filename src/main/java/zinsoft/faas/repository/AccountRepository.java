package zinsoft.faas.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String>, AccountQueryRepository {

    Account findByAcId(String acId);

    @Query("SELECT MAX(acId) FROM Account WHERE statusCd='N' AND LOWER(TRIM(acNm)) = LOWER(TRIM(:acNm))")
    String findAcIdByAcNm(String acNm);

    @Query("SELECT IFNULL(MAX(exprSeq), 0)+1 FROM Account WHERE upAcId = :upAcId")
    Long nextExprSeqByUpAcId(String upAcId);

    @Query("SELECT a FROM Account as a WHERE a.statusCd='N' AND a.upAcId is null ORDER BY acId asc")
    List<Account> findAllByUpAcIdIsNullOrderByAcIdAsc();

    @Query("SELECT a FROM Account as a WHERE a.statusCd='N' AND a.upAcId = :upAcId ORDER BY acId asc")
    List<Account> findAllByUpAcIdOrderByAcIdAsc(String upAcId);

//    @Query("SELECT COUNT(*) FROM Account WHERE acId IN (SELECT IFNULL(MAX(acId), 0)+1 FROM Account WHERE upAcId = :upAcId)")
//    int countByUpAcId(String upAcId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.acId = :acId")
    void deleteByAcId(String acId);

/*
select
    MEM.EMPLYR_ID as id,
    FARM.FARM_CODE as farmCode
from
    sangju_mgr.COMTNEMPLYRINFO MEM
        LEFT JOIN sangju_mgr.EPIS_FARM_MANAGE_INFO_MEMBER FARM
                  ON MEM.EMPLYR_ID = FARM.EMPLYR_ID
where ;
 */

    @Query(value = "select\n" +
            "    FARM.FARM_CODE as farmCode\n" +
            "from\n" +
            "    sangju_mgr.COMTNEMPLYRINFO MEM\n" +
            "        LEFT JOIN sangju_mgr.EPIS_FARM_MANAGE_INFO_MEMBER FARM\n" +
            "                  ON MEM.EMPLYR_ID = FARM.EMPLYR_ID\n" +
            "where $1", nativeQuery = true)
    String getFarmCodeByAccountId(String id);

}
