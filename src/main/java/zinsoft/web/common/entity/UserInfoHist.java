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
public class UserInfoHist {

    @Id
    @GeneratedValue
    private Long userInfoHistSeq;
    private String workerId;
    private Date workDtm;
    private String workCd;
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

}