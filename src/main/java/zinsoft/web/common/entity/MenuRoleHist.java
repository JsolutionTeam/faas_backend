package zinsoft.web.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tf_menu_role_hist")
@DynamicInsert
public class MenuRoleHist {

    @Id
    @GeneratedValue
    private Long menuRoleHistSeq;
    private String workerId;
    private Date workDtm;
    private String workCd;
    private String menuId;
    private String roleId;
    private String actCd;

    public MenuRoleHist(String workerId, String workCd, String menuId, String roleId, String actCd) {
        this.workerId = workerId;
        this.workCd = workCd;
        this.menuId = menuId;
        this.roleId = roleId;
        this.actCd = actCd;
    }

}