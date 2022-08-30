package zinsoft.faas.dto;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zinsoft.util.KeyValueable;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto implements KeyValueable<String> {

    private String acId;
    @JsonIgnore
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;
    @NotBlank
    private String acNm;
    private String bpTCd;
    private String cdTCd;
    @NotBlank
    private String upAcId;
    private String exprYn;
    private Long exprSeq;
    @NotBlank
    private String inputYn;
    private String costTCd;
    private String costTCdNm;
    private String updateYn;
    private String deleteYn;

    private String psTCd;//일반(N),매입(P),매출(S)
    private String psTCdNm;

    private String upAcNm;
    private String bpTCdNm;
    private String cdTCdNm;

    private String gCd;
    private String gCdNm;
    
    @NotBlank
    private String rootAcId;


    @Override
    public String getKey() {
        return acId;
    }

    @Override
    public String getValue() {
        return acId + " | " + acNm;
    }

}