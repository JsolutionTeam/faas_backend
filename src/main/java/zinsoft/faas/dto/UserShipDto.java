package zinsoft.faas.dto;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class UserShipDto {

    public static final String PLAN_T_CD_ACTUAL = "A";//실적
    public static final String PLAN_T_CD_BUDGET = "B";//계획

    private Long userShipSeq;

    private String userId;

    private Date regDtm;

    private Date updateDtm;

    private String statusCd;

    @NotBlank
    @ApiModelProperty(notes = "계획/실적")
    private String planTCd;
    private String planTCdNm;

    @NotBlank
    @ApiModelProperty(notes = "날짜 YYYYMMDD")
    @Pattern(regexp = "^\\d{4}-?(0[1-9]|1[012])-?(0[1-9]|[12][0-9]|3[01])$")
    private String shipDt;

    @ApiModelProperty(notes = "단가")
    @Min(value = 0)
    @Digits(integer = 10, fraction = 0)
    private Long unitAmt;

    @ApiModelProperty(notes = "금액")
    @Min(value = 0)
    @Digits(integer = 10, fraction = 0)
    private Long amt;
    
    @ApiModelProperty(notes = "결제구분코드")
    private String inoutTCd;
    private String inoutTCdNm;
    
    @NotNull
    @Min(value = 0)
    @Digits(integer = 8, fraction = 2)
    @ApiModelProperty(notes = "포장단위수량")
    private Double unitPack;

    @NotNull
    @Min(value = 0)
    @Digits(integer = 8, fraction = 0)
    @ApiModelProperty(notes = "수량")
    private Double quan;

    @NotBlank
    @Pattern(regexp = "^[0-9A-Z]$")
    @ApiModelProperty(notes = "포장단위코드")
    private String packTCd;
    private String packTCdNm;

    @ApiModelProperty(notes = "등급코드")
    private String gradeTCd;
    private String gradeTCdNm;

    @NumberFormat(style=Style.PERCENT)
    @ApiModelProperty(notes = "비품과율")
    @Min(value = 0)
    @Digits(integer = 3, fraction = 2)
    private Double defRate;

    @ApiModelProperty(notes = "출하처")
    private String destNm;

    @ApiModelProperty(notes = "비고")
    private String remark;

    @ApiModelProperty(notes = "작물일련번호")
    private Long cropSeq;
    private String cropNm;

    @ApiModelProperty(notes = "작물재배현황일련번호")
    private Long userCropSeq;

    @ApiModelProperty(notes = "수입지출일련번호")
    private Long userInoutSeq;

    private String updateYn;
    private String deleteYn;

    @ApiModelProperty(notes = "사용자명")
    private String userNm;

    @ApiModelProperty(notes = "출하율")
    private Double shipRate;

    public void setShipDt(String shipDt) {
        this.shipDt = shipDt.replace("-", "");
    }
}