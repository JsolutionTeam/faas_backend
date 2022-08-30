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
public class MenuRoleHistDto extends MenuRoleDto {

    private Long menuRoleHistSeq;
    private String workerId;
    private Date workDtm;
    private String workCd;

}