package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.BoardComment;


/**
 * QBoardComment is a Querydsl query type for BoardComment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBoardComment extends EntityPathBase<BoardComment> {

    private static final long serialVersionUID = 1756191232L;

    public static final QBoardComment boardComment = new QBoardComment("boardComment");

    public final NumberPath<Long> articleSeq = createNumber("articleSeq", Long.class);

    public final StringPath boardId = createString("boardId");

    public final NumberPath<Long> commentSeq = createNumber("commentSeq", Long.class);

    public final StringPath content = createString("content");

    public final NumberPath<Short> depth = createNumber("depth", Short.class);

    public final StringPath emailAddr = createString("emailAddr");

    public final NumberPath<Integer> listOrder = createNumber("listOrder", Integer.class);

    public final DateTimePath<java.util.Date> regDtm = createDateTime("regDtm", java.util.Date.class);

    public final StringPath statusCd = createString("statusCd");

    public final NumberPath<Long> upCommentSeq = createNumber("upCommentSeq", Long.class);

    public final DateTimePath<java.util.Date> updateDtm = createDateTime("updateDtm", java.util.Date.class);

    public final StringPath userId = createString("userId");

    public final StringPath userNm = createString("userNm");

    public final StringPath userPwd = createString("userPwd");

    public QBoardComment(String variable) {
        super(BoardComment.class, forVariable(variable));
    }

    public QBoardComment(Path<? extends BoardComment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardComment(PathMetadata metadata) {
        super(BoardComment.class, metadata);
    }

}

