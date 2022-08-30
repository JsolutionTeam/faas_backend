package zinsoft.faas.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.UserDiaryFile;
import zinsoft.faas.entity.UserDiaryFileId;

public interface UserDiaryFileRepository extends JpaRepository<UserDiaryFile, UserDiaryFileId>, UserDiaryFileQueryRepository {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM UserDiaryFile a WHERE     a.userDiarySeq = :userDiarySeq "
                                           + "AND a.fileSeq in (SELECT z.fileSeq "
                                                              + " FROM FileInfo z "
                                                              + "WHERE z.userId = :userId AND z.fileSeq IN :fileSeqs)")
    void deleteMultiple(String userId, Long userDiarySeq, List<Long> fileSeqs);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM UserDiaryFile a WHERE a.userDiarySeq = (SELECT z.userDiarySeq "
                                                              + " FROM UserDiary z "
                                                              + "WHERE z.userId = :userId AND z.userDiarySeq = :userDiarySeq)")
    void deleteByUserDiarySeq(String userId, Long userDiarySeq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM UserDiaryFile a WHERE a.userDiarySeq IN (SELECT z.userDiarySeq "
                                                              + " FROM UserDiary z "
                                                              + "WHERE z.userId = :userId)")
    void deleteByUserId(String userId);
}
