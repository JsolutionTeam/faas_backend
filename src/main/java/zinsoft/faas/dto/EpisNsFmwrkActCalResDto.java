package zinsoft.faas.dto;

import com.querydsl.core.annotations.QueryProjection;
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

    private String growStep;

    private String fmwrkCd;

    private String fmwrkDesc;

    @QueryProjection
    public EpisNsFmwrkActCalResDto(EpisNsFmwrkActCal fac){
        this.id = fac.getId();
        this.fmwrkActCode = fac.getFmwrkActCode();
        this.rowGbn = fac.getRowGbn();
        this.startMonth = fac.getStartMonth();
        this.startQuart = fac.getStartQuart();
        this.endMonth = fac.getEndMonth();
        this.endQuart = fac.getEndQuart();
        this.growStep = fac.getGrowStep();
        this.fmwrkCd = fac.getFmwrkCd();
        this.fmwrkDesc = fac.getFmwrkDesc();
    }
}
