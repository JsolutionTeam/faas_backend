package zinsoft.faas.repository.impl;

import java.lang.reflect.Modifier;
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
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.faas.dto.UserChemicalDto;
import zinsoft.faas.entity.QUserChemical;
import zinsoft.faas.repository.UserChemicalQueryRepository;
import zinsoft.util.Constants;
import zinsoft.web.common.entity.QCode;
import zinsoft.web.common.entity.QUserInfo;

@RequiredArgsConstructor
public class UserChemicalQueryRepositoryImpl implements UserChemicalQueryRepository  {

    private final JPAQueryFactory query;

    private QBean<UserChemicalDto> allFields = null;

    private final QUserChemical userChemical = QUserChemical.userChemical;
    private final QCode code = QCode.code;
    private final QUserInfo userInfo = QUserInfo.userInfo;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserChemical.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QUserChemical.userChemical);
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
                    .where(code.codeId.eq("CHEMICAL_T_CD"),
                           code.codeVal.eq(userChemical.chemicalTCd)),
                "chemicalTCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                    JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("PACK_T_CD"),
                               code.codeVal.eq(userChemical.packTCd)),
                    "packTCdNm"));
        fieldList.add( userInfo.userNm.as("userNm") );
        // @formatterLon

        allFields = Projections.fields(UserChemicalDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public UserChemicalDto get(UserChemicalDto dto) {

        BooleanExpression condition = userChemical.statusCd.eq(Constants.STATUS_CD_NORMAL);
                                        //.and(code.codeId.eq("CROP_S_CD"));

        //System.out.println(dto.toString());

        if (dto.getUserChemicalSeq() != null && dto.getUserChemicalSeq() > 0) {
            condition = condition.and(userChemical.userChemicalSeq.eq(dto.getUserChemicalSeq()));
        }

        return query.select(allFields)
                .from(userChemical)
                .join(userInfo)
                    .on(userChemical.userId.eq(userInfo.userId))
                .where(condition)
                .fetchOne();
    }

    @Override
    public Page<UserChemicalDto> page(Map<String, Object> search, Pageable pageable) {

        JPQLQuery<UserChemicalDto> jpqQuery = query.select(allFields)
                                        .from(userChemical)
                                        .join(userInfo)
                                            .on(userChemical.userId.eq(userInfo.userId))
                                        .where(pageCondition(search))
                                        .orderBy(orderBy(search));

        QueryResults<UserChemicalDto> result = jpqQuery.offset(pageable.getOffset())
                                            .limit(pageable.getPageSize())
                                            .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<UserChemicalDto> list(Map<String, Object> search) {

        JPQLQuery<UserChemicalDto> jpqQuery = query.select(allFields)
                                        .from(userChemical)
                                        .join(userInfo)
                                            .on(userChemical.userId.eq(userInfo.userId))
                                        .where(pageCondition(search))
                                        .orderBy(orderBy(search));

        List<UserChemicalDto> result = jpqQuery.fetch();

        return result;
    }

    private BooleanExpression pageCondition(Map<String, Object> search) {

        BooleanExpression condition = userChemical.statusCd.eq(Constants.STATUS_CD_NORMAL);

        //System.out.println(search.toString());

        String userId = (String)search.get("userId");
        if(StringUtils.isNotBlank(userId)) {
            condition = condition.and(userChemical.userId.eq(userId));
        }

        String keyword = (String)search.get("keyword");
        if(StringUtils.isNotBlank(keyword)) {
            switch ((String)search.get("field")) {
                case "userNm":
                    condition = condition.and(userInfo.userNm.contains(keyword));
                    break;
                case "userId":
                    condition = condition.and(userChemical.userId.contains(keyword));
                    break;
                case "userChemicalNm":
                    condition = condition.and(userChemical.userChemicalNm.contains(keyword));
                    break;
                case "insect":
                    condition = condition.and(userChemical.insect.contains(keyword));
                    break;
                case "productNm":
                    condition = condition.and(userChemical.productNm.contains(keyword));
                    break;
                case "chemicalNm":
                    condition = condition.and(userChemical.chemicalNm.contains(keyword));
                    break;
                case "makerNm":
                    condition = condition.and(userChemical.makerNm.contains(keyword));
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
                    return userChemical.userChemicalSeq.asc();

            }
        }

        return userChemical.userChemicalSeq.desc();
    }
}
