package zinsoft.web.common.repository.impl;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.util.Constants;
import zinsoft.web.common.dto.MenuRoleDto;
import zinsoft.web.entity.QMenu;
import zinsoft.web.entity.QMenuRole;
import zinsoft.web.entity.QRole;
import zinsoft.web.common.repository.MenuRoleQueryRepository;

@RequiredArgsConstructor
public class MenuRoleQueryRepositoryImpl implements MenuRoleQueryRepository {

    private final JPAQueryFactory query;

    private QBean<MenuRoleDto> allFields = null;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QMenuRole.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QMenuRole.menuRole);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        allFields = Projections.fields(MenuRoleDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public List<MenuRoleDto> list(MenuRoleDto dto) {
        /*
        <bind name="SITE_CD" value="@zinsoft.web.common.vo.Constants@SITE_CD" />
        SELECT a.menu_id, a.role_id, a.act_cd
          FROM tb_menu_role a
               LEFT JOIN tb_role b
                      ON     a.role_id = b.role_id
                         AND b.status_cd = 'N'
         WHERE     a.menu_id IN (SELECT z.menu_id
                                   FROM tb_menu z
                                  WHERE z.site_cd = #{SITE_CD})
                <if test="menuId != null and menuId != ''">
                    AND a.menu_id = #{menuId}
                </if>
                <if test="roleId != null and roleId != ''">
                    AND a.role_id = #{roleId}
                </if>
                <if test="actCd != null and actCd != ''">
                    AND a.act_cd = #{actCd}
                </if>
        */
        final QMenuRole menuRole = QMenuRole.menuRole;
        final QMenu menu = QMenu.menu;
        final QRole role = QRole.role;
        BooleanExpression condition = menuRole.menuId.in(
                JPAExpressions.select(menu.menuId)
                        .from(menu)
                        .where(menu.statusCd.eq(Constants.STATUS_CD_NORMAL),
                                menu.siteCd.eq(Constants.SITE_CD)));

        if (StringUtils.isNotBlank(dto.getMenuId())) {
            condition.and(menuRole.menuId.eq(dto.getMenuId()));
        }

        if (StringUtils.isNotBlank(dto.getRoleId())) {
            condition.and(menuRole.roleId.eq(dto.getRoleId()));
        }

        if (StringUtils.isNotBlank(dto.getActCd())) {
            condition.and(menuRole.actCd.eq(dto.getActCd()));
        }

        // @formatter:off
        return query.select(allFields)
                .from(menuRole)
                    .leftJoin(role)
                        .on(role.roleId.eq(menuRole.roleId),
                            role.statusCd.eq(Constants.STATUS_CD_NORMAL))
                .where(condition)
                .fetch();
        // @formatter:on
    }

    @Override
    public void deleteByRoleId(String roleId) {
        final QMenuRole menuRole = QMenuRole.menuRole;

        query.delete(menuRole)
                .where(menuRole.roleId.eq(roleId))
                .execute();
    }

}
