package zinsoft.faas.repository.impl;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.faas.dto.CropActivityDto;
import zinsoft.faas.entity.QActivity;
import zinsoft.faas.entity.QCrop;
import zinsoft.faas.entity.QCropActivity;
import zinsoft.faas.repository.CropActivityQueryRepository;
import zinsoft.util.Constants;

@RequiredArgsConstructor
public class CropActivityQueryRepositoryImpl implements CropActivityQueryRepository {

    private final JPAQueryFactory query;

    private QBean<CropActivityDto> allFields = null;

    private final QCropActivity cropActivity = QCropActivity.cropActivity;
    private final QActivity activity = QActivity.activity;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QCropActivity.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QCropActivity.cropActivity);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        // final QCode code = QCode.code;

        fieldList.add(activity.actNm);

        allFields = Projections.fields(CropActivityDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public CropActivityDto get(Long cropActivitySeq) {
        BooleanExpression condition = cropActivity.statusCd.eq(Constants.STATUS_CD_NORMAL);

        condition = condition.and(activity.statusCd.eq(Constants.STATUS_CD_NORMAL));
        condition = condition.and(cropActivity.cropActivitySeq.eq(cropActivitySeq));

        // @formatter:off
        CropActivityDto result = query.select(allFields)
                .from(cropActivity)
                .join(activity)
                .on(cropActivity.activitySeq.eq(activity.activitySeq))
                .where(condition)
                .fetchOne();
        // @formatter:on

        return result;
    }

    @Override
    public List<CropActivityDto> listByActivityTCd(Long activityTCd) {
        BooleanExpression condition = cropActivity.statusCd.eq(Constants.STATUS_CD_NORMAL);

        condition = condition.and(activity.statusCd.eq(Constants.STATUS_CD_NORMAL));
        condition = condition.and(cropActivity.activityTCd.eq(activityTCd));

        // @formatter:off
        JPQLQuery<CropActivityDto> jpqQuery = query.select(allFields)
                                                    .from(cropActivity)
                                                    .join(activity)
                                                    .on(cropActivity.activitySeq.eq(activity.activitySeq))
                                                    .where(condition)
                                                    .orderBy(cropActivity.exprSeq.asc());
        // @formatter:on
        List<CropActivityDto> result = jpqQuery.fetch();
        return result;
    }

    @Override
    public List<CropActivityDto> listByCropSeq(Long cropSeq) {
        final QCrop crop = QCrop.crop;

        BooleanExpression condition = cropActivity.statusCd.eq(Constants.STATUS_CD_NORMAL);
        condition = condition.and(activity.statusCd.eq(Constants.STATUS_CD_NORMAL));
        condition = condition.and(crop.cropSeq.eq(cropSeq));

        // @formatter:off
        JPQLQuery<CropActivityDto> jpqQuery = query.select(allFields)
                                                    .from(cropActivity)
//                                                    .join(crop)
//                                                    .on(cropActivity.cropACd.eq(crop.cropACd))
                                                    .join(activity)
                                                    .on(cropActivity.activitySeq.eq(activity.activitySeq))
                                                    .where(condition);
        // @formatter:on
        List<CropActivityDto> result = jpqQuery.fetch();
        return result;
    }

}
