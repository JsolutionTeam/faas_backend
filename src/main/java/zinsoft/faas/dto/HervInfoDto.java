package zinsoft.faas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import zinsoft.faas.entity.EpisFsHervInfo;

import java.util.Date;

@Data
@NoArgsConstructor
public class HervInfoDto {
    private Long hervSeq;
    private String hervName;
    private String farmCode;
    private String pestiKorName;
    private String kindCode;
    private String growType;
    private Date transPlantDt;
    private Date endDt;
    private String cultAreaPy;
    private String cultAreaM;
    private String cropsCnt;
    private String cropsMCnt;
    private String temp;
    private String heavy;
    private String sunlight;
    private String metaRate;
    private String inspModel;
    private String regId;
    private Date regDt;
    private String updId;
    private Date updDt;

    public HervInfoDto(EpisFsHervInfo hi){
        this.hervSeq = hi.getHervSeq();
        this.hervName = hi.getHervName();
        this.farmCode = hi.getFarmCode();
        this.pestiKorName = hi.getPestiKorName();
        this.kindCode = hi.getKindCode();
        this.growType = hi.getGrowType();
        this.transPlantDt = hi.getTransPlantDt();
        this.endDt = hi.getEndDt();
        this.cultAreaPy = hi.getCultAreaPy();
        this.cultAreaM = hi.getCultAreaM();
        this.cropsCnt = hi.getCropsCnt();
        this.cropsMCnt = hi.getCropsMCnt();
        this.temp = hi.getTemp();
        this.heavy = hi.getHeavy();
        this.sunlight = hi.getSunLight();
        this.metaRate = hi.getMetaRate();
        this.inspModel = hi.getInspModel();
        this.regId = hi.getRegId();
        this.regDt = hi.getRegDt();
        this.updId = hi.getUpdId();
        this.updDt = hi.getUpdDt();
    }

}
