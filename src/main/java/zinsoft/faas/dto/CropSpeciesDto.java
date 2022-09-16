package zinsoft.faas.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import zinsoft.faas.entity.Crop;
import zinsoft.faas.entity.CropSpecies;

import java.util.Date;

@Data
@NoArgsConstructor
public class CropSpeciesDto {
    private Long CropSpeciesSeq;
//    private Long CropSeq;
    private String name;
    private Date regDtm; // 등록일자
    private Date updateDtm; // 수정일시

//    private Crop crop;

    public CropSpeciesDto(CropSpecies cropSpecies){
        this.CropSpeciesSeq = cropSpecies.getCropSpeciesSeq();
        this.name = cropSpecies.getName();
        this.regDtm = cropSpecies.getRegDtm();
        this.updateDtm = cropSpecies.getUpdateDtm();
//        this.crop = cropSpecies.getCrop();
    }
}
