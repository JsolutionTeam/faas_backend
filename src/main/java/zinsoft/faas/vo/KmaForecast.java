package zinsoft.faas.vo;

public class KmaForecast {

    private String baseDate;
    private String baseTime;
    private Short nx;
    private Short ny;
    private String category;
    private String fcstDate;
    private String fcstTime;
    private Double fcstValue;

    @Override
    public String toString() {
        return "KmaForecast [baseDate=" + baseDate + ", baseTime=" + baseTime + ", nx=" + nx + ", ny=" + ny + ", category=" + category + ", fcstDate=" + fcstDate + ", fcstTime=" + fcstTime + ", fcstValue=" + fcstValue + "]";
    }

    public String getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }

    public String getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    public Short getNx() {
        return nx;
    }

    public void setNx(Short nx) {
        this.nx = nx;
    }

    public Short getNy() {
        return ny;
    }

    public void setNy(Short ny) {
        this.ny = ny;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFcstDate() {
        return fcstDate;
    }

    public void setFcstDate(String fcstDate) {
        this.fcstDate = fcstDate;
    }

    public String getFcstTime() {
        return fcstTime;
    }

    public void setFcstTime(String fcstTime) {
        this.fcstTime = fcstTime;
    }

    public Double getFcstValue() {
        return fcstValue;
    }

    public void setFcstValue(Double fcstValue) {
        this.fcstValue = fcstValue;
    }

    public Double getObsrValue() {
        return fcstValue;
    }

    public void setObsrValue(Double obsrValue) {
        this.fcstValue = obsrValue;
    }

}
