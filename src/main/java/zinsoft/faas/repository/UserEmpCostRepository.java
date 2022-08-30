package zinsoft.faas.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.UserEmpCost;

public interface UserEmpCostRepository extends JpaRepository<UserEmpCost, Long>, UserEmpCostQueryRepository {

    UserEmpCost findByUserIdAndStatusCd(String userId, String statusCd);

    Long countByYearAndUserIdAndStatusCd(String year, String userId, String string);

    Long countByYearAndUserIdAndStatusCdAndUserEmpCostSeqNot(String year, String userId, String string, Long userEmpCostSeq);

    UserEmpCost findByUserEmpCostSeqAndStatusCd(Long userEmpCostSeq, String string);

    UserEmpCost findByUserIdAndYearAndStatusCd(String userId, String year, String string);

    UserEmpCost findByUserIdAndUserEmpCostSeq(String userId, Long userEmpCostSeq);

    @Override
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserEmpCost a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userEmpCostSeq = :userEmpCostSeq")
    void deleteById(Long userEmpCostSeq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserEmpCost a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId in :userId")
    void deleteByUserId(String userId);

}
