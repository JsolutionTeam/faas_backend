package zinsoft.faas.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserEmpCost {

    private Long userEmpCostSeq;
    private String userId;
    @JsonIgnore
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;
    private Long manAmt;
    private Long womanAmt;

    private String year;

    public UserEmpCost() {
    }

    public UserEmpCost(String userId) {
        this.userId = userId;
    }

    public Long getUserEmpCostSeq() {
        return userEmpCostSeq;
    }

    public void setUserEmpCostSeq(Long userEmpCostSeq) {
        this.userEmpCostSeq = userEmpCostSeq;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Long getManAmt() {
        return manAmt;
    }

    public void setManAmt(Long manAmt) {
        this.manAmt = manAmt;
    }

    public Long getWomanAmt() {
        return womanAmt;
    }

    public void setWomanAmt(Long womanAmt) {
        this.womanAmt = womanAmt;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}