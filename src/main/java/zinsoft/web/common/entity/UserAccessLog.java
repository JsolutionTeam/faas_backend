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
@Table(name = "tf_user_access_log")
@DynamicInsert
@DynamicUpdate
public class UserAccessLog {

    @Id
    @GeneratedValue
    private Long userAccessLogSeq;
    private Date accessDtm;
    private String userId;
    private String inOut;
    private String successYn;
    private String remoteAddr;
    private String userAgent;
    private String note;

    //@Transient
    //private String inOutNm;

}