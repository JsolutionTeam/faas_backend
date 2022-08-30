package zinsoft.faas.repository.impl;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import com.querydsl.core.Tuple;
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
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.faas.dto.UserProductionDto;
import zinsoft.faas.entity.QUserProduction;
import zinsoft.faas.repository.UserProductionQueryRepository;
import zinsoft.util.Constants;
import zinsoft.web.common.entity.QCode;
import zinsoft.web.common.entity.QUserInfo;

@RequiredArgsConstructor
public class UserProductionQueryRepositoryImpl implements UserProductionQueryRepository {

    private final JPAQueryFactory query;

    private QBean<UserProductionDto> allFields = null;

    private final QUserProduction userProduction = QUserProduction.userProduction;
    private final QCode code = QCode.code;
    private final QUserInfo userInfo = QUserInfo.userInfo;

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserProduction.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QUserProduction.userProduction);
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
                               code.codeVal.eq(userProduction.packTCd)),
                    "packTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                    JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("PLAN_T_CD"),
                               code.codeVal.eq(userProduction.planTCd)),
                    "planTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                    JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("GRADE_T_CD"),
                               code.codeVal.eq(userProduction.gradeTCd)),
                    "gradeTCdNm"));
        fieldList.add(userInfo.userNm);
        // @formatterLon

        allFields = Projections.fields(UserProductionDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public UserProductionDto get(Long userProductionSeq, String userId) {

        BooleanExpression condition = userProduction.statusCd.eq(Constants.STATUS_CD_NORMAL);
                                        //.and(code.codeId.eq("CROP_S_CD"));

        //System.out.println(dto.toString());

        if (userProductionSeq != null && userProductionSeq > 0) {
            condition = condition.and(userProduction.userProductionSeq.eq(userProductionSeq));
        }

        if (StringUtils.isNoneBlank(userId)) {
            condition = condition.and(userProduction.userId.eq(userId));
        }

        return query.select(allFields)
                .from(userProduction)
                .join(userInfo)
                    .on(userProduction.userId.eq(userInfo.userId))
                .where(condition)
                .fetchOne();
    }

    @Override
    public Page<UserProductionDto> page(Map<String, Object> search, Pageable pageable) {

        JPQLQuery<UserProductionDto> jpqQuery = query.select(allFields)
                                        .from(userProduction)
                                        .join(userInfo)
                                            .on(userProduction.userId.eq(userInfo.userId))
                                        .where(pageCondition(search));

        QueryResults<UserProductionDto> result = null;

        if (search.get(pageSizeParameter) != null) {
            result = jpqQuery.orderBy(orderBy(search).stream().toArray(OrderSpecifier[]::new))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        } else {
            result = jpqQuery.orderBy(orderBy(search).stream().toArray(OrderSpecifier[]::new))
                    .fetchResults();
        }

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }



    @Override
    public List<UserProductionDto> totalByPlanTCd(Map<String, Object> search){
        BooleanExpression condition = code.codeVal.eq(userProduction.planTCd)
                                    .and(userProduction.statusCd.eq(Constants.STATUS_CD_NORMAL));

        String userId = (String)search.get("userId");
        if(StringUtils.isNotBlank(userId)) {
            condition = condition.and(userProduction.userId.eq(userId));
        }

        String stDt = (String)search.get("stDt");
        String edDt = (String)search.get("edDt");
        if(StringUtils.isNotBlank(stDt) && StringUtils.isNotBlank(edDt)) {
            stDt = stDt.replace("-", "");
            edDt = edDt.replace("-", "");
            condition = condition.and(userProduction.prdDt.between(stDt, edDt));
        }

        return query.select( Projections.fields(UserProductionDto.class,
                             code.codeVal.as("planTCd"),
                             code.codeNm.concat("합계").as("planTCdNm"),
                             userProduction.quan.sum().coalesce(new Double(0)).as("quan"),
                             ExpressionUtils.as(
                                     JPAExpressions.select(code.codeNm)
                                         .from(code)
                                         .where(code.codeId.eq("PACK_T_CD"),
                                                code.codeVal.eq(userProduction.packTCd.coalesce("1"))),
                                     "packTCdNm")
                            ))
                .from(code)
                .leftJoin(userProduction).on(condition)
                .where(code.codeId.eq("PLAN_T_CD"))
                .groupBy(code.codeVal)
                .orderBy(code.codeVal.desc())
                .fetch();
    }

    @Override
    public List<Map<String, Object>> chartByPlanTCd(Map<String, Object> search){

        BooleanExpression condition = userProduction.statusCd.eq(Constants.STATUS_CD_NORMAL);

        String userId = (String)search.get("userId");
        if(StringUtils.isNotBlank(userId)) {
            condition = condition.and(userProduction.userId.eq(userId));
        }

        String dt = (String)search.get("dt");
        if(StringUtils.isNotBlank(dt)) {
            dt = dt.replace("-", "");
            condition = condition.and(userProduction.prdDt.startsWith(dt));
        }

        JPAQuery<Tuple> jpaResult = query.select( userProduction.prdDt.substring(0, 6).as("dt"),
                                                userProduction.planTCd.when(UserProductionDto.PLAN_T_CD_BUDGET)
                                                    .then(userProduction.quan)
                                                    .otherwise(new Double(0))
                                                    .sum().as("sum1"),
                                                userProduction.planTCd.when(UserProductionDto.PLAN_T_CD_ACTUAL)
                                                    .then(userProduction.quan)
                                                    .otherwise(new Double(0))
                                                    .sum().as("sum2")
                                                )
                                               .from(userProduction)
                                               .where(condition);


        String yearYn = (String) search.get("yearYn");
        if(StringUtils.isNoneBlank(yearYn)) {
            jpaResult = jpaResult.groupBy(userProduction.prdDt.substring(0,6));
        }
        List<Tuple> result = jpaResult.orderBy(userProduction.prdDt.asc()).fetch();

        return  result.stream()
                .map(t -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("dt", t.get(0, String.class));
                    map.put("sum1", t.get(1, Double.class));
                    map.put("sum2", t.get(2, Double.class));
                    return map;
                })
                .collect(Collectors.toList());
    }

    private BooleanExpression pageCondition(Map<String, Object> search) {

        BooleanExpression condition = userProduction.statusCd.eq(Constants.STATUS_CD_NORMAL);

        //System.out.println(search.toString());

        String userId = (String)search.get("userId");
        if(StringUtils.isNotBlank(userId)) {
            condition = condition.and(userProduction.userId.eq(userId));
        }

        String planTCd = (String)search.get("planTCd");
        if(StringUtils.isNotBlank(planTCd)) {
            condition = condition.and(userProduction.planTCd.eq(planTCd));
        }

        String stDt = (String)search.get("stDt");
        String edDt = (String)search.get("edDt");
        if(StringUtils.isNotBlank(stDt) && StringUtils.isNotBlank(edDt)) {
            stDt = stDt.replace("-", "");
            edDt = edDt.replace("-", "");
            condition = condition.and(userProduction.prdDt.between(stDt, edDt));
        }

        String prdDt = (String)search.get("prdDt");
        if(StringUtils.isNotBlank(prdDt)) {
            prdDt = prdDt.replace("-", "");
            condition = condition.and(userProduction.prdDt.eq(prdDt));
        }

        String admCd = (String)search.get("admCd");
        if(StringUtils.isNotBlank(admCd)) {
            condition = condition.and(userInfo.admCd.contains(admCd));
        }

        return condition;
    }

    private List<OrderSpecifier<?>> orderBy(Map<String, Object> search) {
        String orderBy = (String) search.get("orderBy");
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (StringUtils.isNotBlank(orderBy)) {
            switch (orderBy) {
                case "ASC":
                    orders.add(userProduction.prdDt.asc());
                    return orders;
            }
            orders.add(userProduction.userProductionSeq.asc());
        } else {
            orders.add(userProduction.prdDt.desc());
            orders.add(userProduction.userProductionSeq.desc());
        }
        return orders;
    }
}
