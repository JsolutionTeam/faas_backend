package zinsoft.web.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_menu_access_log")
@DynamicInsert
public class MenuAccessLog {

    @Id
    @GeneratedValue
    private Long menuAccessLogSeq;
    private Date accessDtm;
    private String menuId;
    private String userId;
    private String remoteAddr;
    private String note;

    public MenuAccessLog(String menuId, String userId, String remoteAddr, String note) {
        this.menuId = menuId;
        this.userId = userId;
        this.remoteAddr = remoteAddr;
        this.note = note;
    }

}