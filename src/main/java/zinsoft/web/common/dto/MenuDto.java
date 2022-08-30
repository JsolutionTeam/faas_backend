package zinsoft.web.common.dto;

import java.util.Date;
import java.util.List;

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
public class MenuDto {

    private String menuId;
    @JsonIgnore
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;
    @JsonIgnore
    private String siteCd;
    @JsonIgnore
    private Integer sortOrder;
    private Integer depth;
    private String menuNm;

    @JsonIgnore
    private List<MenuDto> parentList;

    @JsonIgnore
    private List<MenuRoleDto> insertMenuRoleList;
    @JsonIgnore
    private List<MenuRoleDto> listMenuRoleList;
    @JsonIgnore
    private List<MenuRoleDto> detailMenuRoleList;
    @JsonIgnore
    private List<MenuRoleDto> updateMenuRoleList;
    @JsonIgnore
    private List<MenuRoleDto> deleteMenuRoleList;
    @JsonIgnore
    private List<MenuRoleDto> exprMenuRoleList;
    @JsonIgnore
    private List<MenuApiDto> menuApiList;

    public MenuDto() {
    }

    public MenuDto(String menuNm) {
        this.menuNm = menuNm;
    }

}