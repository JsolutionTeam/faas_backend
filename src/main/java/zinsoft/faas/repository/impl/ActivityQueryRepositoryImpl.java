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
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.faas.dto.ActivityDto;
import zinsoft.faas.entity.QActivity;
import zinsoft.faas.repository.ActivityQueryRepository;
import zinsoft.util.Constants;
import zinsoft.web.common.entity.QCode;

@RequiredArgsConstructor
public class ActivityQueryRepositoryImpl implements ActivityQueryRepository {

    private final JPAQueryFactory query;

    private QBean<ActivityDto> allFields = null;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QActivity.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QActivity.activity);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        final QActivity activity = QActivity.activity;
        final QCode code = QCode.code;

        // @formatter:off
//        fieldList.add(
//            ExpressionUtils.as(
//                JPAExpressions.select(code.codeNm)
//                    .from(code)
//                    .where(code.codeId.eq("MARK_T_CD"),
//                           code.codeVal.eq(activity.markTCd)),
//                "inOutNm"));
        // @formatterLon

        allFields = Projections.fields(ActivityDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public Page<ActivityDto> page(Map<String, Object> search, Pageable pageable) {
        /*
        SELECT *
            FROM (SELECT ROWNUM AS RNUM, z.*
                   FROM (SELECT
                              a.activity_seq,
                              a.reg_dtm,
                              a.update_dtm,
                              a.status_cd,
                              a.crop_a_cd,
                              a.act_nm,
                              a.expr_yn,
                              a.expr_seq,
                              a.mark_t_cd,
                              (SELECT   x.code_nm
                                 FROM   tf_code x
                                WHERE   x.code_id = 'MARK_T_CD' AND x.code_val = a.mark_t_cd)
                               mark_t_cd_nm
                         FROM tf_activity a
                        WHERE a.status_cd = 'N'
                              <if test="cond.keyword != null and cond.keyword != ''">
                                   <if test='cond.field == "actNm"'>
                                       and a.act_nm LIKE '%' || #{cond.keyword} || '%'
                                   </if>
                              </if>
                     ORDER BY a.reg_dtm DESC, a.expr_seq DESC)z
         <![CDATA[
                 WHERE ROWNUM <= (#{start} + #{limit}))
         WHERE RNUM > #{start}
         ]]>
        */
        final QActivity activity = QActivity.activity;

        // @formatter:off
        QueryResults<ActivityDto> result = query.select(allFields)
                .from(activity)
                .where(condition(search))
                .orderBy(activity.regDtm.desc(), activity.exprSeq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        // @formatter:on

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression condition(Map<String, Object> search) {
        final QActivity activity = QActivity.activity;
        BooleanExpression condition = activity.statusCd.eq(Constants.STATUS_CD_NORMAL);

        String keyword = (String) search.get("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            switch ((String) search.get("field")) {
                case "actNm":
                    condition = condition.and(activity.actNm.contains(keyword));
                    break;
            }
        }

        return condition;
    }

}
