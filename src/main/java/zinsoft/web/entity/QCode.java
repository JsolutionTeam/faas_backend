package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.Code;


/**
 * QCode is a Querydsl query type for Code
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCode extends EntityPathBase<Code> {

    private static final long serialVersionUID = -15780268L;

    public static final QCode code = new QCode("code");

    public final StringPath codeEngNm = createString("codeEngNm");

    public final StringPath codeId = createString("codeId");

    public final StringPath codeNm = createString("codeNm");

    public final StringPath codeVal = createString("codeVal");

    public final NumberPath<Integer> exprSeq = createNumber("exprSeq", Integer.class);

    public final StringPath upCodeVal = createString("upCodeVal");

    public QCode(String variable) {
        super(Code.class, forVariable(variable));
    }

    public QCode(Path<? extends Code> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCode(PathMetadata metadata) {
        super(Code.class, metadata);
    }

}

