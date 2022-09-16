package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.UserAccessLog;


/**
 * QUserAccessLog is a Querydsl query type for UserAccessLog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserAccessLog extends EntityPathBase<UserAccessLog> {

    private static final long serialVersionUID = 29626158L;

    public static final QUserAccessLog userAccessLog = new QUserAccessLog("userAccessLog");

    public final DateTimePath<java.util.Date> accessDtm = createDateTime("accessDtm", java.util.Date.class);

    public final StringPath inOut = createString("inOut");

    public final StringPath note = createString("note");

    public final StringPath remoteAddr = createString("remoteAddr");

    public final StringPath successYn = createString("successYn");

    public final NumberPath<Long> userAccessLogSeq = createNumber("userAccessLogSeq", Long.class);

    public final StringPath userAgent = createString("userAgent");

    public final StringPath userId = createString("userId");

    public QUserAccessLog(String variable) {
        super(UserAccessLog.class, forVariable(variable));
    }

    public QUserAccessLog(Path<? extends UserAccessLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserAccessLog(PathMetadata metadata) {
        super(UserAccessLog.class, metadata);
    }

}

