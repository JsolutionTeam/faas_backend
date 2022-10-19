package zinsoft.faas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import zinsoft.faas.entity.SmartfarmCrop;

@Data
@NoArgsConstructor
public class SmarfarmCropResDto {
    private Long idx = null;

    // 대분류
    private String depth1;

    private String code1;

    private String depth2;

    private String code2;

    private String depth3;

    /* 소분류 품종 */
    private String code3;

    /* 작물 코드 */
    private String cropCode;

    public SmarfarmCropResDto(SmartfarmCrop cropCode){
        this.idx = cropCode.getIdx();
        this.depth1 = cropCode.getDepth1();
        this.depth2 = cropCode.getDepth2();
        this.depth3 = cropCode.getDepth3();
        this.code1 = cropCode.getCode1();
        this.code2 = cropCode.getCode2();
        this.code3 = cropCode.getCode3();
        this.cropCode = cropCode.getCropCode();
    }
}