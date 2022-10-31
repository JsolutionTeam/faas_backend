package zinsoft.faas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zinsoft.faas.entity.EpisNsFmwrkWrkcd;
import zinsoft.faas.entity.MgrCropDetail;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisNsFmwrkWrkcdResDto {
    private String fmwrkCd;
    private String fmwrkNm;
}
