package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import zinsoft.web.common.entity.Menu;
import zinsoft.web.common.entity.MenuRole;


/**
 * QMenu is a Querydsl query type for Menu
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMenu extends EntityPathBase<Menu> {

    private static final long serialVersionUID = -15491642L;

    public static final QMenu menu = new QMenu("menu");

    public final ListPath<MenuRole, QMenuRole> deleteMenuRoleList = this.<MenuRole, QMenuRole>createList("deleteMenuRoleList", MenuRole.class, QMenuRole.class, PathInits.DIRECT2);

    public final NumberPath<Integer> depth = createNumber("depth", Integer.class);

    public final ListPath<MenuRole, QMenuRole> detailMenuRoleList = this.<MenuRole, QMenuRole>createList("detailMenuRoleList", MenuRole.class, QMenuRole.class, PathInits.DIRECT2);

    public final ListPath<MenuRole, QMenuRole> exprMenuRoleList = this.<MenuRole, QMenuRole>createList("exprMenuRoleList", MenuRole.class, QMenuRole.class, PathInits.DIRECT2);

    public final ListPath<MenuRole, QMenuRole> insertMenuRoleList = this.<MenuRole, QMenuRole>createList("insertMenuRoleList", MenuRole.class, QMenuRole.class, PathInits.DIRECT2);

    public final ListPath<MenuRole, QMenuRole> listMenuRoleList = this.<MenuRole, QMenuRole>createList("listMenuRoleList", MenuRole.class, QMenuRole.class, PathInits.DIRECT2);

    public final StringPath menuId = createString("menuId");

    public final StringPath menuNm = createString("menuNm");

    public final DateTimePath<java.util.Date> regDtm = createDateTime("regDtm", java.util.Date.class);

    public final StringPath siteCd = createString("siteCd");

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final StringPath statusCd = createString("statusCd");

    public final DateTimePath<java.util.Date> updateDtm = createDateTime("updateDtm", java.util.Date.class);

    public final ListPath<MenuRole, QMenuRole> updateMenuRoleList = this.<MenuRole, QMenuRole>createList("updateMenuRoleList", MenuRole.class, QMenuRole.class, PathInits.DIRECT2);

    public QMenu(String variable) {
        super(Menu.class, forVariable(variable));
    }

    public QMenu(Path<? extends Menu> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMenu(PathMetadata metadata) {
        super(Menu.class, metadata);
    }

}

