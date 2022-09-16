package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.BoardFile;


/**
 * QBoardFile is a Querydsl query type for BoardFile
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBoardFile extends EntityPathBase<BoardFile> {

    private static final long serialVersionUID = -1338763909L;

    public static final QBoardFile boardFile = new QBoardFile("boardFile");

    public final NumberPath<Long> articleSeq = createNumber("articleSeq", Long.class);

    public final StringPath boardId = createString("boardId");

    public final NumberPath<Long> fileSeq = createNumber("fileSeq", Long.class);

    public QBoardFile(String variable) {
        super(BoardFile.class, forVariable(variable));
    }

    public QBoardFile(Path<? extends BoardFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardFile(PathMetadata metadata) {
        super(BoardFile.class, metadata);
    }

}

