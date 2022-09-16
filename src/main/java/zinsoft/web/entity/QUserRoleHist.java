package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.UserRoleHist;


/**
 * QUserRoleHist is a Querydsl query type for UserRoleHist
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserRoleHist extends EntityPathBase<UserRoleHist> {

    private static final long serialVersionUID = -474861814L;

    public static final QUserRoleHist userRoleHist = new QUserRoleHist("userRoleHist");

    public final StringPath roleId = createString("roleId");

    public final StringPath userId = createString("userId");

    public final NumberPath<Long> userRoleHistSeq = createNumber("userRoleHistSeq", Long.class);

    public final StringPath workCd = createString("workCd");

    public final DateTimePath<java.util.Date> workDtm = createDateTime("workDtm", java.util.Date.class);

    public final StringPath workerId = createString("workerId");

    public QUserRoleHist(String variable) {
        super(UserRoleHist.class, forVariable(variable));
    }

    public QUserRoleHist(Path<? extends UserRoleHist> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserRoleHist(PathMetadata metadata) {
        super(UserRoleHist.class, metadata);
    }

}

