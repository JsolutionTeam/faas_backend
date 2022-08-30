package zinsoft.faas.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserInoutDetail {

    private Long userInoutDetailSeq;
    private String userId;
    private Long cropSeq;
    private String inoutCd;
    @JsonIgnore
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;
    private String detail;
    private Long userCropSeq;
    private Long exprSeq;

    public UserInoutDetail() {
    }

    public UserInoutDetail(String userId, Long cropSeq, String inoutCd) {
        this.userId = userId;
        this.cropSeq = cropSeq;
        this.inoutCd = inoutCd;
    }

    public Long getUserInoutDetailSeq() {
        return userInoutDetailSeq;
    }

    public void setUserInoutDetailSeq(Long userInoutDetailSeq) {
        this.userInoutDetailSeq = userInoutDetailSeq;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getCropSeq() {
        return cropSeq;
    }

    public void setCropSeq(Long cropSeq) {
        this.cropSeq = cropSeq;
    }

    public String getInoutCd() {
        return inoutCd;
    }

    public void setInoutCd(String inoutCd) {
        this.inoutCd = inoutCd;
    }

    public Date getRegDtm() {
        return regDtm;
    }

    public void setRegDtm(Date regDtm) {
        this.regDtm = regDtm;
    }

    public Date getUpdateDtm() {
        return updateDtm;
    }

    public void setUpdateDtm(Date updateDtm) {
        this.updateDtm = updateDtm;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getUserCropSeq() {
        return userCropSeq;
    }

    public void setUserCropSeq(Long userCropSeq) {
        this.userCropSeq = userCropSeq;
    }

    public Long getExprSeq() {
        return exprSeq;
    }

    public void setExprSeq(Long exprSeq) {
        this.exprSeq = exprSeq;
    }

}