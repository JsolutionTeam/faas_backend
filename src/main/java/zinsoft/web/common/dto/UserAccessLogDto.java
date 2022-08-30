package zinsoft.web.common.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zinsoft.util.Constants;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserAccessLogDto {

    public static final String INOUT_LOGIN = "I";
    public static final String INOUT_REMEMBER_ME = "R";
    public static final String INOUT_LOGOUT = "O";
    public static final String INOUT_SESSION_DESTROYED = "D";
    public static final String SUCCESS_YN_YES = Constants.YN_YES;
    public static final String SUCCESS_YN_NO = Constants.YN_NO;

    private Long userAccessLogSeq;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S", timezone = "Asia/Seoul")
    private Date accessDtm;
    private String userId;
    private String inOut;
    private String successYn;
    private String remoteAddr;
    private String userAgent;
    private String note;

    private String userNm;
    private String inOutNm;

    public UserAccessLogDto(String userId, String inOut, String successYn, String remoteAddr, String userAgent, String note) {
        this.userId = userId;
        this.inOut = inOut;
        this.successYn = successYn;
        this.remoteAddr = remoteAddr;
        this.userAgent = userAgent;
        this.note = note;
    }

}