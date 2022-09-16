package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.MenuApi;


/**
 * QMenuApi is a Querydsl query type for MenuApi
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMenuApi extends EntityPathBase<MenuApi> {

    private static final long serialVersionUID = -1949940108L;

    public static final QMenuApi menuApi = new QMenuApi("menuApi");

    public final StringPath actCd = createString("actCd");

    public final StringPath menuId = createString("menuId");

    public final StringPath method = createString("method");

    public final StringPath pathPattern = createString("pathPattern");

    public QMenuApi(String variable) {
        super(MenuApi.class, forVariable(variable));
    }

    public QMenuApi(Path<? extends MenuApi> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMenuApi(PathMetadata metadata) {
        super(MenuApi.class, metadata);
    }

}

