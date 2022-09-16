package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.ApiAccessLog;


/**
 * QApiAccessLog is a Querydsl query type for ApiAccessLog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QApiAccessLog extends EntityPathBase<ApiAccessLog> {

    private static final long serialVersionUID = 1530879917L;

    public static final QApiAccessLog apiAccessLog = new QApiAccessLog("apiAccessLog");

    public final DateTimePath<java.util.Date> accessDtm = createDateTime("accessDtm", java.util.Date.class);

    public final NumberPath<Long> apiAccessLogSeq = createNumber("apiAccessLogSeq", Long.class);

    public final StringPath method = createString("method");

    public final StringPath note = createString("note");

    public final StringPath path = createString("path");

    public final StringPath remoteAddr = createString("remoteAddr");

    public final StringPath userId = createString("userId");

    public QApiAccessLog(String variable) {
        super(ApiAccessLog.class, forVariable(variable));
    }

    public QApiAccessLog(Path<? extends ApiAccessLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QApiAccessLog(PathMetadata metadata) {
        super(ApiAccessLog.class, metadata);
    }

}

