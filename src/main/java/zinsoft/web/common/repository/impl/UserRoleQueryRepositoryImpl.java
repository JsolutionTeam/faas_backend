package zinsoft.web.common.repository.impl;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.util.Constants;
import zinsoft.web.common.dto.UserRoleDto;
import zinsoft.web.entity.QRole;
import zinsoft.web.entity.QUserRole;
import zinsoft.web.common.repository.UserRoleQueryRepository;

@RequiredArgsConstructor
public class UserRoleQueryRepositoryImpl implements UserRoleQueryRepository {

    private final JPAQueryFactory query;

    private QBean<UserRoleDto> allFields = null;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserRole.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QUserRole.userRole);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        final QRole role = QRole.role;
        fieldList.add(role.roleNm);

        allFields = Projections.fields(UserRoleDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public List<UserRoleDto> listByUserId(String userId) {
        /*
          SELECT a.user_id, a.role_id, b.role_nm
            FROM tb_user_role a
               LEFT JOIN tb_role b
                      ON     a.role_id = b.role_id
                         AND b.status_cd = 'N'
           WHERE a.user_id = #{userId}
        ORDER BY b.expr_seq
         */
        final QUserRole userRole = QUserRole.userRole;
        final QRole role = QRole.role;

        // @formatter:off
        return query.select(allFields)
                .from(userRole)
                    .leftJoin(role)
                        .on(role.roleId.eq(userRole.roleId),
                                role.statusCd.eq(Constants.STATUS_CD_NORMAL))
                 .where(userRole.userId.eq(userId))
                .orderBy(role.exprSeq.asc())
                .fetch();
        // @formatter:on
    }

    @Override
    public void deleteByUserId(String userId) {
        final QUserRole userRole = QUserRole.userRole;

        query.delete(userRole)
                .where(userRole.userId.eq(userId))
                .execute();
    }

    @Override
    public void deleteByRoleId(String roleId) {
        final QUserRole userRole = QUserRole.userRole;

        query.delete(userRole)
                .where(userRole.roleId.eq(roleId))
                .execute();
    }

}
