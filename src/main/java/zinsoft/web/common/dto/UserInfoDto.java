package zinsoft.web.common.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = 8853228217855524670L;

    public static final String STATUS_CD_NORMAL = "N";
    public static final String STATUS_CD_DELETE = "D";
    public static final String STATUS_CD_BLOCK = "B";
    public static final String STATUS_CD_WAITING = "W";

    @Size(min = 4, max = 255)
    private String userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    private String statusCd;
    @JsonIgnore
    private String userPwd;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date lastLoginDtm;
    @JsonIgnore
    private Date userPwdDtm;
    @JsonIgnore
    private Date userPwdNotiDtm;
    @JsonIgnore
    private Date lastLoginFailDtm;
    @JsonIgnore
    private Short loginFailCnt;
    @NotEmpty
    @Size(max = 50)
    private String userNm;
    @Size(max = 20)
    private String mobileNum;
    @Size(min = 5, max = 5)
    private String zipcode;
    @Size(max = 255)
    private String addr1;
    @Size(max = 255)
    private String addr2;
    @Size(max = 255)
    @Email
    private String emailAddr;
    private String companyNm;
    private String birthDt;
    private String genderTCd;
    private String telNum;
    private Integer point;
    private Integer perform;
    private String accountYear;
    private Long cropSeq;
    private String admCd;
    private String note;

    private List<UserRoleDto> userRoleList;
    private List<String> userRoleIdList;

    @JsonIgnore
    private String curUserPwd;

    @JsonIgnore
    private String remoteAddr;

    private String admCd2;

    private String roleId;
    private String roleNm;
    private String crops;
    private int cropCnt = 0;
    private String userActivityActDt;
    private String slipTrdDt;

    private String cropNm;
    private String cropNm1;
    private String cropNm2;

    private String stZipcode;
    private String edZipcode;


    private String farmCode;

    @JsonIgnore
    public boolean isValid() {
        return (statusCd != null
                && !STATUS_CD_DELETE.equals(statusCd)
                && !STATUS_CD_BLOCK.equals(statusCd)
                && !STATUS_CD_WAITING.equals(statusCd));
    }

}