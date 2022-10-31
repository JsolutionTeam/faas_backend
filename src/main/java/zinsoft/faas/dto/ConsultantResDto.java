package zinsoft.faas.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zinsoft.faas.entity.Consultant;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultantResDto {

    @Schema(name="인덱스")
    private Long idx;

    @Schema(name = "회원ID")
    private String memId;

    @Schema(name = "농가ID")
    private String farmerNo;

    @Schema(name = "작기Id")
    private String cultNo;

    @Schema(name = "컨설팅 시작일")
    private Date startDate;

    @Schema(name = "컨설팅 종료일")
    private Date endDate;

    @Schema(name = "작업 지시 내용")
    private String workOrder;

    @QueryProjection
    public ConsultantResDto(Consultant cst){
        this.idx = cst.getIdx();
        this.cultNo = cst.getCultNo();
        this.memId = cst.getMemId();
        this.farmerNo = cst.getFarmerNo();
        this.startDate = cst.getStartdate();
        this.endDate = cst.getEndDate();
        this.workOrder = cst.getWorkOrder();
    }
}
