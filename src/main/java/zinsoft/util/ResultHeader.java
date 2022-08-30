package zinsoft.util;


import org.apache.commons.text.StringEscapeUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ResultHeader {

    private boolean success;
    private String resultCode;
    private String resultMsg;
    private String logId;

    public ResultHeader() {
    }

    public ResultHeader(boolean success, String resultCode) {
        this(success, resultCode, (Object[]) null);
    }

    public ResultHeader(boolean success, String resultCode, String resultMsg) {
        this.success = success;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public ResultHeader(boolean success, String resultCode, Object[] args) {
        this.success = success;
        this.resultCode = resultCode;
        this.resultMsg = StringEscapeUtils.unescapeJava(ApplicationContextProvider.applicationContext().getMessage("code." + resultCode, args, LocaleUtil.locale()));
    }

    public ResultHeader(boolean success, String resultCode, Object[] args, String logId) {
        this(success, resultCode, args);
        this.logId = logId;
    }

}
