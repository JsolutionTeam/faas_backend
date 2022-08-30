package zinsoft.faas.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class CropActivityDto {

    private Long cropActivitySeq;
    @JsonIgnore
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;
    //private String cropACd;
    private Long activityTCd;
    @NotNull
    private Long activitySeq;
    private String exprYn;
    private Long exprSeq;

    private String actNm;
    private String activityTCdNm;

}