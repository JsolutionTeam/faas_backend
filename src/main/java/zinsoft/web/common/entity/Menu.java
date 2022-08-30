package zinsoft.web.common.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_menu")
public class Menu {

    @Id
    private String menuId;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private String siteCd;
    private Integer sortOrder;
    private Integer depth;
    private String menuNm;

    @OneToMany(mappedBy = "menuId", fetch = FetchType.EAGER)
    @Where(clause = "act_cd = 'I'")
    private List<MenuRole> insertMenuRoleList;

    @OneToMany(mappedBy = "menuId", fetch = FetchType.EAGER)
    @Where(clause = "act_cd = 'L'")
    private List<MenuRole> listMenuRoleList;

    @OneToMany(mappedBy = "menuId", fetch = FetchType.EAGER)
    @Where(clause = "act_cd = 'V'")
    private List<MenuRole> detailMenuRoleList;

    @OneToMany(mappedBy = "menuId", fetch = FetchType.EAGER)
    @Where(clause = "act_cd = 'U'")
    private List<MenuRole> updateMenuRoleList;

    @OneToMany(mappedBy = "menuId", fetch = FetchType.EAGER)
    @Where(clause = "act_cd = 'D'")
    private List<MenuRole> deleteMenuRoleList;

    @OneToMany(mappedBy = "menuId", fetch = FetchType.EAGER)
    @Where(clause = "act_cd = 'E'")
    private List<MenuRole> exprMenuRoleList;

    //private List<MenuApi> menuApiList;

}