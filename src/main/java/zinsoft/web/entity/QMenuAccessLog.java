package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.MenuAccessLog;


/**
 * QMenuAccessLog is a Querydsl query type for MenuAccessLog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMenuAccessLog extends EntityPathBase<MenuAccessLog> {

    private static final long serialVersionUID = 987458586L;

    public static final QMenuAccessLog menuAccessLog = new QMenuAccessLog("menuAccessLog");

    public final DateTimePath<java.util.Date> accessDtm = createDateTime("accessDtm", java.util.Date.class);

    public final NumberPath<Long> menuAccessLogSeq = createNumber("menuAccessLogSeq", Long.class);

    public final StringPath menuId = createString("menuId");

    public final StringPath note = createString("note");

    public final StringPath remoteAddr = createString("remoteAddr");

    public final StringPath userId = createString("userId");

    public QMenuAccessLog(String variable) {
        super(MenuAccessLog.class, forVariable(variable));
    }

    public QMenuAccessLog(Path<? extends MenuAccessLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMenuAccessLog(PathMetadata metadata) {
        super(MenuAccessLog.class, metadata);
    }

}

