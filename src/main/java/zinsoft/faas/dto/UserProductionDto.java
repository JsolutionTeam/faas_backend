package zinsoft.faas.dto;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

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
public class UserProductionDto {

    public static final String PLAN_T_CD_ACTUAL = "A";//실적
    public static final String PLAN_T_CD_BUDGET = "B";//계획

    private Long userProductionSeq;

    private String userId;

    private Date regDtm;

    private Date updateDtm;

    private String statusCd;

    @NotBlank
    @Pattern(regexp = "^(A|B)$")
    @ApiModelProperty(notes = "계획/실적")
    private String planTCd;
    private String planTCdNm;

    @NotBlank
    @ApiModelProperty(notes = "일자")
    @Pattern(regexp = "^\\d{4}-?(0[1-9]|1[012])-?(0[1-9]|[12][0-9]|3[01])$")
    private String prdDt;

    @NotNull
    @Min(value = 0)
    @Digits(integer = 8, fraction = 0)
    private Double quan;

    @NotBlank
    @Pattern(regexp = "^[0-9A-Z]$")
    @ApiModelProperty(notes = "포장단위코드")
    private String packTCd;
    private String packTCdNm;

    @ApiModelProperty(notes = "등급코드")
    private String gradeTCd;
    private String gradeTCdNm;

    @ApiModelProperty(notes = "비고")
    private String remark;

    @ApiModelProperty(notes = "작물일련번호")
    private Long cropSeq;
    private String cropNm;

    @ApiModelProperty(notes = "작물재배현황일련번호")
    private Long userCropSeq;

    @ApiModelProperty(notes = "영농일지일련번호")
    private Long userDiarySeq;

    private String updateYn;
    private String deleteYn;

    @ApiModelProperty(notes = "사용자명")
    private String userNm;

    @ApiModelProperty(notes = "생산율")
    private Double prdRate;

    public void setPrdDt(String prdDt) {
        this.prdDt = prdDt.replace("-", "");
    }

}