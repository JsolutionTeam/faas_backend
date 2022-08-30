package zinsoft.faas.dto;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zinsoft.util.KeyValueable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CropDto implements KeyValueable<String>  {

    private Long cropSeq;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;

    @NotBlank
    private String exprNm;
    private String cropNm;
    private String exprYn;
    private String updateYn;
    private String deleteYn;
    private Long serLife;
    private Long maLife;
    private String assetYn;
    private String cropingYn;
    private Long activityTCd;
    private String activityTCdNm;//작업목록명

    @NotBlank
    private String cropACd;
    private String cropBCd;
    private String cropCCd;
    private String cropSCd = null;
    private String cropTCd;
    private String cropACdNm;
    private String cropBCdNm;
    private String cropCCdNm;
    private String cropSCdNm;
    private String cropTCdNm;

    private Long cropCnt;

    @Override
    public String getKey() {
        // TODO Auto-generated method stub
        return cropSeq.toString();
    }

    @Override
    public String getValue() {
        // TODO Auto-generated method stub
        return exprNm;
    }


}