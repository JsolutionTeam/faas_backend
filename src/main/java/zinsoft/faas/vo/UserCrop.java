package zinsoft.faas.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import zinsoft.util.KeyValueable;

@JsonInclude(Include.NON_NULL)
public class UserCrop implements KeyValueable<String> {

    private Long userCropSeq;
    private String userId;
    private String userNm;
    private String year;
    @JsonIgnore
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;
    private Long cropSeq;
    private double area;
    private String mainKind;
    private String cropSCd;
    private String stCrop;
    private String edCrop;
    private String plantYear;
    private long plantNum;
    private String remark;
    private long mother;
    private long young;
    private String zipcode;
    private String addr1;
    private String addr2;

    private String cropNm;
    private String cropSCdNm;
    private String cropACd;
    
    private String partTCd;   // 사업유형코드 
    private String partTCdNm; // 사업유형명

    private String exprYN; // 표출 여부
    private String aliasNm; // 사용자품목별칭
    
    public UserCrop() {
    }

    public UserCrop(String userId, Long cropSeq) {
        this.userId = userId;
        this.cropSeq = cropSeq;
    }

    public UserCrop(String userId, Long cropSeq, Long userCropSeq) {
        this.userId = userId;
        this.cropSeq = cropSeq;
        this.userCropSeq = userCropSeq;
    }

    @Override
    public String getKey() {
        return userCropSeq.toString();
    }

    @Override
    public String getValue() {
        return cropNm;
    }

    public Long getUserCropSeq() {
        return userCropSeq;
    }

    public void setUserCropSeq(Long userCropSeq) {
        this.userCropSeq = userCropSeq;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public Long getCropSeq() {
        return cropSeq;
    }

    public void setCropSeq(Long cropSeq) {
        this.cropSeq = cropSeq;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getMainKind() {
        return mainKind;
    }

    public void setMainKind(String mainKind) {
        this.mainKind = mainKind;
    }

    public String getCropSCd() {
        return cropSCd;
    }

    public void setCropSCd(String cropSCd) {
        this.cropSCd = cropSCd;
    }

    public String getStCrop() {
        return stCrop;
    }

    public void setStCrop(String stCrop) {
        this.stCrop = stCrop;
    }

    public String getEdCrop() {
        return edCrop;
    }

    public void setEdCrop(String edCrop) {
        this.edCrop = edCrop;
    }

    public String getPlantYear() {
        return plantYear;
    }

    public void setPlantYear(String plantYear) {
        this.plantYear = plantYear;
    }

    public long getPlantNum() {
        return plantNum;
    }

    public void setPlantNum(long plantNum) {
        this.plantNum = plantNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getMother() {
        return mother;
    }

    public void setMother(long mother) {
        this.mother = mother;
    }

    public long getYoung() {
        return young;
    }

    public void setYoung(long young) {
        this.young = young;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getCropNm() {
        return cropNm;
    }

    public void setCropNm(String cropNm) {
        this.cropNm = cropNm;
    }

    public String getCropSCdNm() {
        return cropSCdNm;
    }

    public void setCropSCdNm(String cropSCdNm) {
        this.cropSCdNm = cropSCdNm;
    }

    public String getCropACd() {
        return cropACd;
    }

    public void setCropACd(String cropACd) {
        this.cropACd = cropACd;
    }

    public String getPartTCd() {
        return partTCd;
    }

    public void setPartTCd(String partTCd) {
        this.partTCd = partTCd;
    }

    public String getPartTCdNm() {
        return partTCdNm;
    }

    public void setPartTCdNm(String partTCdNm) {
        this.partTCdNm = partTCdNm;
    }

    public String getExprYN() {
        return exprYN;
    }

    public void setExprYN(String exprYN) {
        this.exprYN = exprYN;
    }

    public String getAliasNm() {
        return aliasNm;
    }

    public void setAliasNm(String aliasNm) {
        this.aliasNm = aliasNm;
    }
    
}
