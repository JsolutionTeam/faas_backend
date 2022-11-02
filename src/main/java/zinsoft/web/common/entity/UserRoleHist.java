package zinsoft.web.common.entity;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_user_info_hist")
@DynamicInsert
@DynamicUpdate
public class UserRoleHist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_role_hist_seq")
    private Long userRoleHistSeq;

    @Column(name="worker_id")
    private String workerId;

    @Column(name="work_dtm")
    private Date workDtm;

    @Column(name="work_cd")
    private String workCd;

    @Column(name="user_id")
    private String userId;

    @Column(name="role_id")
    private String roleId;

}