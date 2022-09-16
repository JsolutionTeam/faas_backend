package zinsoft.faas.repository.impl;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
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
import zinsoft.faas.dto.CropDto;
import zinsoft.faas.dto.QCropDto;
import zinsoft.faas.entity.QCrop;
import zinsoft.faas.entity.QCropActivity;
import zinsoft.faas.repository.CropQueryRepository;
import zinsoft.util.Constants;
import zinsoft.web.entity.QCode;

import static zinsoft.faas.entity.QCropSpecies.cropSpecies;

@RequiredArgsConstructor
public class CropQueryRepositoryImpl implements CropQueryRepository {

    private final JPAQueryFactory query;

    private QBean<CropDto> allFields = null;

    private final QCrop crop = QCrop.crop;
    private QCrop x = new QCrop("x");
    private QCode a = new QCode("a");
    private QCode b = new QCode("b");
    private QCode c = new QCode("c");
    private QCode d = new QCode("d");

    //private final QCode code = QCode.code;
    private final QCropActivity cropActivity = QCropActivity.cropActivity;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QCrop.class.getDeclaredFields())
                .filter(field -> !(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field -> {
                    try {
                        return (Expression<?>) field.get(QCrop.crop);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        // @formatter:off
        fieldList.add(a.codeNm.as("cropACdNm"));
        fieldList.add(b.codeNm.as("cropBCdNm"));
//        fieldList.add(crop.cropSeq.stringValue().substring(0, 4).as("cropCCd"));
        fieldList.add(c.codeNm.as("cropCCdNm"));
        fieldList.add(d.codeNm.as("cropSCdNm"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(cropActivity.activityTCdNm.max())
                                .from(cropActivity)
                                .where(cropActivity.activityTCd.eq(crop.activityTCd)),
                        "cropActNm"));
        fieldList.add(
                ExpressionUtils.as(
                        JPAExpressions.select(x.cropSeq.count())
                                .from(x)
                                .where(x.exprNm.eq(crop.exprNm)
                                        .and(x.statusCd.eq(Constants.STATUS_CD_NORMAL)))
                                .groupBy(x.exprNm)
                        , "cropCnt"));

        allFields = Projections.fields(CropDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public CropDto get(Long cropSeq) {

        return query.select(allFields)
                .from(crop)
                .leftJoin(a).on(crop.cropACd.eq(a.codeVal).and(a.codeId.eq("CROP_A_CD")))
                .leftJoin(b).on(crop.cropBCd.eq(b.codeVal).and(b.codeId.eq("CROP_B_CD")))
//                .leftJoin(c).on(Expressions.stringTemplate("SUBSTR({0}, 1,4)", crop.cropSeq).eq(c.codeVal).and(c.codeId.eq("CROP_C_CD")))
                .leftJoin(d).on(crop.cropSCd.eq(d.codeVal).and(d.codeId.eq("CROP_S_CD")))
                .where(crop.cropSeq.eq(cropSeq))
                .fetchOne();

    }

    @Override
    public Page<CropDto> page(Map<String, Object> search, Pageable pageable) {

        JPQLQuery<CropDto> jpqQuery = getCropList(search);

        QueryResults<CropDto> result = jpqQuery.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<CropDto> list(Map<String, Object> search) {
        return getCropList(search).fetch();
    }

    private JPAQuery<CropDto> getCropList(Map<String, Object> search){
        return query.select(new QCropDto(crop,
                        a.codeNm,
                        b.codeNm,
                        d.codeNm,
                        crop.count().as("cropCnt")
                ))
                .from(crop)
                .leftJoin(a).on(crop.cropACd.eq(a.codeVal).and(a.codeId.eq("CROP_A_CD")))
                .leftJoin(b).on(crop.cropBCd.eq(b.codeVal).and(b.codeId.eq("CROP_B_CD")))
                .leftJoin(d).on(crop.cropSCd.eq(d.codeVal).and(d.codeId.eq("CROP_S_CD")))
                // 처음부터 모든 데이터를 가져가면 너무 오래걸림. 셀렉트박스 선택 시 조회하도록 함.
//                .leftJoin(cropSpecies).on(crop.cropSeq.eq(cropSpecies.crop.cropSeq)).fetchJoin()
                .where(pageCondition(search))
                .groupBy(
                        crop.regDtm,
                        crop.cropACd,
                        crop.cropBCd,
                        crop.cropSeq)
                .orderBy(
                        crop.regDtm.desc(),
                        crop.cropACd.asc(),
                        crop.cropBCd.asc(),
                        crop.cropSeq.asc());
    }

    private BooleanExpression pageCondition(Map<String, Object> search) {

        BooleanExpression condition = crop.statusCd.eq(Constants.STATUS_CD_NORMAL);//pageCondition(search);

        if (search != null) {
            String keyword = (String) search.get("keyword");
            if (StringUtils.isNotBlank(keyword)) {
                String field = (String) search.get("field");
                switch (field) {
                    case "cropACdNm":
                        condition = condition.and(a.codeNm.contains(keyword));
                        break;
                    case "cropBCdNm":
                        condition = condition.and(b.codeNm.contains(keyword));
                        break;
                    case "cropCCdNm":
                        condition = condition.and(crop.exprNm.contains(keyword));
                        break;
                }
            }

            String cropNm = (String) search.get("cropNm");
            if (StringUtils.isNotBlank(cropNm)) {
                condition = condition.and(crop.exprNm.contains(cropNm));
            }

            String cropACd = (String) search.get("cropACd");
            if (StringUtils.isNotBlank(cropACd)) {
                condition = condition.and(crop.cropACd.eq(cropACd));
            }

            String statusCD = (String) search.get("statusCD");
            if (StringUtils.isNotBlank(statusCD)) {
                condition = condition.and(crop.statusCd.eq(statusCD));
            }
        }

        return condition;
    }

}
