package zinsoft.faas.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.UserManureStock;

public interface UserManureStockRepository extends JpaRepository<UserManureStock, Long>, UserManureStockQueryRepository {

    Long countByUserManureSeqAndStatusCd(Long userManureSeq, String statusCd);

    UserManureStock findByUserIdAndUserManureStockSeq(String userId, Long userManureStockSeq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserManureStock a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId AND a.userManureSeq=:userManureSeq")
    void deleteByUserManureSeq(String userId, Long userManureSeq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserManureStock a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId AND a.userDiarySeq in :userDiarySeqs")
    void deleteByUserDiarySeq(String userId, Long[] userDiarySeqs);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserManureStock a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId AND a.userInoutSeq in :userInoutSeqs")
    void deleteByUserInoutSeq(String userId, Long[] userInoutSeqs);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserManureStock a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId")
    void deleteByUserId(String userId);

}
