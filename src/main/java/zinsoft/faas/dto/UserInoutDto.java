package zinsoft.faas.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class UserInoutDto {

    public static final String INOUT_CD_INCOME = "I";
    public static final String INOUT_CD_OUTGOING = "O";

    public static final String INOUT_T_CD_CASH = "1"; // 현금
    public static final String INOUT_T_CD_CREDIT = "2"; // 외상, 신용카드
    public static final String INOUT_T_CD_BANK = "3"; // 보통예금, 체크카드

    private Long userInoutSeq;
    private String userId;
    @NotBlank
    @ApiModelProperty(notes = "수입지출구분")
    @Pattern(regexp = "^(I|O)$")
    private String inoutCd;
    @NotBlank
    @ApiModelProperty(notes = "거래일자")
    @Pattern(regexp = "^\\d{4}-?(0[1-9]|1[012])-?(0[1-9]|[12][0-9]|3[01])$")
    private String trdDt;
    @JsonIgnore
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;
//    @NotNull
//    @Min(value = 1)
    private Long cropSeq;
    @Length(min=3, max=3)
    private String upAcId;
    private String costTCd;
    @NotBlank
    @Length(min=3, max=3)
    @ApiModelProperty(notes = "수입지출항목")
    private String acId;
    private String gradeTCd;
    @Min(value = 0)
    @Digits(integer = 8, fraction = 2)
    private Double unitPack;
    private String packTCd;
    @Min(value = 0)
    @Digits(integer = 8, fraction = 0)
    private Double quan;
    @Min(value = 0)
    @Digits(integer = 10, fraction = 0)
    private Long unitAmt;
    @NotNull
    @Min(value = 1)
    @Digits(integer = 10, fraction = 0)
    private Long amt;
    @NotBlank
    private String inoutTCd;
    private String remark;
    private String inoutContent;
    private String custNm;

    private String rTrdDt;
    private String rInoutTCd;

    private String userNm;
    private String inoutCdNm;
    private String cropNm;
    private String acNm;
    private String gradeTCdNm;
    private String packTCdNm;
    private String inoutTCdNm;
    private String rInoutTCdNm;
    private String costTCdNm;

    private List<Long> imgFileSeqs;
    private Map<Long, String> imgFiles;

    @JsonIgnore
    private List<Long> deleteImgFileSeqs;
    @JsonIgnore
    private List<MultipartFile> img;

//    @NotNull
//    @Min(value = 1)
    private Long userCropSeq;
    private String userCropAliasNm;

    private List<UserChemicalStockDto> chemicalList;
    private List<UserManureStockDto> manureList;

    public UserInoutDto(Long userInoutSeq, String userId) {
        this.userInoutSeq = userInoutSeq;
        this.userId = userId;
    }

}