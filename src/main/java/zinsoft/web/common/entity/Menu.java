package zinsoft.web.common.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

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
    @Column(name="menu_id")
    private String menuId;

    @Column(name="reg_dtm")
    private Date regDtm;

    @Column(name="update_dtm")
    private Date updateDtm;

    @Column(name="status_cd")
    private String statusCd;

    @Column(name="site_cd")
    private String siteCd;

    @Column(name="sort_order")
    private Integer sortOrder;

    @Column(name="depth")
    private Integer depth;

    @Column(name="menu_nm")
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