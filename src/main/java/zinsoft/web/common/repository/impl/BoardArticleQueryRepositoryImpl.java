package zinsoft.web.common.repository.impl;

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
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.util.Constants;
import zinsoft.web.common.dto.BoardArticleDto;

import zinsoft.web.common.repository.BoardArticleQueryRepository;
import zinsoft.web.entity.QBoardArticle;
import zinsoft.web.entity.QBoardCategory;
import zinsoft.web.entity.QBoardComment;

@RequiredArgsConstructor
public class BoardArticleQueryRepositoryImpl implements BoardArticleQueryRepository {

    private final JPAQueryFactory query;

    private QBean<BoardArticleDto> allFields = null;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QBoardArticle.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QBoardArticle.boardArticle);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        final QBoardCategory boardCategory = QBoardCategory.boardCategory;
        fieldList.add(boardCategory.catNm);

        allFields = Projections.fields(BoardArticleDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public Page<BoardArticleDto> page(Map<String, Object> search, Pageable pageable) {
        /*
          SELECT a.article_seq,
                 a.reg_dtm,
                 a.update_dtm,
                 a.status_cd,
                 a.board_id,
                 a.notice_yn,
                 a.user_id,
                 a.user_nm,
                 a.user_pwd,
                 a.email_addr,
                 a.subject,
                 a.content,
                 a.comment_cnt,
                 a.file_cnt,
                 a.read_cnt,
                 a.assent_cnt,
                 a.dissent_cnt,
                 b.cat_nm
            FROM tb_board_article a LEFT JOIN tb_board_category b ON a.cat_seq = b.cat_seq
           WHERE     a.status_cd = 'N'
                 AND a.notice_yn = 'N'
                 AND a.board_id = #{search.boardId}
                <if test="search.keyword != null and search.keyword != ''">
                    <choose>
                        <when test="search.field == 'subject'">
                            AND a.subject LIKE CONCAT('%', #{search.keyword}, '%')
                        </when>
                        <when test="search.field == 'content'">
                            AND a.content LIKE CONCAT('%', #{search.keyword}, '%')
                        </when>
                        <when test="search.field == 'userId'">
                            AND a.user_id LIKE CONCAT('%', #{search.keyword}, '%')
                        </when>
                        <when test="search.field == 'userNm'">
                            AND a.user_nm LIKE CONCAT('%', #{search.keyword}, '%')
                        </when>
                    </choose>
                </if>
        ORDER BY a.article_seq DESC
           LIMIT #{start}, #{numOfRows}
        */
        final QBoardArticle boardArticle = QBoardArticle.boardArticle;
        final QBoardCategory boardCategory = QBoardCategory.boardCategory;
        BooleanExpression condition = boardArticle.statusCd.eq(Constants.STATUS_CD_NORMAL)
                .and(boardArticle.boardId.eq((String) search.get("boardId")))
                .and(boardArticle.noticeYn.eq(Constants.YN_NO));

        String keyword = (String) search.get("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            switch ((String) search.get("field")) {
                case "subject":
                    condition = condition.and(boardArticle.subject.contains(keyword));
                    break;
                case "content":
                    condition = condition.and(boardArticle.content.contains(keyword));
                    break;
                case "userId":
                    condition = condition.and(boardArticle.userId.contains(keyword));
                    break;
                case "userNm":
                    condition = condition.and(boardArticle.userNm.contains(keyword));
                    break;
            }
        }

        // @formatter:off
        QueryResults<BoardArticleDto> result =
                query.select(allFields)
                    .from(boardArticle)
                        .leftJoin(boardCategory)
                            .on(boardArticle.catSeq.eq(boardCategory.catSeq),
                                boardCategory.statusCd.eq(Constants.STATUS_CD_NORMAL))
                    .where(condition)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(boardArticle.articleSeq.desc())
                    .fetchResults();
        // @formatter:on

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public void updateCommentCnt(Long articleSeq) {
        /*
        UPDATE tb_board_article
           SET comment_cnt = (SELECT COUNT (*) FROM tb_board_comment WHERE status_cd = 'N' AND article_seq = #{articleSeq})
         WHERE article_seq = #{articleSeq}
         */
        final QBoardArticle boardArticle = QBoardArticle.boardArticle;
        final QBoardComment boardComment = QBoardComment.boardComment;

        // @formatter:off
        query.update(boardArticle)
                .set(boardArticle.commentCnt,
                        JPAExpressions.select(Wildcard.count.intValue())
                                .from(boardComment)
                                .where(boardComment.statusCd.eq(Constants.STATUS_CD_NORMAL),
                                       boardComment.articleSeq.eq(articleSeq)))
                .where(boardArticle.articleSeq.eq(articleSeq))
                .execute();
        // @formatter:on
    }

}
