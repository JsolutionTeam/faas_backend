package zinsoft.faas.dto;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
public class UserChemicalStockDto {

    private Long userChemicalStockSeq;
    private String userId;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;

    @NotNull
    @Min(value = 1)
    private Long userChemicalSeq;
    private Long userDiarySeq;
    private Long userInoutSeq;

    private Long cropSeq;
    private String cropNm;
    private Long userCropSeq;

    @NotBlank
    @Pattern(regexp = "^(I|O)$")
    private String supInoutCd;
    @ApiModelProperty(notes = "구매사용구분(추가/사용)")
    private String supInoutCdNm;
    @NotBlank
    @Pattern(regexp = "^\\d{4}-?(0[1-9]|1[012])-?(0[1-9]|[12][0-9]|3[01])$")
    private String inoutDt;

    @NotBlank
    @ApiModelProperty(notes = "단위구분코드")
    private String packTCd;
    @ApiModelProperty(notes = "단위")
    private String packTCdNm;

    @NotNull
    @Min(value = 1)
    @Digits(integer = 8, fraction = 2)
    @ApiModelProperty(notes = "수량")
    private Double quan;

    @ApiModelProperty(notes = "금액")
    @Min(value = 0)
    @Digits(integer = 10, fraction = 0)
    private Long amt;

    @ApiModelProperty(notes = "남은수")
    private Double remainingQuan;
    private String remark;

    private String updateYn;
    private String deleteYn;

    @ApiModelProperty(notes = "1:현금/2:외상(신용카드)/3:체크카드")
    private String inoutTCd;

    private String chemicalNm;
    private String makerNm;
    @ApiModelProperty(notes = "용도코드")
    private String chemicalTCd;
    @ApiModelProperty(notes = "용도명")
    private String chemicalTCdNm;

    @ApiModelProperty(notes = "농약명")
    private String userChemicalNm;

    private String userNm;

    public void setInoutDt(String inoutDt) {
        this.inoutDt = inoutDt.replace("-", "");
    }

}