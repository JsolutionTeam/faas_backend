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
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.util.Constants;
import zinsoft.web.common.dto.MenuDto;
import zinsoft.web.common.dto.MenuRoleDto;
import zinsoft.web.common.entity.QMenu;
import zinsoft.web.common.entity.QMenuRole;
import zinsoft.web.common.repository.MenuQueryRepository;

@RequiredArgsConstructor
public class MenuQueryRepositoryImpl implements MenuQueryRepository {

    private final JPAQueryFactory query;

    private QBean<MenuDto> allFields = null;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QMenu.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QMenu.menu);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        allFields = Projections.fields(MenuDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public List<MenuDto> list(String siteCd, String roleId, String exprYn) {
        /*
          SELECT a.menu_id,
                 a.reg_dtm,
                 a.update_dtm,
                 a.status_cd,
                 a.site_cd,
                 a.sort_order,
                 a.depth,
                 a.menu_nm
                FROM tb_menu a, tb_menu_role b
               WHERE     a.menu_id = b.menu_id
                     AND a.status_cd = 'N'
                     AND a.site_cd = #{search.siteCd}
                        <if test="search.roleId != null and search.roleId != ''">
                            AND b.role_id = #{search.roleId}
                        </if>
                        <if test="search.exprYn != null and search.exprYn == 'Y'.toString()">
                            AND b.act_cd = 'E'
                        </if>
        ORDER BY a.site_cd, a.sort_order, a.depth, a.menu_nm
        */
        final QMenu menu = QMenu.menu;
        final QMenuRole menuRole = QMenuRole.menuRole;
        BooleanExpression condition = menu.statusCd.eq(Constants.STATUS_CD_NORMAL)
                .and(menu.siteCd.eq(siteCd));

        if (StringUtils.isNotBlank(roleId)) {
            condition = condition.and(menuRole.roleId.eq(roleId));
        }

        if (StringUtils.isNotBlank(exprYn) && Constants.YN_YES.equals(exprYn)) {
            condition = condition.and(menuRole.actCd.eq(MenuRoleDto.ACT_CD_EXPR));
        }

        // @formatter:off
        return query.select(allFields)
                .from(menu)
                    .join(menuRole)
                        .on(menu.menuId.eq(menuRole.menuId))
                .where(condition)
                .orderBy(menu.sortOrder.asc(),
                         menu.depth.asc())
                .fetch();
        // @formatter:on
    }

}
