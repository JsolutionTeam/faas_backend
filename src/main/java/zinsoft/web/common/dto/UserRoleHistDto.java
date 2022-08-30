package zinsoft.web.common.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserRoleHistDto extends UserRoleDto {

    private static final long serialVersionUID = 2551479043714674327L;

    private String workerId;
    private Date workDtm;
    private String workCd;

    public UserRoleHistDto(String workerId, String workCd, String userId, String roleId) {
        super(userId, roleId);
        this.workerId = workerId;
        this.workCd = workCd;
    }

}