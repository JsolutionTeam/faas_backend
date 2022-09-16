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
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.util.Constants;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.entity.QRole;
import zinsoft.web.entity.QUserInfo;
import zinsoft.web.entity.QUserRole;
import zinsoft.web.common.repository.UserInfoQueryRepository;

@RequiredArgsConstructor
public class UserInfoQueryRepositoryImpl implements UserInfoQueryRepository {

    private final JPAQueryFactory query;

    private QBean<UserInfoDto> allFields = null;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserInfo.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QUserInfo.userInfo);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        allFields = Projections.fields(UserInfoDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public Page<UserInfoDto> page(Map<String, Object> search, Pageable pageable) {
        /*
          SELECT a.user_id,
                 a.reg_dtm,
                 a.update_dtm,
                 a.status_cd,
                 a.user_pwd,
                 a.last_login_dtm,
                 a.user_pwd_dtm,
                 a.user_pwd_noti_dtm,
                 a.user_nm,
                 a.mobile_num,
                 a.zipcode,
                 a.addr1,
                 a.addr2,
                 a.email_addr,
                 a.note
            FROM tb_user_info a
           WHERE     a.status_cd IN ('N', 'B', 'W')
                <if test="search.keyword != null and search.keyword != ''">
                    <choose>
                        <when test="search.field == 'userId'">
                            AND a.user_id LIKE CONCAT('%', #{search.keyword}, '%')
                        </when>
                        <when test="search.field == 'userNm'">
                            AND a.user_nm LIKE CONCAT('%', #{search.keyword}, '%')
                        </when>
                        <when test="search.field == 'mobileNum'">
                            AND a.mobile_num LIKE CONCAT('%', #{search.keyword}, '%')
                        </when>
                        <when test="search.field == 'emailAddr'">
                            AND a.email_addr LIKE CONCAT('%', #{search.keyword}, '%')
                        </when>
                        <when test="search.field == 'roleNm'">
                            AND a.user_id IN
                                   (SELECT z.user_id
                                      FROM tb_user_role z, tb_role y
                                     WHERE     z.role_id = y.role_id
                                           AND y.status_cd = 'N'
                                           AND y.role_nm LIKE CONCAT('%', #{search.keyword}, '%'))
                        </when>
                    </choose>
                </if>
                <if test="search.userId != null and search.userId != ''">
                    AND a.user_id = #{search.userId}
                </if>
                <if test="search.roleId != null and search.roleId != ''">
                    AND a.user_id IN
                            (SELECT z.user_id
                               FROM tb_user_role z
                              WHERE z.role_id = #{search.roleId})
                </if>
                <if test="search.roleIds != null">
                    AND a.user_id IN
                            (SELECT z.user_id
                               FROM tb_user_role z
                              WHERE z.role_id IN
                                    <foreach item="roleId" index="index" collection="search.roleIds" open="(" separator="," close=")">
                                        #{roleId}
                                    </foreach>)
                </if>
        ORDER BY a.reg_dtm DESC, a.user_id DESC
        <if test="numOfRows > 0">
            LIMIT #{start}, #{numOfRows}
        </if>
        */
        final QUserInfo userInfo = QUserInfo.userInfo;

        // @formatter:off
        QueryResults<UserInfoDto> result = query.select(allFields)
                .from(userInfo)
                .where(condition(search))
                .orderBy(userInfo.regDtm.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        // @formatter:on

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<UserInfoDto> list(Map<String, Object> search) {
        final QUserInfo userInfo = QUserInfo.userInfo;

        // @formatter:off
        return query.select(allFields)
                .from(userInfo)
                .where(condition(search))
                .orderBy(userInfo.regDtm.asc())
                .fetch();
        // @formatter:on
    }

    @Override
    public List<String> listUserId(Map<String, Object> search) {
        /*
          SELECT a.user_id
            FROM tb_user_info a
           WHERE     a.status_cd = 'N'
                <if test="search.userNm != null and search.userNm != ''">
                    AND a.user_nm = #{search.userNm}
                </if>
                <if test="search.mobileNum != null and search.mobileNum != ''">
                    AND REGEXP_REPLACE(a.mobile_num, '\\D', '') = REGEXP_REPLACE(#{search.mobileNum}, '\\D', '')
                </if>
        ORDER BY a.user_id
         */
        final QUserInfo userInfo = QUserInfo.userInfo;
        BooleanExpression condition = userInfo.statusCd.in(new String[] { UserInfoDto.STATUS_CD_NORMAL, UserInfoDto.STATUS_CD_BLOCK, UserInfoDto.STATUS_CD_WAITING });

        if (StringUtils.isNotBlank((String) search.get("userNm"))) {
            condition = condition.and(userInfo.userNm.eq((String) search.get("userNm")));
        }

        if (StringUtils.isNotBlank((String) search.get("mobileNum"))) {
            condition = condition.and(userInfo.mobileNum.eq(((String) search.get("mobileNum")).replaceAll("\\D", "")));
        }

        // @formatter:off
        return query.select(userInfo.userId)
                .from(userInfo)
                .where(condition)
                .orderBy(userInfo.userId.asc())
                .fetch();
        // @formatter:on
    }

    private BooleanExpression condition(Map<String, Object> search) {
        final QUserInfo userInfo = QUserInfo.userInfo;
        BooleanExpression condition = userInfo.statusCd.in(new String[] { UserInfoDto.STATUS_CD_NORMAL, UserInfoDto.STATUS_CD_BLOCK, UserInfoDto.STATUS_CD_WAITING });

        String keyword = (String) search.get("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            switch ((String) search.get("field")) {
                case "userId":
                    condition = condition.and(userInfo.userId.contains(keyword));
                    break;
                case "userNm":
                    condition = condition.and(userInfo.userNm.contains(keyword));
                    break;
                case "mobileNum":
                    condition = condition.and(userInfo.mobileNum.contains(keyword.replaceAll("\\D", "")));
                    break;
                case "emailAddr":
                    condition = condition.and(userInfo.emailAddr.contains(keyword));
                    break;
                case "roleNm":
                    final QUserRole userRole = QUserRole.userRole;
                    final QRole role = QRole.role;

                    // @formatter:off
                    condition = condition.and(userInfo.userId.in(
                            JPAExpressions.select(userRole.userId)
                                    .from(userRole)
                                        .join(role)
                                            .on(userRole.roleId.eq(role.roleId),
                                                role.statusCd.eq(Constants.STATUS_CD_NORMAL),
                                                role.roleNm.contains(keyword))));
                    // @formatter:on
                    break;
            }
        }

        if (StringUtils.isNotBlank((String) search.get("userId"))) {
            condition = condition.and(userInfo.userId.eq((String) search.get("userId")));
        }

        if (StringUtils.isNotBlank((String) search.get("roleId"))) {
            final QUserRole userRole = QUserRole.userRole;

            condition = condition.and(userInfo.userId.in(
                    JPAExpressions.select(userRole.userId)
                            .from(userRole)
                            .where(userRole.roleId.eq((String) search.get("roleId")))));
        }

        if (search.get("roleIds") != null) {
            final QUserRole userRole = QUserRole.userRole;

            condition = condition.and(userInfo.userId.in(
                    JPAExpressions.select(userRole.userId)
                            .from(userRole)
                            .where(userRole.roleId.in((String[]) search.get("roleIds")))));
        }

        return condition;
    }

}
