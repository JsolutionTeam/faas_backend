package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.FileInfo;


/**
 * QFileInfo is a Querydsl query type for FileInfo
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFileInfo extends EntityPathBase<FileInfo> {

    private static final long serialVersionUID = -450122447L;

    public static final QFileInfo fileInfo = new QFileInfo("fileInfo");

    public final StringPath contentType = createString("contentType");

    public final StringPath fileNm = createString("fileNm");

    public final NumberPath<Long> fileSeq = createNumber("fileSeq", Long.class);

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final DateTimePath<java.util.Date> regDtm = createDateTime("regDtm", java.util.Date.class);

    public final StringPath savedNm = createString("savedNm");

    public final StringPath statusCd = createString("statusCd");

    public final DateTimePath<java.util.Date> updateDtm = createDateTime("updateDtm", java.util.Date.class);

    public final StringPath userId = createString("userId");

    public QFileInfo(String variable) {
        super(FileInfo.class, forVariable(variable));
    }

    public QFileInfo(Path<? extends FileInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFileInfo(PathMetadata metadata) {
        super(FileInfo.class, metadata);
    }

}

