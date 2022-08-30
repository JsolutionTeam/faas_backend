package zinsoft.faas.repository.impl;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.faas.dto.UserManureDto;
import zinsoft.faas.entity.QUserManure;
import zinsoft.faas.repository.UserManureQueryRepository;
import zinsoft.util.Constants;
import zinsoft.web.common.entity.QCode;
import zinsoft.web.common.entity.QUserInfo;

@RequiredArgsConstructor
public class UserManureQueryRepositoryImpl implements UserManureQueryRepository {

    private final JPAQueryFactory query;

    private QBean<UserManureDto> allFields = null;

    private final QUserManure userManure = QUserManure.userManure;
    private final QCode code = QCode.code;
    private final QUserInfo userInfo = QUserInfo.userInfo;

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserManure.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QUserManure.userManure);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        // @formatter:off
        fieldList.add(
                ExpressionUtils.as(
                    JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("PACK_T_CD"),
                               code.codeVal.eq(userManure.packTCd)),
                    "packTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                    JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("MANURE_T_CD"),
                               code.codeVal.eq(userManure.manureTCd)),
                    "manureTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                    JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("MANURE_T_CD2"),
                               code.codeVal.eq(userManure.manureTCd2)),
                    "manureTCdNm2"));

        fieldList.add(userInfo.userNm);
        // @formatterLon

        allFields = Projections.fields(UserManureDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public UserManureDto get(UserManureDto dto) {

        BooleanExpression condition = userManure.statusCd.eq(Constants.STATUS_CD_NORMAL);
                                        //.and(code.codeId.eq("CROP_S_CD"));

        //System.out.println(dto.toString());

        if (dto.getUserManureSeq() != null && dto.getUserManureSeq() > 0) {
            condition = condition.and(userManure.userManureSeq.eq(dto.getUserManureSeq()));
        }

        if (StringUtils.isNoneBlank(dto.getUserId())) {
            condition = condition.and(userManure.userId.eq(dto.getUserId()));
        }

        return query.select(allFields)
                .from(userManure)
                .join(userInfo)
                    .on(userManure.userId.eq(userInfo.userId))
                .where(condition)
                .fetchOne();
    }

    @Override
    public Page<UserManureDto> page(Map<String, Object> search, Pageable pageable) {

        JPQLQuery<UserManureDto> jpqQuery = query.select(allFields)
                                        .from(userManure)
                                        .join(userInfo)
                                            .on(userManure.userId.eq(userInfo.userId))
                                        .where(pageCondition(search));

        QueryResults<UserManureDto> result = null;

        if (search.get(pageSizeParameter) != null) {
            result = jpqQuery.orderBy(orderBy(search))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        } else {
            search.put("orderBy", "ASC");
            result = jpqQuery.orderBy(orderBy(search))
                    .fetchResults();
        }

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<UserManureDto> list(Map<String, Object> search) {

        JPQLQuery<UserManureDto> jpqQuery = query.select(allFields)
                                        .from(userManure)
                                        .join(userInfo)
                                            .on(userManure.userId.eq(userInfo.userId))
                                        .where(pageCondition(search))
                                        .orderBy(orderBy(search));

        List<UserManureDto> result = jpqQuery.fetch();

        return result;
    }

    private BooleanExpression pageCondition(Map<String, Object> search) {

        BooleanExpression condition = userManure.statusCd.eq(Constants.STATUS_CD_NORMAL);

        //System.out.println(search.toString());

        String userId = (String)search.get("userId");
        if(StringUtils.isNotBlank(userId)) {
            condition = condition.and(userManure.userId.eq(userId));
        }
        String keyword = (String)search.get("keyword");
        if(StringUtils.isNotBlank(keyword)) {
            switch ((String)search.get("field")) {
                case "userNm":
                    condition = condition.and(userInfo.userNm.contains(keyword));
                    break;
                case "userId":
                    condition = condition.and(userManure.userId.contains(keyword));
                    break;
                case "manureNm":
                    condition = condition.and(userManure.manureNm.contains(keyword));
                    break;
                case "makerNm":
                    condition = condition.and(userManure.makerNm.contains(keyword));
                    break;
            }
        }
        String admCd = (String)search.get("admCd");
        if(StringUtils.isNotBlank(admCd)) {
            condition = condition.and(userInfo.admCd.contains(admCd));
        }

        return condition;
    }

    private OrderSpecifier<?> orderBy(Map<String, Object> search){

        String orderBy = (String) search.get("orderBy");

        if(StringUtils.isNotBlank(orderBy)) {
            switch (orderBy) {
                case "ASC":
                    return userManure.userManureSeq.asc();

            }
        }

        return userManure.userManureSeq.desc();
    }
}
