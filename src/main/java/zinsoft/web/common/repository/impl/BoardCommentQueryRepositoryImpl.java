package zinsoft.web.common.repository.impl;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.util.Constants;
import zinsoft.web.common.dto.BoardArticleDto;
import zinsoft.web.common.dto.BoardCommentDto;
import zinsoft.web.common.entity.QBoardComment;
import zinsoft.web.common.repository.BoardCommentQueryRepository;

@RequiredArgsConstructor
public class BoardCommentQueryRepositoryImpl implements BoardCommentQueryRepository {

    private final JPAQueryFactory query;

    private QBean<BoardCommentDto> allFields = null;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QBoardComment.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QBoardComment.boardComment);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        allFields = Projections.fields(BoardCommentDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public List<BoardCommentDto> list(Long articleSeq, Integer listOrder) {
        /*
          SELECT a.comment_seq,
                 a.reg_dtm,
                 a.update_dtm,
                 a.status_cd,
                 a.board_id,
                 a.article_seq,
                 a.list_order,
                 a.depth,
                 a.up_comment_seq,
                 a.user_id,
                 a.user_nm,
                 a.user_pwd,
                 a.email_addr,
                 a.content
            FROM tb_board_comment a
           WHERE     a.status_cd = 'N'
                 AND a.article_seq = #{search.articleSeq}
                    <if test="search.listOrder != null and search.listOrder > 0">
                        AND a.list_order > #{search.listOrder}
                    </if>
        ORDER BY a.list_order
         */
        final QBoardComment boardComment = QBoardComment.boardComment;
        BooleanExpression condition = boardComment.statusCd.eq(Constants.STATUS_CD_NORMAL)
                .and(boardComment.articleSeq.eq(articleSeq));

        if (listOrder != null && listOrder > 0) {
            condition = condition.and(boardComment.listOrder.gt(listOrder));
        }

        // @formatter:off
        return query.select(allFields)
                .from(boardComment)
                .where(condition)
                .orderBy(boardComment.listOrder.asc())
                .fetch();
        // @formatter:on
    }

    @Override
    public Integer nextListOrder(Long articleSeq) {
        final QBoardComment boardComment = QBoardComment.boardComment;

        return query.select(boardComment.listOrder.max().coalesce(0))
                .from(boardComment)
                .where(boardComment.articleSeq.eq(articleSeq))
                .fetchOne() + 1;
    }

    @Override
    public void update(BoardCommentDto dto) {
        /*
        UPDATE tb_board_comment
           SET update_dtm = NOW(),
               content = #{content}
         WHERE     comment_seq = #{commentSeq}
               AND board_id = #{boardId}
               AND article_seq = #{articleSeq}
                <if test="userId != null and userId != ''">
                    AND user_id = #{userId}
                </if>
        */
        final QBoardComment boardComment = QBoardComment.boardComment;
        BooleanExpression condition = boardComment.commentSeq.eq(dto.getCommentSeq())
                .and(boardComment.boardId.eq(dto.getBoardId()))
                .and(boardComment.articleSeq.eq(dto.getArticleSeq()));

        if (StringUtils.isNotBlank(dto.getUserId())) {
            condition = condition.and(boardComment.userId.gt(dto.getUserId()));
        }

        query.update(boardComment)
                .set(boardComment.updateDtm, new Date())
                .set(boardComment.content, dto.getContent())
                .where(condition)
                .execute();
    }

    @Override
    public void updateListOrder(Long articleSeq, Integer listOrder) {
        /*
        UPDATE tb_board_comment
           SET list_order = list_order + 1
         WHERE     article_seq = #{articleSeq}
               AND list_order >= #{listOrder}
        */
        final QBoardComment boardComment = QBoardComment.boardComment;

        // @formatter:off
        query.update(boardComment)
                .set(boardComment.listOrder, boardComment.listOrder.add(1))
                .where(boardComment.articleSeq.eq(articleSeq),
                       boardComment.listOrder.goe(listOrder))
                .execute();
        // @formatter:on
    }

    @Override
    public void delete(BoardArticleDto articleDto) {
        /*
        UPDATE tb_board_comment
           SET update_dtm = NOW(),
               status_cd = 'D'
         WHERE     article_seq = #{articleSeq}
               AND board_id = #{boardId}
        */
        final QBoardComment boardComment = QBoardComment.boardComment;

        // @formatter:off
        query.update(boardComment)
                .set(boardComment.updateDtm, new Date())
                .set(boardComment.statusCd, Constants.STATUS_CD_DELETE)
                .where(boardComment.boardId.eq(articleDto.getBoardId()),
                       boardComment.articleSeq.eq(articleDto.getArticleSeq()))
                .execute();
        // @formatter:on
    }

}
