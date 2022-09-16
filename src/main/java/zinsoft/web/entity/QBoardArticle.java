package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.BoardArticle;


/**
 * QBoardArticle is a Querydsl query type for BoardArticle
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBoardArticle extends EntityPathBase<BoardArticle> {

    private static final long serialVersionUID = 73414807L;

    public static final QBoardArticle boardArticle = new QBoardArticle("boardArticle");

    public final NumberPath<Long> articleSeq = createNumber("articleSeq", Long.class);

    public final NumberPath<Integer> assentCnt = createNumber("assentCnt", Integer.class);

    public final StringPath boardId = createString("boardId");

    public final NumberPath<Long> catSeq = createNumber("catSeq", Long.class);

    public final NumberPath<Integer> commentCnt = createNumber("commentCnt", Integer.class);

    public final StringPath content = createString("content");

    public final NumberPath<Integer> dissentCnt = createNumber("dissentCnt", Integer.class);

    public final StringPath emailAddr = createString("emailAddr");

    public final NumberPath<Integer> fileCnt = createNumber("fileCnt", Integer.class);

    public final StringPath noticeYn = createString("noticeYn");

    public final NumberPath<Integer> readCnt = createNumber("readCnt", Integer.class);

    public final DateTimePath<java.util.Date> regDtm = createDateTime("regDtm", java.util.Date.class);

    public final StringPath statusCd = createString("statusCd");

    public final StringPath subject = createString("subject");

    public final DateTimePath<java.util.Date> updateDtm = createDateTime("updateDtm", java.util.Date.class);

    public final StringPath userId = createString("userId");

    public final StringPath userNm = createString("userNm");

    public final StringPath userPwd = createString("userPwd");

    public QBoardArticle(String variable) {
        super(BoardArticle.class, forVariable(variable));
    }

    public QBoardArticle(Path<? extends BoardArticle> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardArticle(PathMetadata metadata) {
        super(BoardArticle.class, metadata);
    }

}

