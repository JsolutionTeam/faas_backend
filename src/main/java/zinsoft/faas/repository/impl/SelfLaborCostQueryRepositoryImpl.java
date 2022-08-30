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
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.faas.dto.SelfLaborCostDto;
import zinsoft.faas.entity.QSelfLaborCost;
import zinsoft.faas.repository.SelfLaborCostQueryRepository;
import zinsoft.util.Constants;

@RequiredArgsConstructor
public class SelfLaborCostQueryRepositoryImpl implements SelfLaborCostQueryRepository {

    private final JPAQueryFactory query;

    private QBean<SelfLaborCostDto> allFields = null;

    private final QSelfLaborCost selfLaborCost = QSelfLaborCost.selfLaborCost;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QSelfLaborCost.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QSelfLaborCost.selfLaborCost);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        allFields = Projections.fields(SelfLaborCostDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public Page<SelfLaborCostDto> page(Map<String, Object> search, Pageable pageable) {

        JPQLQuery<SelfLaborCostDto> jpqQuery = query.select(allFields)
                                        .from(selfLaborCost)
                                        .where(queryCondition(search))
                                        .orderBy(selfLaborCost.year.desc());

        QueryResults<SelfLaborCostDto> result = jpqQuery.offset(pageable.getOffset())
                                            .limit(pageable.getPageSize())
                                            .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<SelfLaborCostDto> list(Map<String, Object> search) {

        JPQLQuery<SelfLaborCostDto> jpqQuery = query.select(allFields)
                                        .from(selfLaborCost)
                                        .where(queryCondition(search))
                                        .orderBy(selfLaborCost.year.desc());

        List<SelfLaborCostDto> result = jpqQuery.fetch();

        return result;
    }

    private BooleanExpression queryCondition(Map<String, Object> search) {

        BooleanExpression condition = selfLaborCost.statusCd.eq(Constants.STATUS_CD_NORMAL);

        String keyword = (String)search.get("keyword");
        if(StringUtils.isNotBlank(keyword)) {
            String field = (String)search.get("field");
            if(StringUtils.isNotBlank(field) && field.equals("year")) {
                condition = condition.and(selfLaborCost.year.contains(keyword));
            }
        }

        return condition;
    }

    @Override
    public List<String> yearList() {
        List<String> result = new ArrayList<String>();
        BooleanExpression condition = selfLaborCost.statusCd.eq(Constants.STATUS_CD_NORMAL);
        /*
         * JPQLQuery<SelfLaborCostDto> jpqQuery = query.select(allFields)
         * .from(selfLaborCost) .where(queryCondition(search))
         * .orderBy(selfLaborCost.year.desc());
         *
         * List<SelfLaborCostDto> result = jpqQuery.fetch();
         */
        JPAQuery<String> jpqQuery = query.select(selfLaborCost.year.min()).from(selfLaborCost).where(condition);
        result.addAll(jpqQuery.fetch());
        jpqQuery = query.select(selfLaborCost.year.max()).from(selfLaborCost).where(condition);
        result.addAll(jpqQuery.fetch());

        return result;
    }


}
