package zinsoft.faas.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.UserInout;

public interface UserInoutRepository extends JpaRepository<UserInout, Long>, UserInoutQueryRepository {

    UserInout findByUserIdAndUserInoutSeqAndStatusCd(String userId, Long userInoutSeq, String statusCd);

    int countByUserIdAndUserCropSeq(String userId, Long userCropSeq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserInout a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId AND a.userInoutSeq = :userInoutSeq")
    void delete(String userId, Long userInoutSeq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserInout a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId AND a.userInoutSeq in :userInoutSeqs")
    void delete(String userId, Long[] userInoutSeqs);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserInout a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId")
    void deleteByUserId(String userId);

}
