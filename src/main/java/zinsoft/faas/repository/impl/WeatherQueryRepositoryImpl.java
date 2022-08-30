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
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.faas.dto.WeatherDto;
import zinsoft.faas.entity.QWeather;
import zinsoft.faas.repository.WeatherQueryRepository;
import zinsoft.web.common.entity.QCode;
import zinsoft.web.common.entity.QUserInfo;

@RequiredArgsConstructor
public class WeatherQueryRepositoryImpl implements WeatherQueryRepository  {

    private final JPAQueryFactory query;

    private QBean<WeatherDto> allFields = null;

    private final QWeather weather = QWeather.weather;
    private final QCode code = QCode.code;
    private final QUserInfo userInfo = QUserInfo.userInfo;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QWeather.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QWeather.weather);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        // @formatter:off
//        fieldList.add(
//                ExpressionUtils.as(
//                    JPAExpressions.select(crop.exprNm)
//                        .from(crop)
//                        .where(crop.cropSeq.eq(userShip.cropSeq)),
//                    "cropNm"));
        //fieldList.add( userCrop.aliasNm.coalesce(crop.exprNm).as("cropNm") );
        //fieldList.add( crop.exprNm.as("cropNm") );
        // @formatterLon

        allFields = Projections.fields(WeatherDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public WeatherDto get(String baseDate, String stnCode) {

        BooleanExpression condition = weather.date.stringValue().startsWith(baseDate);


        return query.select(allFields)
                .from(weather)
                .where(condition)
                .orderBy(weather.date.desc())
                .fetchFirst();
    }

}
