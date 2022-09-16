package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import zinsoft.web.common.entity.UserInfoHist;


/**
 * QUserInfoHist is a Querydsl query type for UserInfoHist
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserInfoHist extends EntityPathBase<UserInfoHist> {

    private static final long serialVersionUID = -30329022L;

    public static final QUserInfoHist userInfoHist = new QUserInfoHist("userInfoHist");

    public final StringPath addr1 = createString("addr1");

    public final StringPath addr2 = createString("addr2");

    public final StringPath companyNm = createString("companyNm");

    public final StringPath emailAddr = createString("emailAddr");

    public final DateTimePath<java.util.Date> lastLoginDtm = createDateTime("lastLoginDtm", java.util.Date.class);

    public final DateTimePath<java.util.Date> lastLoginFailDtm = createDateTime("lastLoginFailDtm", java.util.Date.class);

    public final NumberPath<Short> loginFailCnt = createNumber("loginFailCnt", Short.class);

    public final StringPath mobileNum = createString("mobileNum");

    public final StringPath note = createString("note");

    public final DateTimePath<java.util.Date> regDtm = createDateTime("regDtm", java.util.Date.class);

    public final StringPath statusCd = createString("statusCd");

    public final DateTimePath<java.util.Date> updateDtm = createDateTime("updateDtm", java.util.Date.class);

    public final StringPath userId = createString("userId");

    public final NumberPath<Long> userInfoHistSeq = createNumber("userInfoHistSeq", Long.class);

    public final StringPath userNm = createString("userNm");

    public final StringPath userPwd = createString("userPwd");

    public final DateTimePath<java.util.Date> userPwdDtm = createDateTime("userPwdDtm", java.util.Date.class);

    public final DateTimePath<java.util.Date> userPwdNotiDtm = createDateTime("userPwdNotiDtm", java.util.Date.class);

    public final StringPath workCd = createString("workCd");

    public final DateTimePath<java.util.Date> workDtm = createDateTime("workDtm", java.util.Date.class);

    public final StringPath workerId = createString("workerId");

    public final StringPath zipcode = createString("zipcode");

    public QUserInfoHist(String variable) {
        super(UserInfoHist.class, forVariable(variable));
    }

    public QUserInfoHist(Path<? extends UserInfoHist> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserInfoHist(PathMetadata metadata) {
        super(UserInfoHist.class, metadata);
    }

}

