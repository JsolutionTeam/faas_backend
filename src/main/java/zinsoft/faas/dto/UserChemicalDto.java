package zinsoft.faas.dto;

import java.util.Date;

import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotBlank;
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
public class UserChemicalDto {

    private Long userChemicalSeq;

    private String userId;

    private Date regDtm;

    private Date updateDtm;

    private String statusCd;

    @ApiModelProperty(notes = "품목명")
    private String cropNm;

    @NotBlank
    @ApiModelProperty(notes = "농약명")
    private String userChemicalNm;

    @NotBlank
    @Pattern(regexp = "^[0-9A-Z]$")
    @ApiModelProperty(notes = "포장단위코드")
    private String packTCd;
    private String packTCdNm;

    @Min(value = 0)
    @ApiModelProperty(notes = "용도코드")
    private String chemicalTCd;
    private String chemicalTCdNm;

    @ApiModelProperty(notes = "적용병충해")
    private String insect;

    @ApiModelProperty(notes = "상표명")
    private String productNm;

    @ApiModelProperty(notes = "농약품목명")
    private String chemicalNm;

    @ApiModelProperty(notes = "제조사")
    private String makerNm;

    @ApiModelProperty(notes = "비고")
    private String remark;

    @ApiModelProperty(notes = "사용자명")
    private String userNm;

}