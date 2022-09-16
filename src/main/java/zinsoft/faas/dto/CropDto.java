package zinsoft.faas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import zinsoft.faas.entity.Crop;
import zinsoft.faas.entity.CropSpecies;
import zinsoft.faas.entity.QCrop;
import zinsoft.util.KeyValueable;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CropDto implements KeyValueable<String> {

    private Long cropSeq; // 작물번호
    private Date regDtm; // 등록일자
    private Date updateDtm; // 수정일시
    private String statusCd; // 상태

    @NotBlank
    private String exprNm; // 설명?이름
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
    private String cropSCd = null;
    private String cropTCd;
    private String cropACdNm;
    private String cropBCdNm;
    private String cropSCdNm;
    private String cropTCdNm;
    private Long cropCnt;


    // 처음부터 모든 데이터를 가져가면 너무 오래걸림. 셀렉트박스 선택 시 조회하도록 함.
//     품목에 해당하는 품종 리스트
//    private List<CropSpeciesDto> cropSpeciesList;

    @QueryProjection
    public CropDto(Crop crop, String cropACdNm, String cropBCdNm, String cropSCdNm, Long cropCnt) {
        this.cropSeq = crop.getCropSeq();
        this.regDtm = crop.getRegDtm();
        this.updateDtm = crop.getUpdateDtm();
        this.statusCd = crop.getStatusCd();
        this.exprNm = crop.getExprNm();
        this.exprYn = crop.getExprYn();
        this.updateYn = crop.getUpdateYn();
        this.deleteYn = crop.getDeleteYn();
        this.serLife = crop.getSerLife();
        this.maLife = crop.getMaLife();
        this.assetYn = crop.getAssetYn();
        this.cropingYn = crop.getCropingYn();
        this.cropACd = crop.getCropACd();
        this.cropBCd = crop.getCropBCd();
        this.cropSCd = crop.getCropSCd();
        this.cropACdNm = cropACdNm;
        this.cropBCdNm = cropBCdNm;
        this.cropSCdNm = cropSCdNm;
        this.cropCnt = cropCnt;
//        if(crop.getCropSpeciesList() != null && crop.getCropSpeciesList().size() > 0){
//            List<CropSpecies> cropSpecies = crop.getCropSpeciesList();
//            this.cropSpeciesList =
//                    cropSpecies.stream()
//                            .map(CropSpeciesDto::new)
//                            .collect(Collectors.toList());
//        }
    }

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