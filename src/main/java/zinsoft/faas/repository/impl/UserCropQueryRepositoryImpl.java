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
import zinsoft.faas.dto.UserCropDto;
import zinsoft.faas.entity.QCrop;
import zinsoft.faas.entity.QUserCrop;
import zinsoft.faas.repository.UserCropQueryRepository;
import zinsoft.util.Constants;
import zinsoft.web.common.entity.QCode;
import zinsoft.web.common.entity.QUserInfo;

@RequiredArgsConstructor
public class UserCropQueryRepositoryImpl implements UserCropQueryRepository  {

    private final JPAQueryFactory query;

    private QBean<UserCropDto> allFields = null;

    private final QUserCrop userCrop = QUserCrop.userCrop;
    private final QCrop crop = QCrop.crop;
    private final QCode code = QCode.code;
    private final QUserInfo userInfo = QUserInfo.userInfo;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserCrop.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QUserCrop.userCrop);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        // @formatter:off
        fieldList.add( userInfo.userNm.as("userNm") );
        fieldList.add( crop.cropACd.as("cropACd") );
        fieldList.add(
                ExpressionUtils.as(
                    JPAExpressions.select(code.codeNm)
                        .from(code)
                        .where(code.codeId.eq("CROP_S_CD"),
                               code.codeVal.eq(userCrop.cropSCd)),
                    "cropSCdNm"));

        //fieldList.add( userCrop.aliasNm.coalesce(crop.exprNm).as("cropNm") );
        fieldList.add( crop.exprNm.as("cropNm") );
        // @formatterLon

        allFields = Projections.fields(UserCropDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public UserCropDto get(UserCropDto dto) {

        BooleanExpression condition = userCrop.statusCd.eq(Constants.STATUS_CD_NORMAL);
                                        //.and(code.codeId.eq("CROP_S_CD"));

        //System.out.println(dto.toString());

        if (dto.getUserCropSeq() != null && dto.getUserCropSeq() > 0) {
            condition = condition.and(userCrop.userCropSeq.eq(dto.getUserCropSeq()));
        }
        if (StringUtils.isNotBlank(dto.getUserId())) {
            condition = condition.and(userCrop.userId.eq(dto.getUserId()));
        }
        if (dto.getCropSeq() != null && dto.getCropSeq() > 0) {
            condition = condition.and(userCrop.cropSeq.eq(dto.getCropSeq()));
        }

        return query.select(allFields)
                .from(userCrop)
                .join(crop)
                    .on(userCrop.cropSeq.eq(crop.cropSeq))
                .join(userInfo)
                    .on(userCrop.userId.eq(userInfo.userId))
                //.leftJoin(code) //code값은 subquery로 처리함
                //    .on(userCrop.cropSCd.eq(code.codeVal), code.codeId.eq("CROP_S_CD") )
                .where(condition)
                .fetchOne();

    }

    @Override
    public Page<UserCropDto> page(Map<String, Object> search, Pageable pageable) {

        JPQLQuery<UserCropDto> jpqQuery = query.select(allFields)
                                        .from(userCrop)
                                        .join(crop)
                                            .on(userCrop.cropSeq.eq(crop.cropSeq))
                                        .join(userInfo)
                                            .on(userCrop.userId.eq(userInfo.userId))
                                        .where(pageCondition(search))
                                        .orderBy(orderBy(search));

        QueryResults<UserCropDto> result = jpqQuery.offset(pageable.getOffset())
                                            .limit(pageable.getPageSize())
                                            .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<UserCropDto> list(Map<String, Object> search) {

        BooleanExpression condition = pageCondition(search);

        String year = (String)search.get("year");
        if(StringUtils.isNotBlank(year)) {
            condition = condition.and(userCrop.year.in(new String[] {year, "1900"}));
        }

        String assetYn = (String)search.get("assetYn");
        if(StringUtils.isNotBlank(assetYn)) {
            condition = condition.and(crop.assetYn.eq(assetYn));
        }

        String sYear = (String)search.get("sYear");
        String eYear = (String)search.get("eYear");
        if(StringUtils.isNotBlank(sYear)) {
            condition = condition.and( userCrop.year.between(sYear, eYear)
                                        .or(userCrop.year.eq("1900")) );
        }

        JPQLQuery<UserCropDto> jpqQuery = query.select(allFields)
                                        .from(userCrop)
                                        .join(crop)
                                            .on(userCrop.cropSeq.eq(crop.cropSeq))
                                        .join(userInfo)
                                            .on(userCrop.userId.eq(userInfo.userId))
                                        .where(condition)
                                        .orderBy(orderBy(search));

        List<UserCropDto> result = jpqQuery.fetch();

        return result;
    }

    @Override
    public long countByAliasNm(UserCropDto dto) {

        BooleanExpression condition = userCrop.statusCd.eq(Constants.STATUS_CD_NORMAL)
                                          .and(userCrop.userId.eq(dto.getUserId()));

        String aliasNm = dto.getAliasNm().trim().toLowerCase();
        condition = condition.and(userCrop.aliasNm.trim().toLowerCase().eq(aliasNm));

        if(dto.getUserCropSeq() > 0) {
            condition = condition.and(userCrop.userCropSeq.ne(dto.getUserCropSeq()));
        }

        return query.select(allFields)
                .from(userCrop)
                .where(condition)
                .fetchCount();
    }

    private BooleanExpression pageCondition(Map<String, Object> search) {

        BooleanExpression condition = userCrop.statusCd.eq(Constants.STATUS_CD_NORMAL);

        //System.out.println(search.toString());

        String exprNn = (String)search.get("exprYN");
        if(StringUtils.isNotBlank(exprNn)) {
            condition = condition.and(userCrop.exprYN.eq(exprNn));
        }
        String cropCd = (String)search.get("cropCd");
        if(StringUtils.isNotBlank(cropCd)) {
            if(cropCd.trim().equals("2")) {
                condition = condition.and(userCrop.cropSeq.goe(600000).and(userCrop.cropSeq.loe(899999)));
            }else {
                condition = condition.and(userCrop.cropSeq.lt(600000).or(userCrop.cropSeq.gt(899999)));
            }
        }
        String userId = (String)search.get("userId");
        if(StringUtils.isNotBlank(userId)) {
            condition = condition.and(userCrop.userId.eq(userId));
        }
        String keyword = (String)search.get("keyword");
        if(StringUtils.isNotBlank(keyword)) {
            switch ((String)search.get("field")) {
                case "userId":
                    condition = condition.and(userCrop.userId.contains(keyword));
                    break;
                case "cropNm":
                    condition = condition.and(crop.exprNm.contains(keyword));
                    break;
                case "mainKind":
                    condition = condition.and(userCrop.mainKind.contains(keyword));
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
                    return userCrop.userCropSeq.asc();

                case "aliasNm":
                    return userCrop.aliasNm == null ? crop.exprNm.asc() : userCrop.aliasNm.asc();
            }
        }

        return userCrop.userCropSeq.desc();
    }
}
