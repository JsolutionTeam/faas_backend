package zinsoft.web.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class MenuRoleDto {

    public static final String ACT_CD_INSERT = "I";
    public static final String ACT_CD_LIST = "L";
    public static final String ACT_CD_DETAIL = "V";
    public static final String ACT_CD_UPDATE = "U";
    public static final String ACT_CD_DELETE = "D";
    public static final String ACT_CD_EXPR = "E";

    private String menuId;
    private String roleId;
    private String actCd;

    public MenuRoleDto() {
    }

    public MenuRoleDto(String menuId, String roleId, String actCd) {
        this.menuId = menuId;
        this.roleId = roleId;
        this.actCd = actCd;
    }

}