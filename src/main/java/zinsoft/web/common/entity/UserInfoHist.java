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
public class UserInfoHist {

    @Id
    @GeneratedValue
    @Column(name = "user_info_hist_seq")
    private Long userInfoHistSeq;

    @Column(name = "worker_id")
    private String workerId;

    @Column(name = "work_dtm")
    private Date workDtm;

    @Column(name = "work_cd")
    private String workCd;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "reg_dtm")
    private Date regDtm;

    @Column(name = "update_dtm")
    private Date updateDtm;

    @Column(name = "status_cd")
    private String statusCd;

    @Column(name = "user_pwd")
    private String userPwd;

    @Column(name = "last_login_dtm")
    private Date lastLoginDtm;

    @Column(name = "user_pwd_dtm")
    private Date userPwdDtm;

    @Column(name = "user_pwd_noti_dtm")
    private Date userPwdNotiDtm;

    @Column(name = "last_login_fail_dtm")
    private Date lastLoginFailDtm;

    @Column(name = "login_fail_cnt")
    private Short loginFailCnt;

    @Column(name = "user_nm")
    private String userNm;

    @Column(name = "mobile_num")
    private String mobileNum;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "addr1")
    private String addr1;

    @Column(name = "addr2")
    private String addr2;

    @Column(name = "email_addr")
    private String emailAddr;

    @Column(name = "company_nm")
    private String companyNm;

    @Column(name = "note")
    private String note;


}