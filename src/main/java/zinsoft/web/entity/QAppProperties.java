package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.AppProperties;


/**
 * QAppProperties is a Querydsl query type for AppProperties
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAppProperties extends EntityPathBase<AppProperties> {

    private static final long serialVersionUID = 735078829L;

    public static final QAppProperties appProperties = new QAppProperties("appProperties");

    public final StringPath propId = createString("propId");

    public final StringPath propVal = createString("propVal");

    public QAppProperties(String variable) {
        super(AppProperties.class, forVariable(variable));
    }

    public QAppProperties(Path<? extends AppProperties> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAppProperties(PathMetadata metadata) {
        super(AppProperties.class, metadata);
    }

}

