package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.MenuRoleHist;


/**
 * QMenuRoleHist is a Querydsl query type for MenuRoleHist
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMenuRoleHist extends EntityPathBase<MenuRoleHist> {

    private static final long serialVersionUID = -721058658L;

    public static final QMenuRoleHist menuRoleHist = new QMenuRoleHist("menuRoleHist");

    public final StringPath actCd = createString("actCd");

    public final StringPath menuId = createString("menuId");

    public final NumberPath<Long> menuRoleHistSeq = createNumber("menuRoleHistSeq", Long.class);

    public final StringPath roleId = createString("roleId");

    public final StringPath workCd = createString("workCd");

    public final DateTimePath<java.util.Date> workDtm = createDateTime("workDtm", java.util.Date.class);

    public final StringPath workerId = createString("workerId");

    public QMenuRoleHist(String variable) {
        super(MenuRoleHist.class, forVariable(variable));
    }

    public QMenuRoleHist(Path<? extends MenuRoleHist> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMenuRoleHist(PathMetadata metadata) {
        super(MenuRoleHist.class, metadata);
    }

}

