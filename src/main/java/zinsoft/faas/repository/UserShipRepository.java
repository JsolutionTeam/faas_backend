package zinsoft.faas.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.UserShip;

public interface UserShipRepository extends JpaRepository<UserShip, Long>, UserShipQueryRepository {

    UserShip findByUserIdAndUserShipSeq(String userId, Long userShipSeq);

    UserShip findByUserInoutSeqAndStatusCd(Long userInoutSeq, String string);

    UserShip findByUserIdAndUserInoutSeqAndStatusCd(String userId, Long userInoutSeq, String string);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserShip a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId")
    void deleteByUserId(String userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserShip a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId AND a.userInoutSeq in :userInoutSeqs")
    void deleteByUserInoutSeq(String userId, Long[] userInoutSeqs);

}
