package zinsoft.faas.repository.impl;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.faas.dto.UserChemicalStockDto;
import zinsoft.faas.entity.QUserChemical;
import zinsoft.faas.entity.QUserChemicalStock;
import zinsoft.faas.entity.QUserInout;
import zinsoft.faas.repository.UserChemicalStockQueryRepository;
import zinsoft.util.Constants;
import zinsoft.web.entity.QCode;
import zinsoft.web.entity.QUserInfo;

@RequiredArgsConstructor
public class UserChemicalStockQueryRepositoryImpl implements UserChemicalStockQueryRepository {

    private final JPAQueryFactory query;

    private QBean<UserChemicalStockDto> allFields = null;

    private final QUserChemicalStock userChemicalStock = QUserChemicalStock.userChemicalStock;
    private final QUserChemical userChemical = QUserChemical.userChemical;
    private final QUserInout userInout = QUserInout.userInout;
    private final QCode code = QCode.code;
    private final QUserInfo userInfo = QUserInfo.userInfo;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserChemicalStock.class.getDeclaredFields())
                .filter(field -> !(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field -> {
                    try {
                        return (Expression<?>) field.get(QUserChemicalStock.userChemicalStock);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        // @formatter:off
        fieldList.add(userChemical.chemicalNm);
        fieldList.add(userChemical.userChemicalNm);
        fieldList.add(userChemical.makerNm);
        fieldList.add(userChemical.chemicalTCd);
        fieldList.add(userInfo.userNm);

//        fieldList.remove(userChemicalStock.packTCd);
//        fieldList.add(userChemical.packTCd.coalesce(userChemicalStock.packTCd).as("packTCd"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(code.codeNm)
                                .from(code)
                                .where(code.codeId.eq("PACK_T_CD"),
                                        code.codeVal.eq(
                                                //userChemical.packTCd.coalesce(userChemicalStock.packTCd)
                                                userChemicalStock.packTCd
                                        )),
                        "packTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(code.codeNm)
                                .from(code)
                                .where(code.codeId.eq("CHEMICAL_T_CD"),
                                        code.codeVal.eq(userChemical.chemicalTCd)),
                        "chemicalTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(code.codeNm)
                                .from(code)
                                .where(code.codeId.eq("SUP_INOUT_CD"),
                                        code.codeVal.eq(userChemicalStock.supInoutCd)),
                        "supInoutCdNm"));

        fieldList.add(
                ExpressionUtils.as(new CaseBuilder()
                                .when(userChemicalStock.userInoutSeq.isNotNull())
                                .then(JPAExpressions.select(userInout.inoutTCd)
                                        .from(userInout)
                                        .where(userInout.userInoutSeq.eq(userChemicalStock.userInoutSeq))
                                ).otherwise(""),
                        "inoutTCd"));

//        fieldList.add(
//                ExpressionUtils.as(SQLExpressions.sum(new CaseBuilder()
//                .when(userChemicalStock.supInoutCd.eq("I"))
//                .then(userChemicalStock.quan)
//                .otherwise(userChemicalStock.quan.multiply(-1)))
//    .over().partitionBy(userChemicalStock.userChemicalSeq).orderBy(userChemicalStock.inoutDt.asc()),
//       "remainingQuan"));
//    /*.orderBy(Expressions.stringTemplate("userChemicalStock.inoutDt asc"))*/
//        fieldList.add(ExpressionUtils.as(Expressions.stringTemplate("sum(case when userChemicalStock.supInoutCd = 'I' then userChemicalStock.quan else userChemicalStock.quan end) over (PARTITION BY userChemicalStock.userChemicalSeq order by userChemicalStock.inoutDt asc)").castToNum(Double.class), "remainingQuan"));
        // @formatterLon

        allFields = Projections.fields(UserChemicalStockDto.class, fieldList.toArray(new Expression<?>[0]));

    }

    @Override
    public UserChemicalStockDto get(String userId, Long userChemicalStockSeq) {

        BooleanExpression condition = userChemicalStock.userChemicalStockSeq.eq(userChemicalStockSeq);

        return query.select(allFields)
                .from(userChemicalStock)
                .join(userInfo).on(userChemicalStock.userId.eq(userInfo.userId))
                .join(userChemical).on(userChemicalStock.userChemicalSeq.eq(userChemical.userChemicalSeq))
                .where(condition)
                .fetchOne();
    }

    @Override
    public Page<UserChemicalStockDto> page(Map<String, Object> search, Pageable pageable) {

        JPQLQuery<UserChemicalStockDto> jpqQuery = query.select(allFields)
                .from(userChemicalStock)
                .join(userInfo).on(userChemicalStock.userId.eq(userInfo.userId))
                .join(userChemical).on(userChemicalStock.userChemicalSeq.eq(userChemical.userChemicalSeq))
                .where(queryCondition(search))
                .groupBy(
                        userInfo.userId,
                        userChemicalStock.inoutDt, // orderBy 내용이 바뀌면 변경되어야 함
                        userChemicalStock.userChemicalStockSeq // orderBy 내용이 바뀌면 변경되어야 함
                )
                .orderBy(orderBy(search).stream().toArray(OrderSpecifier[]::new));

        QueryResults<UserChemicalStockDto> result = jpqQuery.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<UserChemicalStockDto> list(Map<String, Object> search) {

        JPQLQuery<UserChemicalStockDto> jpqQuery = query.select(allFields)
                .from(userChemicalStock)
                .join(userInfo).on(userChemicalStock.userId.eq(userInfo.userId))
                .join(userChemical).on(userChemicalStock.userChemicalSeq.eq(userChemical.userChemicalSeq))
                .where(queryCondition(search))
                .groupBy(
                        userInfo.userId,
                        userChemicalStock.inoutDt, // orderBy 내용이 바뀌면 변경되어야 함
                        userChemicalStock.userChemicalStockSeq // orderBy 내용이 바뀌면 변경되어야 함
                )
                .orderBy(orderBy(search).stream().toArray(OrderSpecifier[]::new));

        List<UserChemicalStockDto> result = jpqQuery.fetch();

        return result;
    }

    private BooleanExpression queryCondition(Map<String, Object> search) {

        BooleanExpression condition = userChemicalStock.statusCd.eq(Constants.STATUS_CD_NORMAL)
                .and(userChemical.userChemicalSeq.eq(userChemicalStock.userChemicalSeq));

        String userId = (String) search.get("userId");
        if (StringUtils.isNotBlank(userId)) {
            condition = condition.and(userChemicalStock.userId.eq(userId));
        }

        String stDt = (String) search.get("stDt");
        String edDt = (String) search.get("edDt");
        if (StringUtils.isNotBlank(stDt) && StringUtils.isNotBlank(edDt)) {
            stDt = stDt.replace("-", "");
            edDt = edDt.replace("-", "");
            condition = condition.and(userChemicalStock.inoutDt.between(stDt, edDt));
        }

        if (search.get("userDiarySeq") != null) {
            Long userDiarySeq = (Long) search.get("userDiarySeq");
            condition = condition.and(userChemicalStock.userDiarySeq.eq(userDiarySeq));
        }

        if (search.get("userInoutSeq") != null) {
            Long userInoutSeq = (Long) search.get("userInoutSeq");
            condition = condition.and(userChemicalStock.userInoutSeq.eq(userInoutSeq));
        }

        String keyword = (String) search.get("keyword");
        if (StringUtils.isNoneBlank(keyword)) {
            condition = condition.and(userChemical.userChemicalNm.concat(userChemicalStock.remark).contains(keyword));
        }

        return condition;
    }

    private List<OrderSpecifier<?>> orderBy(Map<String, Object> search) {
        String orderBy = (String) search.get("orderBy");
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (StringUtils.isNotBlank(orderBy)) {
            switch (orderBy) {
                case "ASC":
                    orders.add(userChemicalStock.inoutDt.asc());
                    orders.add(userChemicalStock.userChemicalStockSeq.asc());
                    return orders;
            }
        } else {
            orders.add(userChemicalStock.inoutDt.desc());
            orders.add(userChemicalStock.userChemicalStockSeq.desc());
        }
        return orders;
    }

}
