package zinsoft.faas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import zinsoft.faas.entity.EpisNsFmwrkAct;
import zinsoft.faas.entity.EpisNsFmwrkActCal;

import java.util.Date;

@Data
@NoArgsConstructor
public class EpisNsFmwrkActResDto {

    @Schema(name="기본키")
    private String fmwrkActCode;

    @Schema(name="작물코드")
    private String cropCd;

    @Schema(name="등록자")
    private String regId;

    private Date regDt;

    private String updId;

    private Date updDt;

    public EpisNsFmwrkActResDto(EpisNsFmwrkAct fa){
        this.fmwrkActCode = fa.getFmwrkActCode();
        this.cropCd = fa.getCropCd();
        this.regId = fa.getRegId();
        this.regDt = fa.getRegDt();
        this.updId = fa.getUpdId();
        this.updDt = fa.getUpdDt();
    }
}
