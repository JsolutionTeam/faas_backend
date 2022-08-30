package zinsoft.faas.vo;

import zinsoft.util.KeyValueable;

public class AdmZipcode implements KeyValueable<String> {

    private String admCd;
    private String admNm;
    private String stZipcode;
    private String edZipcode;

    @Override
    public String getKey() {
        return admCd;
    }

    @Override
    public String getValue() {
        return admNm;
    }

    public String getAdmCd() {
        return admCd;
    }

    public void setAdmCd(String admCd) {
        this.admCd = admCd;
    }

    public String getAdmNm() {
        return admNm;
    }

    public void setAdmNm(String admNm) {
        this.admNm = admNm;
    }

    public String getStZipcode() {
        return stZipcode;
    }

    public void setStZipcode(String stZipcode) {
        this.stZipcode = stZipcode;
    }

    public String getEdZipcode() {
        return edZipcode;
    }

    public void setEdZipcode(String edZipcode) {
        this.edZipcode = edZipcode;
    }

}