package zinsoft.web.common.entity;

import java.util.Date;

import javax.persistence.*;

import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class MenuAccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="menu_access_log_seq")
    private Long menuAccessLogSeq;

    @Column(name="access_dtm")
    private Date accessDtm;

    @Column(name="menu_id")
    private String menuId;

    @Column(name="user_id")
    private String userId;

    @Column(name="remote_addr")
    private String remoteAddr;

    @Column(name="note")
    private String note;


    public MenuAccessLog(String menuId, String userId, String remoteAddr, String note) {
        this.menuId = menuId;
        this.userId = userId;
        this.remoteAddr = remoteAddr;
        this.note = note;
    }

}