package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.MenuRole;


/**
 * QMenuRole is a Querydsl query type for MenuRole
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMenuRole extends EntityPathBase<MenuRole> {

    private static final long serialVersionUID = -318095524L;

    public static final QMenuRole menuRole = new QMenuRole("menuRole");

    public final StringPath actCd = createString("actCd");

    public final StringPath menuId = createString("menuId");

    public final StringPath roleId = createString("roleId");

    public QMenuRole(String variable) {
        super(MenuRole.class, forVariable(variable));
    }

    public QMenuRole(Path<? extends MenuRole> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMenuRole(PathMetadata metadata) {
        super(MenuRole.class, metadata);
    }

}

