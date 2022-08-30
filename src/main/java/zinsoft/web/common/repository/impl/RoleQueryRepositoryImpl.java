package zinsoft.web.common.repository.impl;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.QueryResults;
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
import zinsoft.web.common.dto.RoleDto;
import zinsoft.web.common.entity.QRole;
import zinsoft.web.common.repository.RoleQueryRepository;

@RequiredArgsConstructor
public class RoleQueryRepositoryImpl implements RoleQueryRepository {

    private final JPAQueryFactory query;

    private QBean<RoleDto> allFields = null;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QRole.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QRole.role);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        allFields = Projections.fields(RoleDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public Page<RoleDto> page(Map<String, Object> search, Pageable pageable) {
        /*
          SELECT a.role_id,
                 a.reg_dtm,
                 a.update_dtm,
                 a.status_cd,
                 a.role_nm,
                 a.expr_seq
            FROM tb_role a
           WHERE     a.status_cd = 'N'
                <if test="search.keyword != null and search.keyword != ''">
                    <choose>
                        <when test="search.field == 'roleId'">
                            AND a.role_id LIKE CONCAT('%', #{search.keyword}, '%')
                        </when>
                        <when test="search.field == 'roleNm'">
                            AND a.role_nm LIKE CONCAT('%', #{search.keyword}, '%')
                        </when>
                    </choose>
                </if>
        ORDER BY a.expr_seq DESC
        <if test="numOfRows > 0">
            LIMIT #{start}, #{numOfRows}
        </if>
        */
        final QRole role = QRole.role;

        // @formatter:off
        QueryResults<RoleDto> result =
                query.select(allFields)
                    .from(role)
                    .where(condition(search, role))
                    .orderBy(role.exprSeq.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        // @formatter:on

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<RoleDto> list(Map<String, Object> search) {
        final QRole role = QRole.role;

        // @formatter:off
        return query.select(allFields)
                .from(role)
                .where(condition(search, role))
                .orderBy(role.exprSeq.asc())
                .fetch();
        // @formatter:on
    }

    private BooleanExpression condition(Map<String, Object> search, QRole role) {
        BooleanExpression condition = role.statusCd.eq(Constants.STATUS_CD_NORMAL);

        String keyword = (String) search.get("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            switch ((String) search.get("field")) {
                case "roleId":
                    condition = condition.and(role.roleId.contains(keyword));
                    break;
                case "roleNm":
                    condition = condition.and(role.roleNm.contains(keyword));
                    break;
            }
        }

        return condition;
    }
}
