package zinsoft.web.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class UserRoleDto implements Serializable {

    private static final long serialVersionUID = 4457979577200928948L;

    private String userId;
    private String roleId;

    private String roleNm;

    public UserRoleDto() {
    }

    public UserRoleDto(String userId) {
        this.userId = userId;
    }

    public UserRoleDto(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

}