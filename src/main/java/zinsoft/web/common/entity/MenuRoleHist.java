package zinsoft.web.common.entity;

import java.util.Date;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_role_hist_seq")
    private Long menuRoleHistSeq;

    @Column(name = "workder_id")
    private String workerId;


    @Column(name = "work_dtm")
    private Date workDtm;

    @Column(name = "work_cd")
    private String workCd;
    
    @Column(name = "menu_id")
    private String menuId;
    
    @Column(name = "role_id")
    private String roleId;
    
    @Column(name = "act_cd")
    private String actCd;

    public MenuRoleHist(String workerId, String workCd, String menuId, String roleId, String actCd) {
        this.workerId = workerId;
        this.workCd = workCd;
        this.menuId = menuId;
        this.roleId = roleId;
        this.actCd = actCd;
    }

}