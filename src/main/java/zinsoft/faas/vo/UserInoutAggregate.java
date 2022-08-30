package zinsoft.faas.vo;

import java.util.List;
import java.util.Map;

public class UserInoutAggregate {
    
    String inoutCd;
    String inoutCdNm;
    Long inoutTotal;
    Long inoutCount;
    Long userCropCount;
    Long userCustCount;
    
    List<Map<String, Object>> inoutTCdList;
    List<Map<String, Object>> totalQuanList;
    
    public String getInoutCd() {
        return inoutCd;
    }
    
    public void setInoutCd(String inoutCd) {
        this.inoutCd = inoutCd;
    }
    
    public String getInoutCdNm() {
        return inoutCdNm;
    }

    public void setInoutCdNm(String inoutCdNm) {
        this.inoutCdNm = inoutCdNm;
    }

    public Long getInoutTotal() {
        return inoutTotal;
    }
    
    public void setInoutTotal(Long inoutTotal) {
        this.inoutTotal = inoutTotal;
    }
    
    public Long getInoutCount() {
        return inoutCount;
    }
    
    public void setInoutCount(Long inoutCount) {
        this.inoutCount = inoutCount;
    }
    
    public Long getUserCropCount() {
        return userCropCount;
    }
    
    public void setUserCropCount(Long userCropCount) {
        this.userCropCount = userCropCount;
    }
    
    public Long getUserCustCount() {
        return userCustCount;
    }
    
    public void setUserCustCount(Long userCustCount) {
        this.userCustCount = userCustCount;
    }
    
    public List<Map<String, Object>> getInoutTCdList() {
        return inoutTCdList;
    }
    
    public void setInoutTCdList(List<Map<String, Object>> inoutTCdList) {
        this.inoutTCdList = inoutTCdList;
    }
    
    public List<Map<String, Object>> getTotalQuanList() {
        return totalQuanList;
    }
    
    public void setTotalQuanList(List<Map<String, Object>> totalQuanList) {
        this.totalQuanList = totalQuanList;
    }
    
}
