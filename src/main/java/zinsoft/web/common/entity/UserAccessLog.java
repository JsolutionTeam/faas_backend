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
@Table(name = "tf_user_access_log")
@DynamicInsert
@DynamicUpdate
public class UserAccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_access_log_seq")
    private Long userAccessLogSeq;

    @Column(name="access_dtm")
    private Date accessDtm;

    @Column(name="user_id")
    private String userId;

    @Column(name="in_out")
    private String inOut;

    @Column(name="success_yn")
    private String successYn;

    @Column(name="remote_addr")
    private String remoteAddr;

    @Column(name="user_agent")
    private String userAgent;

    @Column(name="note")
    private String note;


    //@Transient
    //private String inOutNm;

}