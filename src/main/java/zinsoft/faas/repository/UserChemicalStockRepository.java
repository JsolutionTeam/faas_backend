package zinsoft.faas.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.UserChemicalStock;

public interface UserChemicalStockRepository extends JpaRepository<UserChemicalStock, Long>, UserChemicalStockQueryRepository {

    Long countByUserChemicalSeqAndStatusCd(Long userChemicalSeq, String statusCd);

    UserChemicalStock findByUserIdAndUserChemicalStockSeq(String userId, Long userChemicalStockSeq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserChemicalStock a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId AND a.userChemicalSeq=:userChemicalSeq")
    void deleteByUserChemicalSeq(String userId, Long userChemicalSeq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserChemicalStock a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId AND a.userDiarySeq in :userDiarySeqs")
    void deleteByUserDiarySeq(String userId, Long[] userDiarySeqs);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserChemicalStock a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId AND a.userInoutSeq in :userInoutSeqs")
    void deleteByUserInoutSeq(String userId, Long[] userInoutSeqs);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserChemicalStock a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId")
    void deleteByUserId(String userId);

}
