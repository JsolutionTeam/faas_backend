package zinsoft.web.common.repository.impl;

import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.util.Constants;
import zinsoft.web.common.dto.UserAccessLogDto;
import zinsoft.web.entity.QUserInfo;
import zinsoft.web.entity.QUserRole;
import zinsoft.web.entity.QCode;
import zinsoft.web.entity.QUserAccessLog;
import zinsoft.web.common.repository.UserAccessLogQueryRepository;

@RequiredArgsConstructor
public class UserAccessLogQueryRepositoryImpl implements UserAccessLogQueryRepository {

    private final JPAQueryFactory query;

    private QBean<UserAccessLogDto> allFields = null;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserAccessLog.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QUserAccessLog.userAccessLog);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        final QUserInfo userInfo = QUserInfo.userInfo;
        fieldList.add(userInfo.userNm);

        final QUserAccessLog userAccessLog = QUserAccessLog.userAccessLog;
        final QCode code = QCode.code;

        // @formatter:off
        fieldList.add(
            ExpressionUtils.as(
                JPAExpressions.select(code.codeNm)
                    .from(code)
                    .where(code.codeId.eq("IN_OUT"),
                           code.codeVal.eq(userAccessLog.inOut)),
                "inOutNm"));
        // @formatterLon

        allFields = Projections.fields(UserAccessLogDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public Page<UserAccessLogDto> page(Map<String, Object> search, Pageable pageable) {
        /*
          SELECT a.access_dtm,
                 a.user_id,
                 a.in_out,
                 (SELECT z.code_nm FROM tb_code z WHERE z.code_id = 'IN_OUT' AND z.code_val = a.in_out )
                   in_out_nm,
                 a.success_yn,
                 a.remote_addr,
                 a.user_agent,
                 a.note,
                 b.user_nm
            FROM tb_user_access_log a
                 LEFT OUTER JOIN tb_user_info b
                              ON a.user_id = b.user_id
            <where>
                <if test="search.keyword != null and search.keyword != ''">
                    <choose>
                        <when test="search.field == 'userId'">
                            AND a.user_id LIKE CONCAT('%', #{search.keyword}, '%')
                        </when>
                        <when test="search.field == 'userNm'">
                            AND b.user_nm LIKE CONCAT('%', #{search.keyword}, '%')
                        </when>
                        <when test="search.field == 'remoteAddr'">
                            AND a.remote_addr LIKE CONCAT('%', #{search.keyword}, '%')
                        </when>
                    </choose>
                </if>
                <if test="search.stDt != null and search.stDt != '' and search.edDt != null and search.edDt != ''">
                    AND DATE_FORMAT(a.access_dtm, '%Y%m%d') BETWEEN #{search.stDt} AND #{search.edDt}
                </if>
                <if test="search.roleId != null and search.roleId != ''">
                    AND a.user_in IN (SELECT z.user_id
                                        FROM tb_user_role z
                                       WHERE z.role_id = #{search.roleId})
                </if>
            </where>
        ORDER BY a.access_dtm DESC
        <if test="numOfRows > 0">
            LIMIT #{start}, #{numOfRows}
        </if>
        */
        final QUserAccessLog userAccessLog = QUserAccessLog.userAccessLog;
        final QUserInfo userInfo = QUserInfo.userInfo;

        // @formatter:off
        QueryResults<UserAccessLogDto> result =
                query.select(allFields)
                    .from(userAccessLog)
                        .leftJoin(userInfo)
                            .on(userInfo.userId.eq(userAccessLog.userId))
                    .where(condition(search))
                    .orderBy(userAccessLog.userAccessLogSeq.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        // @formatter:on

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<UserAccessLogDto> list(Map<String, Object> search) {
        final QUserAccessLog userAccessLog = QUserAccessLog.userAccessLog;
        final QUserInfo userInfo = QUserInfo.userInfo;

        // @formatter:off
        return query.select(allFields)
                .from(userAccessLog)
                    .leftJoin(userInfo)
                        .on(userInfo.userId.eq(userAccessLog.userId))
                .where(condition(search))
                .orderBy(userAccessLog.userAccessLogSeq.asc())
                .fetch();
        // @formatter:on
    }

    private BooleanExpression condition(Map<String, Object> search) {
        final QUserAccessLog userAccessLog = QUserAccessLog.userAccessLog;
        final QUserInfo userInfo = QUserInfo.userInfo;
        BooleanExpression condition = Expressions.asBoolean(true).isTrue();

        String keyword = (String) search.get("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            switch ((String) search.get("field")) {
                case "userId":
                    condition = condition.and(userAccessLog.userId.contains(keyword));
                    break;
                case "userNm":
                    condition = condition.and(userInfo.userNm.contains(keyword));
                    break;
                case "remoteAddr":
                    condition = condition.and(userAccessLog.remoteAddr.contains(keyword));
                    break;
            }
        }

        String stDt = (String) search.get("stDt");
        String edDt = (String) search.get("edDt");

        if (StringUtils.isNotBlank(stDt) && StringUtils.isNotBlank(edDt)) {
            try {
                Date stDate = ((SimpleDateFormat) Constants.DATE_FORMATTER.clone()).parse(stDt);
                Date edDate = ((SimpleDateFormat) Constants.DATE_MS_FORMATTER.clone()).parse(edDt + Constants.TIME_MS_ED);
                //Expressions.dateTimeOperation(Date.class, Ops.DateTimeOps.DATE, userAccessLog.accessDtm)
                condition = condition.and(userAccessLog.accessDtm.between(stDate, edDate));
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }

        if (StringUtils.isNotBlank((String) search.get("roleId"))) {
            final QUserRole userRole = QUserRole.userRole;

            condition = condition.and(userAccessLog.userId.in(
                    JPAExpressions.select(userRole.userId)
                            .from(userRole)
                            .where(userRole.roleId.eq((String) search.get("roleId")))));
        }

        return condition;
    }

}
