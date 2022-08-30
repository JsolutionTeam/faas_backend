package zinsoft.faas.dto;

import java.util.Date;

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
public class UserManureDto {

    private Long userManureSeq;

    private String userId;

    private Date regDtm;

    private Date updateDtm;

    private String statusCd;

    @NotBlank
    @ApiModelProperty(notes = "비료명")
    private String manureNm;

    @NotBlank
    @Pattern(regexp = "^[0-9A-Z]$")
    @ApiModelProperty(notes = "포장단위코드")
    private String packTCd;
    private String packTCdNm;

    @NotBlank
    @Pattern(regexp = "^[1]$")
    @ApiModelProperty(notes = "비료종류코드")
    private String manureTCd;

    @NotBlank
    @ApiModelProperty(notes = "비료상세코드")
    private String manureTCd2;

    @ApiModelProperty(notes = "비료종류명")
    private String manureTCdNm;

    @ApiModelProperty(notes = "비료상세명")
    private String manureTCdNm2;

    @ApiModelProperty(notes = "제조사")
    private String makerNm;

    @ApiModelProperty(notes = "비고")
    private String remark;

    @ApiModelProperty(notes = "복합비료내용(N:질소)")
    private Integer cpFerN;

    @ApiModelProperty(notes = "복합비료내용(P:인산)")
    private Integer cpFerP;

    @ApiModelProperty(notes = "복합비료내용(K:칼륨)")
    private Integer cpFerK;

    @ApiModelProperty(notes = "사용자명")
    private String userNm;

}