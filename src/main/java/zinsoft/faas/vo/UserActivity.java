package zinsoft.faas.vo;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserActivity {

    private Long userActivitySeq;
    private String userId;
    @NotNull
    private Long cropSeq;
    @JsonIgnore
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;
    @NotNull
    private Long activitySeq;
    private Long exprSeq;

    private String cropNm;
    private String actNm;

    private Long userCropSeq;

    public UserActivity() {
    }

    public UserActivity(String userId, Long cropSeq) {
        this.userId = userId;
        this.cropSeq = cropSeq;
    }

    public UserActivity(String userId, Long cropSeq, Long userCropSeq) {
        this.userId = userId;
        this.cropSeq = cropSeq;
        this.userCropSeq = userCropSeq;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getUserActivitySeq() {
        return userActivitySeq;
    }

    public void setUserActivitySeq(Long userActivitySeq) {
        this.userActivitySeq = userActivitySeq;
    }

    public Long getCropSeq() {
        return cropSeq;
    }

    public void setCropSeq(Long cropSeq) {
        this.cropSeq = cropSeq;
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

    public Long getActivitySeq() {
        return activitySeq;
    }

    public void setActivitySeq(Long activitySeq) {
        this.activitySeq = activitySeq;
    }

    public Long getExprSeq() {
        return exprSeq;
    }

    public void setExprSeq(Long exprSeq) {
        this.exprSeq = exprSeq;
    }

    public String getCropNm() {
        return cropNm;
    }

    public void setCropNm(String cropNm) {
        this.cropNm = cropNm;
    }

    public String getActNm() {
        return actNm;
    }

    public void setActNm(String actNm) {
        this.actNm = actNm;
    }

    public Long getUserCropSeq() {
        return userCropSeq;
    }

    public void setUserCropSeq(Long userCropSeq) {
        this.userCropSeq = userCropSeq;
    }

}