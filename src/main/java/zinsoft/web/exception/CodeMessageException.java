package zinsoft.web.exception;

import lombok.Getter;
import zinsoft.util.Result;

@Getter
public class CodeMessageException extends RuntimeException {

    private static final long serialVersionUID = -2387949537239595948L;

    public static final String ERROR_EXCEPTION = "z.error.exception";

    private String code;
    private String arg;
    private Result result;

    public CodeMessageException() {
        this.code = Result.INTERNAL_SERVER_ERROR;
    }

    public CodeMessageException(String code) {
        this.code = code;
    }

    public CodeMessageException(String code, String arg) {
        this.code = code;
        this.arg = arg;
    }

    public CodeMessageException(Result result) {
        this.result = result;
        this.code = result.getHeader().getResultCode();
    }

}
