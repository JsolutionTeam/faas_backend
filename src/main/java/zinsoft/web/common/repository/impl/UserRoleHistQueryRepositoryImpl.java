package zinsoft.web.common.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import lombok.RequiredArgsConstructor;
import zinsoft.web.common.repository.UserRoleHistQueryRepository;

@RequiredArgsConstructor
public class UserRoleHistQueryRepositoryImpl implements UserRoleHistQueryRepository {

    //private final JPAQueryFactory query;

    private final static String BASE_QUERY = ""
            + "INSERT INTO UserRoleHist ( "
            + "    workerId, "
            + "    workCd, "
            + "    userId, "
            + "    roleId) "
            + "SELECT "
            + "    :workerId, "
            + "    :workCd, "
            + "    userId, "
            + "    roleId "
            + "FROM UserRole "
            + "WHERE ";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void insertByUserId(String workerId, String workCd, String userId) {
        /*
        INSERT INTO tb_user_role_hist (worker_id,
                                       work_dtm,
                                       work_cd,
                                       user_id,
                                       role_id)
           SELECT #{workerId},
                  NOW(),
                  #{workCd},
                  user_id,
                  role_id
             FROM tb_user_role
            WHERE user_id = #{userId}
         */
        Query q = entityManager.createQuery(BASE_QUERY + "userId = :userId");

        q.setParameter("workerId", workerId);
        q.setParameter("workCd", workCd);
        q.setParameter("userId", userId);

        q.executeUpdate();
    }

    @Override
    public void insertByRoleId(String workerId, String workCd, String roleId) {
        Query q = entityManager.createQuery(BASE_QUERY + "roleId = :roleId");

        q.setParameter("workerId", workerId);
        q.setParameter("workCd", workCd);
        q.setParameter("roleId", roleId);

        q.executeUpdate();
    }

}
