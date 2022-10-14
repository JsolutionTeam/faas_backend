package zinsoft.faas.repository.impl;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
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
import zinsoft.faas.dto.QUserDiaryDto;
import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.entity.QActivity;
import zinsoft.faas.entity.QUserDiary;
import zinsoft.faas.repository.UserDiaryQueryRepository;
import zinsoft.util.Constants;
import zinsoft.web.entity.QCode;
import zinsoft.web.entity.QUserInfo;

@RequiredArgsConstructor
@Slf4j
public class UserDiaryQueryRepositoryImpl implements UserDiaryQueryRepository {

    private final JPAQueryFactory query;

    private QBean<UserDiaryDto> allFields = null;

    private final QUserDiary userDiary = QUserDiary.userDiary;
    private final QCode code = QCode.code;
    //    private final QUserCrop userCrop = QUserCrop.userCrop;
//    private final QCrop crop = QCrop.crop;
    private final QUserInfo userInfo = QUserInfo.userInfo;
    private final QActivity activity = QActivity.activity;

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserDiary.class.getDeclaredFields())
                .filter(field -> !(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field -> {
                    try {
                        return (Expression<?>) field.get(QUserDiary.userDiary);
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
                                .where(code.codeId.eq("DIARY_T_CD"),
                                        code.codeVal.eq(userDiary.diaryTCd)),
                        "diaryTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(code.codeNm)
                                .from(code)
                                .where(code.codeId.eq("SKY_T_CD"),
                                        code.codeVal.eq(userDiary.skyTCd)),
                        "skyTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(code.codeNm)
                                .from(code)
                                .where(code.codeId.eq("PACK_T_CD"),
                                        code.codeVal.eq(userDiary.packTCd)),
                        "packTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(code.codeNm)
                                .from(code)
                                .where(code.codeId.eq("GRADE_T_CD"),
                                        code.codeVal.eq(userDiary.gradeTCd)),
                        "gradeTCdNm"));

        fieldList.remove(userDiary.actNm);
        fieldList.add(userDiary.actNm.coalesce(activity.actNm).as("actNm"));
        fieldList.add(userInfo.userNm.as("userNm"));
        fieldList.add(userInfo.addr1.concat(userInfo.addr2).as("addr"));
        fieldList.add(userInfo.emailAddr.as("emailAddr"));
        //fieldList.add( crop.exprNm.as("cropNm") );

        // 영농일지 고도화 중 추가된 작업


        // crop b cd 값으로 조회하기 위해 가져온다.
        fieldList.add(code.codeNm.as("cropBCdNm"));


        // @formatter:on

        allFields = Projections.fields(UserDiaryDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public UserDiaryDto get(UserDiaryDto dto) {

        BooleanExpression condition = userDiary.statusCd.eq(Constants.STATUS_CD_NORMAL);
        String userId = dto.getUserId();
        Long userDiarySeq = dto.getUserDiarySeq();

        if (StringUtils.isNotBlank(userId)) {
            condition = condition.and(userDiary.userId.eq(userId));
        }

        if (userDiarySeq != null && userDiarySeq > 0) {
            condition = condition.and(userDiary.userDiarySeq.eq(userDiarySeq));
        }

        return query.select(allFields)
                .from(userDiary)
                .leftJoin(activity)
                .on(userDiary.activitySeq.eq(activity.activitySeq))
                .join(userInfo)
                .on(userDiary.userId.eq(userInfo.userId))
                .where(condition)
                .fetchOne();
    }

    @Override
    public List<String> listYear(String userId, String diaryTCd, String year) {
        BooleanExpression condition = userDiary.statusCd.eq(Constants.STATUS_CD_NORMAL);

        if (StringUtils.isNotBlank(userId)) {
            condition = condition.and(userDiary.userId.eq(userId));
        }

        if (StringUtils.isNotBlank(diaryTCd)) {
            condition = condition.and(userDiary.diaryTCd.eq(diaryTCd));
        }

        if (StringUtils.isNotBlank(year)) {
            condition = condition.and(userDiary.actDt.startsWith(year));
        }

        return query.select(userDiary.actDt.substring(0, 4))
                .from(userDiary)
                .where(condition)
                .groupBy(userDiary.actDt.substring(0, 4))
                .fetch();
    }

    @Override
    public Page<UserDiaryDto> page(Map<String, Object> search, Pageable pageable) {
        JPQLQuery<UserDiaryDto> jpqQuery = query.select(allFields)
                .from(userDiary)
                // user diary - activity(작업단계) 조회하기 위해
                .leftJoin(activity)
                .on(userDiary.activitySeq.eq(activity.activitySeq)).fetchJoin()

                // user diary 작성자 정보 조회를 위해
                .join(userInfo)
                .on(userDiary.userId.eq(userInfo.userId))

                .where(pageCondition(search));
        QueryResults<UserDiaryDto> result = null;

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

//        JPQLQuery<UserDiaryDto> jpqQuery = query.select(
//                        new QUserDiaryDto(
//                                userDiary,
//                                code.codeNm,
//                                cropSpecies.name,
//                                userDiary.actNm,//@TODO 작형 추후에 변경~.~
//                                ExpressionUtils.as(
//                                        JPAExpressions.select(code.codeNm)
//                                                .from(code)
//                                                .where(code.codeId.eq("DIARY_T_CD"),
//                                                        code.codeVal.eq(userDiary.diaryTCd)),
//                                        "diaryTCdNm"),
//                                ExpressionUtils.as(
//                                        JPAExpressions.select(code.codeNm)
//                                                .from(code)
//                                                .where(code.codeId.eq("SKY_T_CD"),
//                                                        code.codeVal.eq(userDiary.skyTCd)),
//                                        "skyTCdNm"),
//                                ExpressionUtils.as(
//                                        JPAExpressions.select(code.codeNm)
//                                                .from(code)
//                                                .where(code.codeId.eq("PACK_T_CD"),
//                                                        code.codeVal.eq(userDiary.packTCd)),
//                                        "packTCdNm"),
//                                ExpressionUtils.as(
//                                        JPAExpressions.select(code.codeNm)
//                                                .from(code)
//                                                .where(code.codeId.eq("GRADE_T_CD"),
//                                                        code.codeVal.eq(userDiary.gradeTCd)),
//                                        "gradeTCdNm"),
//                                ExpressionUtils.as(
//                                        JPAExpressions.select(userDiary.activitySeq.count())
//                                                .from(userDiary)
//                                        , "actCnt"
//                                ),
//                                userInfo.userNm,
//                                userInfo.addr1.concat(userInfo.addr2).as("addr"),
//                                userInfo.emailAddr
//                        )
//                )
//                .from(userDiary)
//                .leftJoin(activity)
//                .on(userDiary.activitySeq.eq(activity.activitySeq)).fetchJoin()
////                                            .join(userCrop)
////                                            .on(userDiary.userCropSeq.eq(userCrop.userCropSeq))
////                                            .leftJoin(crop)
////                                            .on(userCrop.cropSeq.eq(crop.cropSeq))
//                .join(userInfo)
//                .on(userDiary.userId.eq(userInfo.userId)).fetchJoin()
//                .leftJoin(cropSpecies)
//                .on(cropSpecies.eq(userDiary.cropSpecies)).fetchJoin()
//                .leftJoin(code)
//                .on(code.codeId.eq(userDiary.cropBCd)).fetchJoin()
//
//                .where(pageCondition(search));
//        // @formatter:on
//        QueryResults<UserDiaryDto> result = null;
//
//        if (search.get(pageSizeParameter) != null) {
//            result = jpqQuery.orderBy(orderBy(search).stream().toArray(OrderSpecifier[]::new))
//                    .offset(pageable.getOffset())
//                    .limit(pageable.getPageSize())
//                    .fetchResults();
//        } else {
//            search.put("orderBy", "ASC");
//            result = jpqQuery.orderBy(orderBy(search).stream().toArray(OrderSpecifier[]::new))
//                    .fetchResults();
//        }
//
//        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression pageCondition(Map<String, Object> search) {

        // status code가 N이고,
        BooleanExpression condition = userDiary.statusCd.eq(Constants.STATUS_CD_NORMAL);

        // userId가 같은 데이터를 기본으로 찾는다.
        String userId = (String) search.get("userId");
        if (StringUtils.isNotBlank(userId)) {
            condition = condition.and(userDiary.userId.eq(userId));
        }

        String actDt = (String) search.get("actDt");
        String sActDt = (String) search.get("sActDt");
        String eActDt = (String) search.get("eActDt");
        if (StringUtils.isNotBlank(actDt)) {
            condition = condition.and(userDiary.actDt.eq(actDt));
        } else if (StringUtils.isNotBlank(sActDt) && StringUtils.isNotBlank(eActDt)) {
            condition = condition.and(userDiary.actDt.between(sActDt, eActDt));
        }

        String diaryTCd = (String) search.get("diaryTCd");
        if (StringUtils.isNotBlank(diaryTCd)) {
            condition = condition.and(userDiary.diaryTCd.eq(diaryTCd));
        }

        if (search.get("cropSeq") != null) {
            String strCropSeq = (String) search.get("cropSeq");
            if (StringUtils.isNotBlank(strCropSeq) != false) {
                Long cropSeq = Long.valueOf(strCropSeq);
                if (cropSeq > 0) {
                    condition = condition.and(userDiary.cropSeq.eq(cropSeq));
                }
            }
        }

        if (search.get("userCropSeq") != null) {
            String strUserCropSeq = (String) search.get("userCropSeq");
            if (StringUtils.isNotBlank(strUserCropSeq) != false) {
                Long userCropSeq = Long.valueOf(strUserCropSeq);
                if (userCropSeq > 0) {
                    condition = condition.and(userDiary.userCropSeq.eq(userCropSeq));
                }
            }
        }

        if (search.get("activitySeq") != null) {
            String strActivitySeq = (String) search.get("activitySeq");
            if (StringUtils.isNotBlank(strActivitySeq) != false) {
                Long activitySeq = Long.valueOf(strActivitySeq);
                if (activitySeq > 0) {
                    condition = condition.and(userDiary.activitySeq.eq(activitySeq));
                }
            }
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
                    condition = condition.and(userDiary.userId.contains(keyword));
                    break;
                case "remark":
                    condition = condition.and(userDiary.remark.contains(keyword));
                    break;

//                case "cropNm":
//                    condition = condition.and(crop.exprNm.contains(keyword));
//                    break;
                default:
                    condition = condition.and(userInfo.userNm.contains(keyword).or(userDiary.userId.contains(keyword))/*.or(crop.exprNm.contains(keyword))*/);
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
                    orders.add(userDiary.actDt.asc());
                    orders.add(userDiary.userDiarySeq.asc());
                    return orders;
                case "DESC":
                    orders.add(userDiary.actDt.desc());
                    orders.add(userDiary.userDiarySeq.desc());
                    return orders;
            }
        } else {
            orders.add(userDiary.actDt.desc());
            orders.add(userDiary.userDiarySeq.desc());
        }
        return orders;
    }

//  SELECT  b.ACT_NM, COUNT(b.ACTIVITY_SEQ) cnt
//  FROM tf_user_diary a
//      JOIN tf_activity b ON a.ACTIVITY_SEQ = b.ACTIVITY_SEQ
//WHERE a.STATUS_CD = 'N'
//      AND a.USER_ID = 'sybae'
//      AND act_dt LIKE '202110%'
//GROUP BY a.activity_seq

    @Override
    public List<Map<String, Object>> countByActSeq(String userId, String actDt) {
        BooleanExpression condition = userDiary.statusCd.eq(Constants.STATUS_CD_NORMAL);

        if (StringUtils.isNotBlank(userId)) {
            condition = condition.and(userDiary.userId.eq(userId));
        }

        if (StringUtils.isNotBlank(actDt)) {
            condition = condition.and(userDiary.actDt.startsWith(actDt));
        }

        return query.select(activity.actNm,
                        activity.activitySeq.count()
                )
                .from(userDiary)
                .join(activity).on(userDiary.activitySeq.eq(activity.activitySeq))
                .where(condition)
                .groupBy(userDiary.activitySeq)
                .fetch()
                .stream()
                .map(t -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("actNm", t.get(activity.actNm));
                    m.put("cnt", t.get(activity.activitySeq.count()));
                    return m;
                })
                .collect(Collectors.toList());
    }
}
