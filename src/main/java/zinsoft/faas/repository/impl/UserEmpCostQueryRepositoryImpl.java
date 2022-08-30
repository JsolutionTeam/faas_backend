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
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.faas.dto.UserEmpCostDto;
import zinsoft.faas.entity.QUserEmpCost;
import zinsoft.faas.repository.UserEmpCostQueryRepository;
import zinsoft.util.Constants;
import zinsoft.web.common.entity.QUserInfo;

@RequiredArgsConstructor
public class UserEmpCostQueryRepositoryImpl implements UserEmpCostQueryRepository {

    private final JPAQueryFactory query;

    private QBean<UserEmpCostDto> allFields = null;

    private final QUserEmpCost userEmpCost = QUserEmpCost.userEmpCost;
    private final QUserInfo userInfo = QUserInfo.userInfo;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserEmpCost.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QUserEmpCost.userEmpCost);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        fieldList.add(userInfo.userNm);

        allFields = Projections.fields(UserEmpCostDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public UserEmpCostDto get(String userId, Long userEmpCostSeq) {

        BooleanExpression condition = userEmpCost.statusCd.eq(Constants.STATUS_CD_NORMAL);
        //.and(code.codeId.eq("CROP_S_CD"));

        //System.out.println(dto.toString());

        if (userEmpCostSeq != null && userEmpCostSeq > 0) {
            condition = condition.and(userEmpCost.userEmpCostSeq.eq(userEmpCostSeq));
        }

        if (StringUtils.isNoneBlank(userId)) {
            condition = condition.and(userEmpCost.userId.eq(userId));
        }

        return query.select(allFields)
                .from(userEmpCost)
                .join(userInfo)
                .on(userEmpCost.userId.eq(userInfo.userId))
                .where(condition)
                .fetchOne();
    }

    @Override
    public Page<UserEmpCostDto> page(Map<String, Object> search, Pageable pageable) {

        QueryResults<UserEmpCostDto> result = query.select(allFields)
                .from(userEmpCost)
                .join(userInfo)
                .on(userEmpCost.userId.eq(userInfo.userId))
                .where(queryCondition(search))
                .orderBy(userEmpCost.year.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<UserEmpCostDto> list(Map<String, Object> search) {

        return query.select(allFields)
                .from(userEmpCost)
                .join(userInfo)
                .on(userEmpCost.userId.eq(userInfo.userId))
                .where(queryCondition(search))
                .orderBy(userEmpCost.year.desc())
                .fetch();
    }

    public BooleanExpression queryCondition(Map<String, Object> search) {
        BooleanExpression condition = userEmpCost.statusCd.eq(Constants.STATUS_CD_NORMAL);

        String userId = (String) search.get("userId");
        if (StringUtils.isNotBlank(userId)) {
            condition = condition.and(userEmpCost.userId.eq(userId));
        }

        String keyword = (String) search.get("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            String field = (String) search.get("field");
            if (StringUtils.isNotBlank(field) && field.equals("year")) {
                condition = condition.and(userEmpCost.year.contains(keyword));
            }
        }

        return condition;
    }

    @Override
    public List<String> yearList(String userId) {
        List<String> result = new ArrayList<String>();
        BooleanExpression condition = userEmpCost.statusCd.eq(Constants.STATUS_CD_NORMAL);

        if (StringUtils.isNotBlank(userId)) {
            condition = condition.and(userEmpCost.userId.eq(userId));
        }
        /*
         * JPQLQuery<SelfLaborCostDto> jpqQuery = query.select(allFields)
         * .from(selfLaborCost) .where(queryCondition(search))
         * .orderBy(selfLaborCost.year.desc());
         *
         * List<SelfLaborCostDto> result = jpqQuery.fetch();
         */
        JPAQuery<String> jpqQuery = query.select(userEmpCost.year.min()).from(userEmpCost).where(condition);
        result.addAll(jpqQuery.fetch());
        jpqQuery = query.select(userEmpCost.year.max()).from(userEmpCost).where(condition);
        result.addAll(jpqQuery.fetch());

        return result;
    }

}
