package zinsoft.web.common.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "vf_user_info")
@DynamicInsert
@DynamicUpdate
public class UserInfo {

    @Id
    private String userId;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private String userPwd;
    private Date lastLoginDtm;
    private Date userPwdDtm;
    private Date userPwdNotiDtm;
    private Date lastLoginFailDtm;
    private Short loginFailCnt;
    private String userNm;
    private String mobileNum;
    private String zipcode;
    private String addr1;
    private String addr2;
    private String emailAddr;
    private String companyNm;
    private String note;
    private String admCd;

    @OneToMany(mappedBy = "userId")
    private List<UserRole> userRoleList;

}