package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.UserRememberMe;


/**
 * QUserRememberMe is a Querydsl query type for UserRememberMe
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserRememberMe extends EntityPathBase<UserRememberMe> {

    private static final long serialVersionUID = -1680753961L;

    public static final QUserRememberMe userRememberMe = new QUserRememberMe("userRememberMe");

    public final DateTimePath<java.util.Date> lastUsed = createDateTime("lastUsed", java.util.Date.class);

    public final StringPath series = createString("series");

    public final StringPath token = createString("token");

    public final StringPath userId = createString("userId");

    public QUserRememberMe(String variable) {
        super(UserRememberMe.class, forVariable(variable));
    }

    public QUserRememberMe(Path<? extends UserRememberMe> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserRememberMe(PathMetadata metadata) {
        super(UserRememberMe.class, metadata);
    }

}

