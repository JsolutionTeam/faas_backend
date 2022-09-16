package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.Board;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -490114209L;

    public static final QBoard board = new QBoard("board");

    public final StringPath boardId = createString("boardId");

    public final StringPath boardNm = createString("boardNm");

    public final StringPath pushEmails = createString("pushEmails");

    public final StringPath pushRoles = createString("pushRoles");

    public final StringPath pushYn = createString("pushYn");

    public final DateTimePath<java.util.Date> regDtm = createDateTime("regDtm", java.util.Date.class);

    public final StringPath rolesDelete = createString("rolesDelete");

    public final StringPath rolesDetail = createString("rolesDetail");

    public final StringPath rolesInsert = createString("rolesInsert");

    public final StringPath rolesList = createString("rolesList");

    public final StringPath rolesUpdate = createString("rolesUpdate");

    public final StringPath statusCd = createString("statusCd");

    public final DateTimePath<java.util.Date> updateDtm = createDateTime("updateDtm", java.util.Date.class);

    public QBoard(String variable) {
        super(Board.class, forVariable(variable));
    }

    public QBoard(Path<? extends Board> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoard(PathMetadata metadata) {
        super(Board.class, metadata);
    }

}

