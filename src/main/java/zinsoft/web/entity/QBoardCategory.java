package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.BoardCategory;


/**
 * QBoardCategory is a Querydsl query type for BoardCategory
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBoardCategory extends EntityPathBase<BoardCategory> {

    private static final long serialVersionUID = -739719811L;

    public static final QBoardCategory boardCategory = new QBoardCategory("boardCategory");

    public final StringPath boardId = createString("boardId");

    public final StringPath catNm = createString("catNm");

    public final NumberPath<Long> catSeq = createNumber("catSeq", Long.class);

    public final NumberPath<Short> exprSeq = createNumber("exprSeq", Short.class);

    public final DateTimePath<java.util.Date> regDtm = createDateTime("regDtm", java.util.Date.class);

    public final StringPath statusCd = createString("statusCd");

    public final DateTimePath<java.util.Date> updateDtm = createDateTime("updateDtm", java.util.Date.class);

    public QBoardCategory(String variable) {
        super(BoardCategory.class, forVariable(variable));
    }

    public QBoardCategory(Path<? extends BoardCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardCategory(PathMetadata metadata) {
        super(BoardCategory.class, metadata);
    }

}

