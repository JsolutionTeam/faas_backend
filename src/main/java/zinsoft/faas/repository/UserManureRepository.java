package zinsoft.faas.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.UserManure;

public interface UserManureRepository extends JpaRepository<UserManure, Long>, UserManureQueryRepository {

    UserManure findByUserIdAndUserManureSeq(String userId, Long userManureSeq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserManure a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId")
    void deleteByUserId(String userId);


}
