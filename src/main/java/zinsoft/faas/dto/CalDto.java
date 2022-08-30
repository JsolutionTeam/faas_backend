package zinsoft.faas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class CalDto {

    public static final String CAL_T_CD_LUNAR_DAY = "0";
    public static final String CAL_T_CD_SOLAR_TERM = "1";

    private String calDt;
    private String calTCd;
    private String exprNm;

}