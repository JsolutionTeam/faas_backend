package zinsoft.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class Result {

    public static final String OK = "2000000";
    public static final String ID_AVAILABLE = "2000001";
    public static final String NO_USER = "2000002";

    public static final String BAD_REQUEST = "4000000";
    public static final String NO_DATA = "4000001";
    public static final String ID_ALREADY_IN_USE = "4000002";
    public static final String CURRENT_PASSWORD_MISMATCH = "4000003";
    public static final String INVALID_PASSWORD = "4000004";
    public static final String BLOCKED_USER = "4000005";
    public static final String NOT_FOUND_USER_CROP = "4000006";
    public static final String TOO_LONG_TERM = "4000007";
    public static final String INVALID_PACK_T_CD= "4000008";
    public static final String INVALID_GRADE_T_CD = "4000009";
    public static final String INVALID_INOUT_T_CD = "4000010";
    public static final String INVALID_AC_ID = "4000011";
    public static final String INVALID_CROP = "4000012";
    public static final String INVALID_ACTIVITY = "4000013";
    public static final String ALREADY_COST = "4000014";

    public static final String INVALID_CROPCD   = "4000100";
    public static final String INVALID_GROWSTEP = "4000101";
    public static final String INVALID_FMWRKCD  = "4000102";

    public static final String UNAUTHORIZED = "4010000";
    public static final String LOGIN_FAIL = "4010001";
    public static final String PASSWORD_MISMATCH = "4010002";
    public static final String IP_NOT_ALLOWED = "4010003";

    public static final String FORBIDDEN = "4030000";
    public static final String DUPLICATE_LOGIN = "4030001";
    public static final String USER_NOT_APPROVED = "4030002";

    public static final String NOT_FOUND = "4040000";

    public static final String INTERNAL_SERVER_ERROR = "5000000";

    private ResultHeader header;
    private Object body;

    public Result() {
    }

    public Result(boolean success, String resultCode) {
        this.header = new ResultHeader(success, resultCode);
    }

    public Result(boolean success, String resultCode, String resultMsg) {
        this.header = new ResultHeader(success, resultCode, resultMsg);
    }

    public Result(boolean success, String resultCode, Object[] args) {
        this.header = new ResultHeader(success, resultCode, args);
    }

    public Result(boolean success, String resultCode, Object[] args, String logId) {
        this.header = new ResultHeader(success, resultCode, args, logId);
    }

    public Result(boolean success, String resultCode, Object body) {
        this(success, resultCode);
        this.body = body;
    }

    public Result(boolean success, String resultCode, String resultMsg, Object body) {
        this(success, resultCode, resultMsg);
        this.body = body;
    }

}
