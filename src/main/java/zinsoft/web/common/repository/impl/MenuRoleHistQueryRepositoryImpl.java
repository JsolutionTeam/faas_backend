package zinsoft.web.common.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import lombok.RequiredArgsConstructor;
import zinsoft.web.common.repository.MenuRoleHistQueryRepository;

@RequiredArgsConstructor
public class MenuRoleHistQueryRepositoryImpl implements MenuRoleHistQueryRepository {

    //private final JPAQueryFactory query;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void insertByRoleId(String workerId, String workCd, String roleId) {
        /*
        final QMenuRoleHist menuRoleHist = QMenuRoleHist.menuRoleHist;
        final QMenuRole menuRole = QMenuRole.menuRole;

        INSERT INTO tb_menu_role_hist (worker_id,
                                       work_dtm,
                                       work_cd,
                                       menu_id,
                                       role_id,
                                       act_cd)
           SELECT #{workerId},
                  NOW(),
                  #{workCd},
                  menu_id,
                  role_id,
                  act_cd
             FROM tb_menu_role
            WHERE role_id = #{roleId}
        // @formatter:off
        query.insert(menuRoleHist)
                .columns(menuRoleHist.workerId,
                        menuRoleHist.workCd,
                        menuRoleHist.menuId,
                        menuRoleHist.roleId,
                        menuRoleHist.actCd)
                .select(JPAExpressions.select(
                            Expressions.constant(workerId), // Expressions.asString(workerId)
                            Expressions.constant(workCd),
                            menuRole.menuId,
                            menuRole.roleId,
                            menuRole.actCd)
                        .from(menuRole)
                        .where(menuRole.roleId.eq(roleId)))
                        .execute();
        // @formatter:on
        */

        Query q = entityManager.createQuery(""
                + "INSERT INTO MenuRoleHist ( "
                + "    workerId, "
                + "    workCd, "
                + "    menuId, "
                + "    roleId, "
                + "    actCd) "
                + "SELECT "
                + "    :workerId, "
                + "    :workCd, "
                + "    menuId, "
                + "    roleId, "
                + "    actCd "
                + "FROM MenuRole "
                + "WHERE roleId = :roleId");

        q.setParameter("workerId", workerId);
        q.setParameter("workCd", workCd);
        q.setParameter("roleId", roleId);

        q.executeUpdate();
    }

}
