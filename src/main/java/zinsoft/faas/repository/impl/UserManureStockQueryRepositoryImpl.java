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
import zinsoft.faas.dto.UserManureStockDto;
import zinsoft.faas.entity.QUserInout;
import zinsoft.faas.entity.QUserManure;
import zinsoft.faas.entity.QUserManureStock;
import zinsoft.faas.repository.UserManureStockQueryRepository;
import zinsoft.util.Constants;
import zinsoft.web.entity.QCode;
import zinsoft.web.entity.QUserInfo;

@RequiredArgsConstructor
public class UserManureStockQueryRepositoryImpl implements UserManureStockQueryRepository {

    private final JPAQueryFactory query;

    private QBean<UserManureStockDto> allFields = null;

    private final QUserManureStock userManureStock = QUserManureStock.userManureStock;
    private final QUserManure userManure = QUserManure.userManure;
    private final QUserInout userInout = QUserInout.userInout;
    private final QCode code = QCode.code;
    private final QUserInfo userInfo = QUserInfo.userInfo;


    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserManureStock.class.getDeclaredFields())
                .filter(field -> !(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field -> {
                    try {
                        return (Expression<?>) field.get(QUserManureStock.userManureStock);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        // @formatter:off

        fieldList.add(userManure.manureNm);
        fieldList.add(userManure.makerNm);
        fieldList.add(userManure.manureTCd);
        fieldList.add(userInfo.userNm);


//        fieldList.remove(userManureStock.packTCd);
//        fieldList.add(userManure.packTCd.coalesce(userManureStock.packTCd).as("packTCd"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(code.codeNm)
                                .from(code)
                                .where(code.codeId.eq("PACK_T_CD"),
                                        code.codeVal.eq(
                                                // userManure.packTCd.coalesce(userManureStock.packTCd))
                                                userManureStock.packTCd
                                        )
                                ),
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
                                .where(code.codeId.eq("SUP_INOUT_CD"),
                                        code.codeVal.eq(userManureStock.supInoutCd)),
                        "supInoutCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(code.codeNm)
                                .from(code)
                                .where(code.codeId.eq("MANURE_T_CD2"),
                                        code.codeVal.eq(userManure.manureTCd2)),
                        "manureTCdNm2"));

        fieldList.add(
                ExpressionUtils.as(new CaseBuilder()
                                .when(userManureStock.userInoutSeq.isNotNull())
                                .then(JPAExpressions.select(userInout.inoutTCd)
                                        .from(userInout)
                                        .where(userInout.userInoutSeq.eq(userManureStock.userInoutSeq))
                                ).otherwise(""),
                        "inoutTCd"));
        // @formatterLon

        allFields = Projections.fields(UserManureStockDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public UserManureStockDto get(String userId, Long userManureStockSeq) {

        BooleanExpression condition = userManureStock.userManureStockSeq.eq(userManureStockSeq);

        return query.select(allFields)
                .from(userManureStock)
                .join(userInfo).on(userManureStock.userId.eq(userInfo.userId))
                .join(userManure).on(userManureStock.userManureSeq.eq(userManure.userManureSeq))
                .where(condition)
                .fetchOne();
    }

    @Override
    public Page<UserManureStockDto> page(Map<String, Object> search, Pageable pageable) {

        JPQLQuery<UserManureStockDto> jpqQuery = query.select(allFields)
                .from(userManureStock)
                .join(userInfo).on(userManureStock.userId.eq(userInfo.userId))
                .join(userManure).on(userManureStock.userManureSeq.eq(userManure.userManureSeq))
                .where(queryCondition(search))
                .groupBy(
                        userInfo.userId,
                        userManureStock.inoutDt, // orderBy 내용이 바뀌면 변경되어야 함
                        userManureStock.userManureStockSeq // orderBy 내용이 바뀌면 변경되어야 함
                )
                .orderBy(orderBy(search).stream().toArray(OrderSpecifier[]::new));

        QueryResults<UserManureStockDto> result = jpqQuery.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<UserManureStockDto> list(Map<String, Object> search) {
        JPQLQuery<UserManureStockDto> jpqQuery = query.select(allFields)
                .from(userManureStock)
                .join(userInfo).on(userManureStock.userId.eq(userInfo.userId))
                .join(userManure).on(userManureStock.userManureSeq.eq(userManure.userManureSeq))
                .where(queryCondition(search))
                .groupBy(
                        userInfo.userId,
                        userManureStock.inoutDt, // orderBy 내용이 바뀌면 변경되어야 함
                        userManureStock.userManureStockSeq // orderBy 내용이 바뀌면 변경되어야 함
                )
                .orderBy(orderBy(search).stream().toArray(OrderSpecifier[]::new));

        List<UserManureStockDto> result = jpqQuery.fetch();

        return result;
    }

    private BooleanExpression queryCondition(Map<String, Object> search) {

        BooleanExpression condition = userManureStock.statusCd.eq(Constants.STATUS_CD_NORMAL)
                .and(userManure.userManureSeq.eq(userManureStock.userManureSeq));

        String userId = (String) search.get("userId");
        if (StringUtils.isNotBlank(userId)) {
            condition = condition.and(userManureStock.userId.eq(userId));
        }

        String stDt = (String) search.get("stDt");
        String edDt = (String) search.get("edDt");
        if (StringUtils.isNotBlank(stDt) && StringUtils.isNotBlank(edDt)) {
            stDt = stDt.replace("-", "");
            edDt = edDt.replace("-", "");
            condition = condition.and(userManureStock.inoutDt.between(stDt, edDt));
        }

        if (search.get("userDiarySeq") != null) {
            Long userDiarySeq = (Long) search.get("userDiarySeq");
            condition = condition.and(userManureStock.userDiarySeq.eq(userDiarySeq));
        }

        if (search.get("userInoutSeq") != null) {
            Long userInoutSeq = (Long) search.get("userInoutSeq");
            condition = condition.and(userManureStock.userInoutSeq.eq(userInoutSeq));
        }

        String keyword = (String) search.get("keyword");
        if (StringUtils.isNoneBlank(keyword)) {
            condition = condition.and(userManure.manureNm.concat(userManureStock.remark).contains(keyword));
        }
        return condition;
    }

    private List<OrderSpecifier<?>> orderBy(Map<String, Object> search) {
        String orderBy = (String) search.get("orderBy");
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (StringUtils.isNotBlank(orderBy)) {
            switch (orderBy) {
                case "ASC":
                    orders.add(userManureStock.inoutDt.asc());
                    orders.add(userManureStock.userManureStockSeq.asc());
                    return orders;
            }
        } else {
            orders.add(userManureStock.inoutDt.desc());
            orders.add(userManureStock.userManureStockSeq.desc());
        }
        return orders;
    }

}
