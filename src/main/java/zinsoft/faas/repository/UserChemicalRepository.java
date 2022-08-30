package zinsoft.faas.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.UserChemical;

public interface UserChemicalRepository extends JpaRepository<UserChemical, Long>, UserChemicalQueryRepository {

    UserChemical findByUserIdAndUserChemicalSeq(String userId, Long userChemicalSeq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserChemical a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId")
    void deleteByUserId(String userId);


}
