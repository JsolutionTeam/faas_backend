package zinsoft.web.common.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class ApiAccessLogDto {

    private Long apiAccessLogSeq;
    private Date accessDtm;
    private String path;
    private String method;
    private String userId;
    private String remoteAddr;
    private String note;

}