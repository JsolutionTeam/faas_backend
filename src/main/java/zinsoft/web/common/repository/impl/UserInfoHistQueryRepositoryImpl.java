package zinsoft.web.common.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import lombok.RequiredArgsConstructor;
import zinsoft.web.common.repository.UserInfoHistQueryRepository;

@RequiredArgsConstructor
public class UserInfoHistQueryRepositoryImpl implements UserInfoHistQueryRepository {

    //private final JPAQueryFactory query;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void insertByUserId(String workerId, String workCd, String userId) {
        /*
        INSERT INTO tb_user_info_hist (worker_id,
                                       work_dtm,
                                       work_cd,
                                       user_id,
                                       reg_dtm,
                                       update_dtm,
                                       status_cd,
                                       user_pwd,
                                       user_nm,
                                       mobile_num,
                                       zipcode,
                                       addr1,
                                       addr2,
                                       email_addr,
                                       note)
           SELECT #{workerId},
                  NOW(),
                  #{workCd},
                  user_id,
                  reg_dtm,
                  update_dtm,
                  status_cd,
                  user_pwd,
                  user_nm,
                  mobile_num,
                  zipcode,
                  addr1,
                  addr2,
                  email_addr,
                  note
             FROM tb_user_info
            WHERE user_id = #{userId}
         */
        Query q = entityManager.createQuery(""
                + "INSERT INTO UserInfoHist ( "
                + "    workerId, "
                + "    workCd, "
                + "    userId, "
                + "    regDtm, "
                + "    updateDtm, "
                + "    statusCd, "
                + "    userPwd, "
                + "    userNm, "
                + "    mobileNum, "
                + "    zipcode, "
                + "    addr1, "
                + "    addr2, "
                + "    emailAddr, "
                + "    note) "
                + "SELECT "
                + "    :workerId, "
                + "    :workCd, "
                + "    userId, "
                + "    regDtm, "
                + "    updateDtm, "
                + "    statusCd, "
                + "    userPwd, "
                + "    userNm, "
                + "    mobileNum, "
                + "    zipcode, "
                + "    addr1, "
                + "    addr2, "
                + "    emailAddr, "
                + "    note "
                + "FROM UserInfo "
                + "WHERE userId = :userId");

        q.setParameter("workerId", workerId);
        q.setParameter("workCd", workCd);
        q.setParameter("userId", userId);

        q.executeUpdate();
    }

}
