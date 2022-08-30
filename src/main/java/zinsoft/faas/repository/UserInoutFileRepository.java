package zinsoft.faas.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.UserInoutFile;
import zinsoft.faas.entity.UserInoutFileId;

public interface UserInoutFileRepository extends JpaRepository<UserInoutFile, UserInoutFileId>, UserInoutFileQueryRepository {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM UserInoutFile a WHERE     a.userInoutSeq = :userInoutSeq "
                                           + "AND a.fileSeq In (SELECT z.fileSeq "
                                                              + " FROM FileInfo z "
                                                              + "WHERE z.userId = :userId AND z.fileSeq IN :fileSeqs)")
    void deleteMultiple(String userId, Long userInoutSeq, List<Long> fileSeqs);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM UserInoutFile a WHERE a.userInoutSeq = (SELECT z.userInoutSeq "
                                                              + " FROM UserInout z "
                                                              + "WHERE z.userId = :userId AND z.userInoutSeq = :userInoutSeq)")
    void deleteByUserInoutSeq(String userId, Long userInoutSeq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM UserInoutFile a WHERE a.userInoutSeq IN (SELECT z.userInoutSeq "
                                                              + " FROM UserInout z "
                                                              + "WHERE z.userId = :userId)")
    void deleteByUserId(String userId);
}
