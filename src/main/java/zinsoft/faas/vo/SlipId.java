package zinsoft.faas.vo;

public class SlipId {

    private long slipSeq;
    private String userId;
    private String trdDt;
    private long slipNo;
    private String grId;

    public SlipId() {
    }

    public SlipId(long slipSeq, String userId, String trdDt, long slipNo, String grId) {
        this.slipSeq = slipSeq;
        this.userId = userId;
        this.trdDt = trdDt;
        this.slipNo = slipNo;
        this.grId = grId;
    }

    public long getSlipSeq() {
        return slipSeq;
    }

    public void setSlipSeq(long slipSeq) {
        this.slipSeq = slipSeq;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTrdDt() {
        return trdDt;
    }

    public void setTrdDt(String trdDt) {
        this.trdDt = trdDt;
    }

    public long getSlipNo() {
        return slipNo;
    }

    public void setSlipNo(long slipNo) {
        this.slipNo = slipNo;
    }

    public String getGrId() {
        return grId;
    }

    public void setGrId(String grId) {
        this.grId = grId;
    }

}