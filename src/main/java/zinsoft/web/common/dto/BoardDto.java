package zinsoft.web.common.dto;

import java.util.Date;

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
public class BoardDto {

    private String boardId;
    @JsonIgnore
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;
    private String boardNm;
    private String pushYn;
    private String pushRoles;
    private String pushEmails;
    private String rolesInsert;
    private String rolesList;
    private String rolesDetail;
    private String rolesUpdate;
    private String rolesDelete;

}