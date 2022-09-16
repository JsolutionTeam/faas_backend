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
import zinsoft.faas.dto.UserShipDto;
import zinsoft.faas.entity.QUserInout;
import zinsoft.faas.entity.QUserShip;
import zinsoft.faas.repository.UserShipQueryRepository;
import zinsoft.util.Constants;
import zinsoft.web.entity.QCode;
import zinsoft.web.entity.QUserInfo;

@RequiredArgsConstructor
public class UserShipQueryRepositoryImpl implements UserShipQueryRepository  {

    private final JPAQueryFactory query;

    private QBean<UserShipDto> allFields = null;

    private final QUserShip userShip = QUserShip.userShip;
    private final QCode code = QCode.code;
    private final QUserInfo userInfo = QUserInfo.userInfo;
    private final QUserInout userInout = QUserInout.userInout;

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserShip.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QUserShip.userShip);
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
                               code.codeVal.eq(userShip.packTCd)),
                    "packTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                    JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("PLAN_T_CD"),
                               code.codeVal.eq(userShip.planTCd)),
                    "planTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                    JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("GRADE_T_CD"),
                               code.codeVal.eq(userShip.gradeTCd)),
                    "gradeTCdNm"));
        fieldList.add(userInfo.userNm);
        fieldList.add(userInout.inoutTCd);
        fieldList.add(
                ExpressionUtils.as(
                    JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("INOUT_T_CD"),
                               code.codeVal.eq(userInout.inoutTCd)),
                    "inoutTCdNm"));
        // @formatterLon

        allFields = Projections.fields(UserShipDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public UserShipDto get(Long userShipSeq, String userId) {

        BooleanExpression condition = userShip.statusCd.eq(Constants.STATUS_CD_NORMAL);
                                        //.and(code.codeId.eq("CROP_S_CD"));

        //System.out.println(dto.toString());

        if (userShipSeq != null && userShipSeq > 0) {
            condition = condition.and(userShip.userShipSeq.eq(userShipSeq));
        }

        if (StringUtils.isNoneBlank(userId)) {
            condition = condition.and(userShip.userId.eq(userId));
        }

        return query.select(allFields)
                .from(userShip)
                .join(userInfo)
                    .on(userShip.userId.eq(userInfo.userId))
                .leftJoin(userInout)
                	.on(userShip.userInoutSeq.eq(userInout.userInoutSeq))
                .where(condition)
                .fetchOne();
    }

    @Override
    public Page<UserShipDto> page(Map<String, Object> search, Pageable pageable) {

        JPQLQuery<UserShipDto> jpqQuery = query.select(allFields)
                                        .from(userShip)
                                        .join(userInfo)
                                            .on(userShip.userId.eq(userInfo.userId))
                                        .leftJoin(userInout)
                                            .on(userShip.userInoutSeq.eq(userInout.userInoutSeq))
                                        .where(pageCondition(search));

        QueryResults<UserShipDto> result = null;

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
    public List<UserShipDto> totalByPlanTCd(Map<String, Object> search){

        BooleanExpression condition = code.codeVal.eq(userShip.planTCd)
                .and(userShip.statusCd.eq(Constants.STATUS_CD_NORMAL));

        String userId = (String)search.get("userId");
        if(StringUtils.isNotBlank(userId)) {
            condition = condition.and(userShip.userId.eq(userId));
        }

        String stDt = (String)search.get("stDt");
        String edDt = (String)search.get("edDt");
        if(StringUtils.isNotBlank(stDt) && StringUtils.isNotBlank(edDt)) {
        stDt = stDt.replace("-", "");
        edDt = edDt.replace("-", "");
            condition = condition.and(userShip.shipDt.between(stDt, edDt));
        }

        return query.select( Projections.fields(UserShipDto.class,
                             code.codeVal.as("planTCd"),
                             code.codeNm.concat("합계").as("planTCdNm"),
                             userShip.quan.multiply(userShip.unitPack).sum().coalesce(new Double(0)).as("quan"),
                             ExpressionUtils.as(
                                     JPAExpressions.select(code.codeNm)
                                         .from(code)
                                         .where(code.codeId.eq("PACK_T_CD"),
                                                code.codeVal.eq(userShip.packTCd.coalesce("1"))),
                                     "packTCdNm")
                            ))
                    .from(code)
                    .leftJoin(userShip).on(condition)
                    .where(code.codeId.eq("PLAN_T_CD"))
                    .groupBy(code.codeVal)
                    .orderBy(code.codeVal.desc())
                    .fetch();
    }

    @Override
    public List<Map<String, Object>> chartByPlanTCd(Map<String, Object> search){

        BooleanExpression condition = userShip.statusCd.eq(Constants.STATUS_CD_NORMAL);

        String userId = (String)search.get("userId");
        if(StringUtils.isNotBlank(userId)) {
            condition = condition.and(userShip.userId.eq(userId));
        }

        String dt = (String)search.get("dt");
        if(StringUtils.isNotBlank(dt)) {
            dt = dt.replace("-", "");
            condition = condition.and(userShip.shipDt.startsWith(dt));
        }

        JPAQuery<Tuple> jpaResult = query.select( userShip.shipDt.substring(0, 6).as("dt"),
                                         userShip.planTCd.when(UserProductionDto.PLAN_T_CD_BUDGET)
                                             .then(userShip.quan)
                                             .otherwise(new Double(0))
                                             .sum().as("sum1"),
                                         userShip.planTCd.when(UserProductionDto.PLAN_T_CD_ACTUAL)
                                             .then(userShip.quan)
                                             .otherwise(new Double(0))
                                             .sum().as("sum2")
                                             )
                                        .from(userShip)
                                        .where(condition);


        String yearYn = (String) search.get("yearYn");
        if(StringUtils.isNoneBlank(yearYn)) {
            jpaResult = jpaResult.groupBy(userShip.shipDt.substring(0,6));
        }
        List<Tuple> result = jpaResult.orderBy(userShip.shipDt.asc()).fetch();

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

        BooleanExpression condition = userShip.statusCd.eq(Constants.STATUS_CD_NORMAL);

        //System.out.println(search.toString());

        String userId = (String)search.get("userId");
        if(StringUtils.isNotBlank(userId)) {
            condition = condition.and(userShip.userId.eq(userId));
        }

        String planTCd = (String)search.get("planTCd");
        if(StringUtils.isNotBlank(planTCd)) {
            condition = condition.and(userShip.planTCd.eq(planTCd));
        }

        String stDt = (String)search.get("stDt");
        String edDt = (String)search.get("edDt");
        if(StringUtils.isNotBlank(stDt) && StringUtils.isNotBlank(edDt)) {
            stDt = stDt.replace("-", "");
            edDt = edDt.replace("-", "");
            condition = condition.and(userShip.shipDt.between(stDt, edDt));
        }

        String shipDt = (String)search.get("shipDt");
        if(StringUtils.isNotBlank(shipDt)) {
            shipDt = shipDt.replace("-", "");
            condition = condition.and(userShip.shipDt.eq(shipDt));
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
                    orders.add(userShip.shipDt.asc());
                    return orders;
            }
            orders.add(userShip.userShipSeq.asc());
        } else {
            orders.add(userShip.shipDt.desc());
            orders.add(userShip.userShipSeq.desc());
        }
        return orders;
    }

}
