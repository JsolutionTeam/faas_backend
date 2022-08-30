package zinsoft.faas.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class UserInoutAggregateDetail {
    
    List<Map<String, Object>> inoutCd;
    List<Map<String, Object>> inoutTCd;
    List<Map<String, Object>> userCrop;
    List<Map<String, Object>> partTCd;
    List<Map<String, Object>> userCust;
    
    public List<Map<String, Object>> getInoutCd() {
        return inoutCd;
    }
    
    public void setInoutCd(List<Map<String, Object>> inoutCd) {
        this.inoutCd = inoutCd;
    }
    
    public List<Map<String, Object>> getInoutTCd() {
        return inoutTCd;
    }
    
    public void setInoutTCd(List<Map<String, Object>> inoutTCd) {
        this.inoutTCd = inoutTCd;
    }
    
    public List<Map<String, Object>> getUserCrop() {
        return userCrop;
    }
    
    public void setUserCrop(List<Map<String, Object>> userCrop) {
        this.userCrop = userCrop;
    }
    
    public List<Map<String, Object>> getPartTCd() {
        return partTCd;
    }
    
    public void setPartTCd(List<Map<String, Object>> partTCd) {
        this.partTCd = partTCd;
    }
    
    public List<Map<String, Object>> getUserCust() {
        return userCust;
    }
    
    public void setUserCust(List<Map<String, Object>> userCust) {
        this.userCust = userCust;
    }
    
    public List<Map<String, Object>> removeDup(List<Map<String, Object>> list){
        List<Map<String, Object>> reList =  null;
        if(list != null && list.size() > 0) {
            HashSet<Map<String, Object>> setList = new HashSet<Map<String, Object>>(list);
            reList = new ArrayList<Map<String, Object>>(setList);
        }
        return reList;
    }
}
