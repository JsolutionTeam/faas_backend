package zinsoft.web.common.repository.impl;

import java.util.List;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.util.Constants;
import zinsoft.web.common.dto.RestfulApiAccessDto;
import zinsoft.web.common.repository.MenuApiQueryRepository;
import zinsoft.web.entity.QMenu;
import zinsoft.web.entity.QMenuApi;
import zinsoft.web.entity.QMenuRole;

@RequiredArgsConstructor
public class MenuApiQueryRepositoryImpl implements MenuApiQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<RestfulApiAccessDto> listRestfulApiAccess() {
        final QMenuApi menuApi = QMenuApi.menuApi;
        final QMenuRole menuRole = QMenuRole.menuRole;
        final QMenu menu = QMenu.menu;

        /*
        SELECT b.path_pattern, b.method, c.role_id
          FROM tb_menu a, tb_menu_api b, tb_menu_role c
         WHERE     a.menu_id = b.menu_id
               AND b.menu_id = c.menu_id
               AND b.act_cd = c.act_cd
               AND a.status_cd = 'N'
         */
        // @formatter:off
        return query.selectFrom(menuApi).distinct()
                    .join(menuRole)
                        .on(menuRole.menuId.eq(menuApi.menuId),
                            menuRole.actCd.eq(menuApi.actCd))
                    .join(menu)
                        .on(menu.menuId.eq(menuApi.menuId),
                            menu.statusCd.eq(Constants.STATUS_CD_NORMAL))
                .transform(GroupBy.groupBy(menuApi.pathPattern, menuApi.method)
                        .list(Projections.fields(
                                RestfulApiAccessDto.class,
                                menuApi.pathPattern,
                                menuApi.method,
                                GroupBy.list(menuRole.roleId).as("roleIdList"))));
        // @formatter:on
    }

}
