package zinsoft.web.common.repository.impl;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.util.Constants;
import zinsoft.web.common.dto.BoardFileDto;
import zinsoft.web.common.repository.BoardFileQueryRepository;
import zinsoft.web.entity.QBoardFile;
import zinsoft.web.entity.QFileInfo;

@RequiredArgsConstructor
public class BoardFileQueryRepositoryImpl implements BoardFileQueryRepository {

    private final JPAQueryFactory query;

    private QBean<BoardFileDto> allFields = null;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QBoardFile.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QBoardFile.boardFile);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        final QFileInfo fileInfo = QFileInfo.fileInfo;
        fieldList.add(fileInfo.fileSize);
        fieldList.add(fileInfo.fileNm);

        allFields = Projections.fields(BoardFileDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public List<BoardFileDto> list(Long articleSeq) {
        /*
        SELECT a.file_seq,
               a.board_id,
               a.article_seq,
               b.saved_nm,
               b.file_size,
               b.file_nm,
               b.content_type
          FROM tb_board_file a, tb_file_info b
         WHERE     a.file_seq = b.file_seq
               AND a.article_seq = #{search.articleSeq}
               AND b.status_cd = 'N'
        */
        final QBoardFile boardFile = QBoardFile.boardFile;
        final QFileInfo fileInfo = QFileInfo.fileInfo;

        // @formatter:off
        return query.select(allFields)
                .from(boardFile)
                    .join(fileInfo)
                        .on(fileInfo.fileSeq.eq(boardFile.fileSeq),
                            fileInfo.statusCd.eq(Constants.STATUS_CD_NORMAL))
                .where(boardFile.articleSeq.eq(articleSeq))
                .fetch();
        // @formatter:on
    }

    @Override
    public BoardFileDto get(Long fileSeq) {
        /*
        SELECT a.board_id,
               a.article_seq,
               a.file_seq,
               b.saved_nm,
               b.file_size,
               b.file_nm,
               b.content_type
          FROM tb_board_file a, tb_file_info b
         WHERE     a.file_seq = b.file_seq
               AND a.file_seq = #{fileSeq}
        */
        final QBoardFile boardFile = QBoardFile.boardFile;
        final QFileInfo fileInfo = QFileInfo.fileInfo;

        // @formatter:off
        return query.select(allFields)
                .from(boardFile)
                    .join(fileInfo)
                        .on(fileInfo.fileSeq.eq(boardFile.fileSeq),
                            fileInfo.statusCd.eq(Constants.STATUS_CD_NORMAL))
                .where(boardFile.fileSeq.eq(fileSeq))
                .fetchOne();
        // @formatter:on
    }

    @Override
    public long delete(Long articleSeq, List<Long> fileSeqList) {
        /*
        DELETE FROM tb_board_file
              WHERE file_seq IN
                    <foreach item="fileSeq" index="index" collection="fileSeqList" open="(" separator="," close=")">
                        #{fileSeq}
                    </foreach>
        */
        final QBoardFile boardFile = QBoardFile.boardFile;

        // @formatter:off
        return query.delete(boardFile)
                .where(boardFile.articleSeq.eq(articleSeq),
                       boardFile.fileSeq.in(fileSeqList))
                .execute();
        // @formatter:on
    }

}
