package zinsoft.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import zinsoft.web.common.entity.UserInfo;
import zinsoft.web.common.entity.UserRole;


/**
 * QUserInfo is a Querydsl query type for UserInfo
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserInfo extends EntityPathBase<UserInfo> {

    private static final long serialVersionUID = 18932736L;

    public static final QUserInfo userInfo = new QUserInfo("userInfo");

    public final StringPath addr1 = createString("addr1");

    public final StringPath addr2 = createString("addr2");

    public final StringPath admCd = createString("admCd");

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

    public final StringPath userNm = createString("userNm");

    public final StringPath userPwd = createString("userPwd");

    public final DateTimePath<java.util.Date> userPwdDtm = createDateTime("userPwdDtm", java.util.Date.class);

    public final DateTimePath<java.util.Date> userPwdNotiDtm = createDateTime("userPwdNotiDtm", java.util.Date.class);

    public final ListPath<UserRole, zinsoft.web.entity.QUserRole> userRoleList = this.<UserRole, zinsoft.web.entity.QUserRole>createList("userRoleList", UserRole.class, zinsoft.web.entity.QUserRole.class, PathInits.DIRECT2);

    public final StringPath zipcode = createString("zipcode");

    public QUserInfo(String variable) {
        super(UserInfo.class, forVariable(variable));
    }

    public QUserInfo(Path<? extends UserInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserInfo(PathMetadata metadata) {
        super(UserInfo.class, metadata);
    }

}

