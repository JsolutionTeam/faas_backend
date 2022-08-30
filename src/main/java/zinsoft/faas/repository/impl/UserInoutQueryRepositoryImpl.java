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
import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.entity.QAccount;
import zinsoft.faas.entity.QUserInout;
import zinsoft.faas.repository.UserInoutQueryRepository;
import zinsoft.util.Constants;
import zinsoft.web.common.entity.QCode;
import zinsoft.web.common.entity.QUserInfo;

@RequiredArgsConstructor
public class UserInoutQueryRepositoryImpl implements UserInoutQueryRepository {

    private final JPAQueryFactory query;

    private QBean<UserInoutDto> allFields = null;

    private final QUserInout userInout = QUserInout.userInout;
    private final QCode code = QCode.code;
//    private final QUserCrop userCrop = QUserCrop.userCrop;
//    private final QCrop crop = QCrop.crop;
    private final QUserInfo userInfo = QUserInfo.userInfo;
    private final QAccount account = QAccount.account;

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserInout.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QUserInout.userInout);
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
                        .where(code.codeId.eq("INOUT_CD"),
                               code.codeVal.eq(userInout.inoutCd)),
                    "inoutCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                    JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("PACK_T_CD"),
                               code.codeVal.eq(userInout.packTCd)),
                    "packTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                    JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("GRADE_T_CD"),
                               code.codeVal.eq(userInout.gradeTCd)),
                    "gradeTCdNm"));
//        fieldList.add(
//                ExpressionUtils.as(
//                    JPAExpressions.select(account.acNm)
//                        .from(account)
//                        .where(account.acId.eq(userInout.acId)),
//                    "acNm"));
        fieldList.add(
                ExpressionUtils.as(
                    JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("INOUT_T_CD"),
                               code.codeVal.eq(userInout.inoutTCd)),
                    "inoutTCdNm"));
//        fieldList.add(
//                ExpressionUtils.as(
//                    JPAExpressions.select(account.upAcId)
//                        .from(account)
//                        .where(account.acId.eq(userInout.acId)),
//                    "upAcId"));
        fieldList.add(
                ExpressionUtils.as(
                    JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("R_INOUT_T_CD"),
                               code.codeVal.eq(userInout.rInoutTCd)),
                    "rInoutTCdNm"));

