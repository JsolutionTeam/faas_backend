package zinsoft.faas.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.UserDiary;

public interface UserDiaryRepository extends JpaRepository<UserDiary, Long>, UserDiaryQueryRepository {

    UserDiary findByUserIdAndUserDiarySeq(String userId, Long userDiarySeq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserDiary a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId AND a.userDiarySeq = :userDiarySeq")
    void delete(String userId, Long userDiarySeq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserDiary a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId AND a.userDiarySeq in :userDiarySeqs")
    void delete(String userId, Long[] userDiarySeqs);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserDiary a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.userId = :userId")
    void deleteByUserId(String userId);

}
