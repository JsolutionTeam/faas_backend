package zinsoft.web.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
    @GeneratedValue
    private Long userRoleHistSeq;
    private String workerId;
    private Date workDtm;
    private String workCd;
    private String userId;
    private String roleId;

}