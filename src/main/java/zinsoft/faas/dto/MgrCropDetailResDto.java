package zinsoft.faas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zinsoft.faas.entity.MgrCropDetail;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MgrCropDetailResDto {
    private String code;
    private String codeNm;

    public MgrCropDetailResDto(MgrCropDetail crop){
        this.code = crop.getId().getCode();
        this.codeNm = crop.getCodeNm();
    }
}
