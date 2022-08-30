package zinsoft.faas.dto;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class UserCropDto{

    private Long userCropSeq;
    //@NotBlank
    private String userId;
    private String userNm;
    @NotBlank
    @Pattern(regexp = "^20[0-9]{2}")
    private String year;
    @JsonIgnore
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;

    private Long cropSeq;
    private double area;
    private String mainKind;
    private String cropSCd;
    @Range(min = 1, max=12, message = "month(1~12)")
    private String stCrop;
    @Range(min = 1, max=12, message = "month(1~12)")
    private String edCrop;
    @Pattern(regexp = "^20[0-9]{2}")
    private String plantYear;
    private long plantNum;
    private String remark;
    private long mother;
    private long young;
    private String zipcode;
    private String addr1;
    private String addr2;

    private String cropNm;
    private String cropSCdNm;
    private String cropACd;

    private String partTCd;   // 사업유형코드
    private String partTCdNm; // 사업유형명

    private String exprYN; // 표출 여부
    @NotBlank
    private String aliasNm; // 사용자품목별칭
}
