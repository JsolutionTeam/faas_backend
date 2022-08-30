package zinsoft.faas.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.UserProduction;

public interface UserProductionRepository extends JpaRepository<UserProduction, Long>, UserProductionQueryRepository {

    UserProduction findByUserIdAndUserProductionSeq(String userId, Long userProductionSeq);

    UserProduction findByUserIdAndUserDiarySeqAndStatusCd(String userId, Long userDiarySeq, String statusCd);

    UserProduction findByUserDiarySeqAndStatusCd(Long userDiarySeq, String statusCd);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserProduction a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId")
    void deleteByUserId(String userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserProduction a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId AND a.userDiarySeq in :userDiarySeqs")
    void deleteByUserDiarySeq(String userId, Long[] userDiarySeqs);

}
