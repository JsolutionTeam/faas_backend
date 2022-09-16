package zinsoft.web.common.repository.impl;

import java.util.List;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.util.Constants;
import zinsoft.util.LocaleUtil;
import zinsoft.web.common.dto.CodeDto;
import zinsoft.web.entity.QCode;
import zinsoft.web.common.repository.CodeQueryRepository;

@RequiredArgsConstructor
public class CodeQueryRepositoryImpl implements CodeQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<CodeDto> list(String codeId, String withNormalize) {
        /*
        <bind name="LANG" value="@zinsoft.util.LocaleUtil@lang()" />
          SELECT a.code_id,
                 a.code_val,
                <choose>
                    <when test="LANG == 'ko'">
                        a.code_nm,
                    </when>
                    <otherwise>
                        IFNULL(a.code_eng_nm, a.code_nm) code_nm,
                    </otherwise>
                </choose>
                 a.up_code_val,
                 a.expr_seq
            FROM tb_code a
           WHERE a.code_id = #{codeId}
        ORDER BY a.code_id, a.expr_seq, a.code_val
        */
        final String lang = LocaleUtil.lang();
        final QCode code = QCode.code;
        Expression<CodeDto> proj = null;

        if ("ko".equals(lang)) {
            if (Constants.YN_YES.equals(withNormalize)) {
                proj = Projections.bean(CodeDto.class, code.codeId, code.codeVal, code.codeNm.trim().toLowerCase().as("codeNm"), code.upCodeVal, code.exprSeq);
            } else {
                proj = Projections.bean(CodeDto.class, code.codeId, code.codeVal, code.codeNm, code.upCodeVal, code.exprSeq);
            }
        } else {
            if (Constants.YN_YES.equals(withNormalize)) {
                proj = Projections.bean(CodeDto.class, code.codeId, code.codeVal, code.codeEngNm.trim().toLowerCase().coalesce(code.codeNm).as("codeNm"), code.upCodeVal, code.exprSeq);
            } else {
                proj = Projections.bean(CodeDto.class, code.codeId, code.codeVal, code.codeEngNm.coalesce(code.codeNm).as("codeNm"), code.upCodeVal, code.exprSeq);
            }
        }

        return query.select(proj)
                .from(code)
                .where(code.codeId.eq(codeId))
                .orderBy(code.exprSeq.asc())
                .fetch();
    }

    @Override
    public List<CodeDto> listStartsWithCodeVal(String codeId, String codeVal) {
        final String lang = LocaleUtil.lang();
        final QCode code = QCode.code;
        Expression<CodeDto> proj = null;

        if ("ko".equals(lang)) {
            proj = Projections.bean(CodeDto.class, code.codeId, code.codeVal, code.codeNm, code.upCodeVal, code.exprSeq);
        } else {
            proj = Projections.bean(CodeDto.class, code.codeId, code.codeVal, code.codeEngNm.coalesce(code.codeNm).as("codeNm"), code.upCodeVal, code.exprSeq);
        }

        // @formatter:off
        return query.select(proj)
                .from(code)
                .where(code.codeId.eq(codeId),
                       code.codeVal.startsWith(codeVal))
                .orderBy(code.exprSeq.asc())
                .fetch();
        // @formatter:on
    }

}
