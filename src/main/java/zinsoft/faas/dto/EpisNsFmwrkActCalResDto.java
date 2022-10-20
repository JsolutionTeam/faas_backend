package zinsoft.faas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import zinsoft.faas.entity.EpisNsFmwrkActCal;

@Data
@NoArgsConstructor
public class EpisNsFmwrkActCalResDto {

    private Long id;

    private String fmwrkActCode;

    private Integer rowGbn;

    private Integer startMonth;

    private Integer startQuart;

    private Integer endMonth;

    private Integer endQuart;

    private String growStepEly;

    private String growStepMid;

    private String growStepLst;

    private String fmwrkNm;

    private String fmwrkDesc;

    public EpisNsFmwrkActCalResDto(EpisNsFmwrkActCal fac){
        this.id = fac.getId();
        this.fmwrkActCode = fac.getFmwrkActCode();
        this.rowGbn = fac.getRowGbn();
        this.startMonth = fac.getStartMonth();
        this.startQuart = fac.getStartQuart();
        this.endMonth = fac.getEndMonth();
        this.endQuart = fac.getEndQuart();
        this.growStepEly = fac.getGrowStepEly();
        this.growStepMid = fac.getGrowStepMid();
        this.growStepLst = fac.getGrowStepLst();
        this.fmwrkNm = fac.getFmwrkNm();
        this.fmwrkDesc = fac.getFmwrkDesc();
    }
}