//        fieldList.add(userCrop.aliasNm.as("userCropAliasNm") );
//        fieldList.add(crop.exprNm.as("cropNm") );
        fieldList.add(userInfo.userNm.as("userNm") );
        fieldList.add(userInfo.addr1.as("addr1") );
        fieldList.add(userInfo.emailAddr.as("emailAddr") );
        fieldList.add(account.upAcId.as("upAcId") );
        fieldList.add(account.acNm.as("acNm") );
        fieldList.add(account.costTCd.as("costTCd") );
        //fieldList.add( crop.exprNm.as("cropNm") );
       // @formatter:on

        allFields = Projections.fields(UserInoutDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public UserInoutDto get(UserInoutDto dto) {

        BooleanExpression condition = userInout.statusCd.eq(Constants.STATUS_CD_NORMAL);
        String userId = dto.getUserId();
        Long userInoutSeq = dto.getUserInoutSeq();

        if (StringUtils.isNotBlank(userId)) {
            condition = condition.and(userInout.userId.eq(userId));
        }

        if (userInoutSeq != null && userInoutSeq > 0) {
            condition = condition.and(userInout.userInoutSeq.eq(userInoutSeq));
        }

        return query.select(allFields)
                .from(userInout)
//                .join(userCrop)
//                .on(userInout.userCropSeq.eq(userCrop.userCropSeq))
//                .join(crop)
//                .on(userCrop.cropSeq.eq(crop.cropSeq))
                .leftJoin(account)
                .on(userInout.acId.eq(account.acId))
                .join(userInfo)
                .on(userInout.userId.eq(userInfo.userId))
                .where(condition)
                .fetchOne();
    }

    @Override
    public Page<UserInoutDto> page(Map<String, Object> search, Pageable pageable) {
     // @formatter:off
        JPQLQuery<UserInoutDto> jpqQuery = query.select(allFields)
                                            .from(userInout)
//                                            .join(userCrop)
//                                            .on(userInout.userCropSeq.eq(userCrop.userCropSeq))
//                                            .leftJoin(crop)
//                                            .on(userCrop.cropSeq.eq(crop.cropSeq))
                                            .leftJoin(account)
                                            .on(userInout.acId.eq(account.acId))
                                            .join(userInfo)
                                            .on(userInout.userId.eq(userInfo.userId))
                                            .where(pageCondition(search));
     // @formatter:on
        QueryResults<UserInoutDto> result = null;

        if (search.get(pageSizeParameter) != null) {
            result = jpqQuery.orderBy(orderBy(search).stream().toArray(OrderSpecifier[]::new))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        } else {
            search.put("orderBy", "ASC");
            result = jpqQuery.orderBy(orderBy(search).stream().toArray(OrderSpecifier[]::new))
                    .fetchResults();
        }

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression pageCondition(Map<String, Object> search) {

        BooleanExpression condition = userInout.statusCd.eq(Constants.STATUS_CD_NORMAL);
        String userId = (String) search.get("userId");
        if (StringUtils.isNotBlank(userId)) {
            condition = condition.and(userInout.userId.eq(userId));
        }

        String trdDt = (String) search.get("trdDt");
        String sTrdDt = (String) search.get("sTrdDt");
        String eTrdDt = (String) search.get("eTrdDt");
        if (StringUtils.isNotBlank(trdDt)) {
            condition = condition.and(userInout.trdDt.eq(trdDt));
        } else if (StringUtils.isNotBlank(sTrdDt) && StringUtils.isNotBlank(eTrdDt)) {
            condition = condition.and(userInout.trdDt.between(sTrdDt, eTrdDt));
        }

        String inoutCd = (String) search.get("inoutCd");
        if (StringUtils.isNotBlank(inoutCd)) {
            condition = condition.and(userInout.inoutCd.eq(inoutCd));
        }

        if (search.get("cropSeq") != null) {
            Long cropSeq = Long.valueOf((String) search.get("cropSeq"));
            if (cropSeq > 0) {
                condition = condition.and(userInout.cropSeq.eq(cropSeq));
            }
        }

        if (search.get("userCropSeq") != null) {
            Long userCropSeq = Long.valueOf((String) search.get("userCropSeq"));
            if (userCropSeq > 0) {
                condition = condition.and(userInout.userCropSeq.eq(userCropSeq));
            }
        }

        String inoutTCd = (String) search.get("inoutTCd");
        if (StringUtils.isNotBlank(inoutTCd)) {
            condition = condition.and(userInout.inoutTCd.eq(inoutTCd));
        }

        String costTCd = (String) search.get("costTCd");
        if (StringUtils.isNotBlank(costTCd)) {
            condition = condition.and(account.costTCd.eq(costTCd));
        }

        String admCd = (String) search.get("admCd");
        if (StringUtils.isNotBlank(admCd)) {
            condition = condition.and(userInfo.admCd.contains(admCd));
        }

        String keyword = (String) search.get("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            switch ((String) search.get("field")) {
                case "userNm":
                    condition = condition.and(userInfo.userNm.contains(keyword));
                    break;
                case "userId":
                    condition = condition.and(userInout.userId.contains(keyword));
                    break;
//                case "cropNm":
//                    condition = condition.and(crop.exprNm.contains(keyword));
//                    break;
                case "custNm":
                    condition = condition.and(userInout.custNm.contains(keyword));
                    break;
                case "remark":
                    condition = condition.and(userInout.remark.contains(keyword));
                    break;
                case "inoutContent":
                    condition = condition.and(userInout.inoutContent.contains(keyword));
                    break;
                default:
                    condition = condition.and(userInfo.userNm.contains(keyword)
                                                .or(userInout.userId.contains(keyword))
                                                //.or(crop.exprNm.contains(keyword))
                                                .or(userInout.custNm.contains(keyword))
                                                .or(userInout.remark.contains(keyword))
                                                .or(userInout.inoutContent.contains(keyword)));
                    break;
            }
        }
        return condition;
    }

    private List<OrderSpecifier<?>> orderBy(Map<String, Object> search) {
        String orderBy = (String) search.get("orderBy");
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (StringUtils.isNotBlank(orderBy)) {
            switch (orderBy) {
                case "ASC":
                    orders.add(userInout.trdDt.asc());
                    return orders;
            }
        } else {
            orders.add(userInout.trdDt.desc());
            orders.add(userInout.userInoutSeq.desc());
        }
        return orders;
    }
//    SELECT b.AC_NM, COUNT(a.USER_INOUT_SEQ) cnt
//    FROM tf_user_inout a
//        JOIN tf_account b ON a.AC_ID = b.AC_ID
//    WHERE a.STATUS_CD = 'N'
//        AND a.TRD_DT LIKE '2021%'
//    GROUP BY a.ac_id
    @Override
    public List<Map<String, Object>> countByAcId(String userId, String trdDt ){
        BooleanExpression condition = userInout.statusCd.eq(Constants.STATUS_CD_NORMAL)
                                            .and( userInout.inoutCd.eq(UserInoutDto.INOUT_CD_OUTGOING) );

        if (StringUtils.isNotBlank(userId)) {
            condition = condition.and(userInout.userId.eq(userId));
        }

        if (StringUtils.isNotBlank(trdDt)) {
            condition = condition.and(userInout.trdDt.startsWith(trdDt));
        }

        return query.select(account.acNm,
                            userInout.amt.sum())
                .from(userInout)
                .join(account)
                    .on(userInout.acId.eq(account.acId))
                .where(condition)
                .groupBy(userInout.acId)
                .fetch()
                .stream()
                .map(t -> {Map<String, Object> m = new HashMap<>();
                           m.put("acNm", t.get(account.acNm));
                           m.put("cnt", t.get(userInout.amt.sum()));
                           return m;})
                .collect(Collectors.toList());
    }
}
