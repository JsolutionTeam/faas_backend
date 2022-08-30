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
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.faas.dto.AccountDto;
import zinsoft.faas.entity.QAccount;
import zinsoft.faas.repository.AccountQueryRepository;
import zinsoft.util.Constants;
import zinsoft.web.common.entity.QCode;

@RequiredArgsConstructor
public class AccountQueryRepositoryImpl implements AccountQueryRepository {

    private final JPAQueryFactory query;

    private QBean<AccountDto> allFields = null;

    private final QAccount account = QAccount.account;
    private final QAccount account2 = new QAccount("account2");
    private final QCode code = QCode.code;

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QAccount.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QAccount.account);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        // @formatter:off
        fieldList.remove(account.acNm);
        fieldList.add(
                ExpressionUtils.as(
                        Expressions.stringTemplate("LOWER( TRIM({0}))", account.acNm),
                        "acNm"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(account2.acNm)
                        .from(account2)
                        .where(account2.acId.eq(account.upAcId)),
                        "upAcNm"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("COST_T_CD"),
                               code.codeVal.eq(account.costTCd)),
                        "costTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("BP_T_CD"),
                               code.codeVal.eq(account.bpTCd)),
                        "bpTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("CD_T_CD"),
                               code.codeVal.eq(account.cdTCd)),
                        "cdTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("PS_T_CD"),
                               code.codeVal.eq(account.psTCd)),
                        "psTCdNm"));

//        fieldList.add(userChemical.chemicalNm);
//        fieldList.add(userChemical.makerNm);
//        fieldList.add(userChemical.chemicalTCd);
//
//        fieldList.remove(account.packTCd);
//        fieldList.add(userChemical.packTCd.coalesce(account.packTCd).as("packTCd"));

//        fieldList.add(userCrop.aliasNm.coalesce(crop.exprNm).as("cropNm"));
        // @formatterLon

        allFields = Projections.fields(AccountDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public String nextAcId(String upAcId) {//coalesce("0").asNumber()
        BooleanExpression condition = account.acId.in(JPAExpressions
                                                        //.select(account.acId.max().castToNum(Long.class).add(new Long(1)) )
                                                        .select(Expressions.stringTemplate("IFNULL(MAX({0}), 0) + 1", account.acId))
                                                        .from(account)
                                                        .where(account.upAcId.eq(upAcId)));

        long cnt =  query.selectFrom(account)
                    .where(condition)
                    .fetchCount();

        Object acId = null;
        if(cnt > 0) {
            acId = query.select(Expressions.stringTemplate("IFNULL(MAX({0}), 0) + 1", account.acId))
                    .from(account)
                    .fetchOne();
        }else {
            acId = query.select(Expressions.stringTemplate("IFNULL(MAX({0}), 0) + 1", account.acId))
                    .from(account)
                    .where(account.upAcId.eq(upAcId))
                    .fetchOne();
        }

        return ((Integer)acId).toString();
    }

    @Override
    public AccountDto get(String acId) {

        BooleanExpression condition = account.acId.eq(acId);

        return query.select(allFields)
                .from(account)
                .where(condition)
                .fetchOne();
    }

    @Override
    public Page<AccountDto> page(Map<String, Object> search, Pageable pageable) {
        BooleanExpression condition = account.statusCd.eq(Constants.STATUS_CD_NORMAL)
                                        .and(account.bpTCd.eq("P"));

        String keyword = (String)search.get("keyword");
        String field = (String)search.get("field");
        if(StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(field)) {
            switch(field) {
                case "acId":
                    condition = condition.and(account.acId.contains(keyword));
                    break;
                case "acNm":
                    condition = condition.and(account.acNm.contains(keyword));
                    break;
                case "costTCdNm":
                    condition = condition.and(code.codeNm.contains(keyword));
                    break;
            }
        }

        JPQLQuery<AccountDto> jpqQuery = query.select(allFields)
                                        .from(account)
                                        .leftJoin(code).on(account.costTCd.eq(code.codeVal).and(code.codeId.eq("COST_T_CD")))
                                        .where(condition)
                                        .orderBy(account.acId.asc(), account.bpTCd.asc(), account.cdTCd.asc(), account.exprSeq.asc());

        QueryResults<AccountDto> result = null;

        if (search.get(pageSizeParameter) != null) {
            result = jpqQuery.offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetchResults();
        } else {

            result = jpqQuery.fetchResults();
        }
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<AccountDto> list(Map<String, Object> search) {
        BooleanExpression condition = account.statusCd.eq(Constants.STATUS_CD_NORMAL);
        String acId = (String)search.get("acId");
        if(StringUtils.isNotBlank(acId) ) {
            if(acId.length() == 3) {
                condition = condition.and(account.acId.eq(acId));
            }else {
                condition = condition.and(account.acId.contains(acId));
            }
        }

        String acNm = (String)search.get("acNm");
        if(StringUtils.isNotBlank(acNm)) {
            condition = condition.and(account.acNm.eq(acNm));
        }

        String bpTCd = (String)search.get("bpTCd");
        if(StringUtils.isNotBlank(bpTCd)) {
            condition = condition.and(account.bpTCd.eq(bpTCd));
        }

        String cdTCd = (String)search.get("cdTCd");
        if(StringUtils.isNotBlank(cdTCd)) {
            condition = condition.and(account.cdTCd.eq(cdTCd));
        }

        String exprYn = (String)search.get("exprYn");
        if(StringUtils.isNotBlank(exprYn)) {
            condition = condition.and(account.exprYn.eq(exprYn));
        }

        String inputYn = (String)search.get("inputYn");
        if(StringUtils.isNotBlank(inputYn)) {
            condition = condition.and(account.inputYn.eq(inputYn));
        }

        String upAcId = (String)search.get("upAcId");
        if(StringUtils.isNotBlank(upAcId)) {
            condition = condition.and(account.upAcId.eq(upAcId));
        }

        String psTCd = (String)search.get("psTCd");
        if(StringUtils.isNotBlank(psTCd)) {
            condition = condition.and(account.psTCd.eq(psTCd));
        }

        String keyword = (String)search.get("keyword");
        String field = (String)search.get("field");
        if(StringUtils.isNotBlank(keyword) && StringUtils.isNotBlank(field)) {
            switch(field) {
                case "acId":
                    condition = condition.and(account.acId.contains(keyword));
                    break;
                case "acNm":
                    condition = condition.and(account.acNm.contains(keyword));
                    break;
            }
        }

        List<AccountDto> result = query.select(allFields)
                                    .from(account)
                                    .where(condition)
                                    .orderBy(queryOrderBy(search))
                                    .fetch();
        return result;
    }

    private BooleanExpression queryCondition(String userId) {

//        BooleanExpression condition = account.statusCd.eq(Constants.STATUS_CD_NORMAL)
//                .and(userChemical.userChemicalSeq.eq(account.userChemicalSeq));
//
//        if (StringUtils.isNotBlank(userId)) {
//            condition = condition.and(account.userId.eq(userId));
//        }
//
//        return condition;
        return null;
    }

    private OrderSpecifier<?> queryOrderBy(Map<String, Object> search){
        OrderSpecifier<?> order = account.acId.asc();

        String orderBy = (String)search.get("orderBy");
        if(StringUtils.isNotBlank(orderBy)) {
            switch(orderBy) {
                case "ASC":
                    order = account.acId.asc();
                    break;
                case "NAME_ASC":
                    order = account.acNm.asc();
                    break;
                default:
                    order = account.acId.desc();
                    break;
            }
        }

        return order;
    }



}
