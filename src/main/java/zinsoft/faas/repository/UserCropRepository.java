package zinsoft.faas.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.UserCrop;

public interface UserCropRepository extends JpaRepository<UserCrop, Long>, UserCropQueryRepository {

    UserCrop findByUserCropSeqAndUserIdAndStatusCd(Long userCropSeq, String userId, String statusCd);

    boolean existsByUserIdAndUserCropSeqAndStatusCd(String userId, Long userCropSeq,  String statusCd);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserCrop a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId")
    void deleteByUserId(String userId);
}
