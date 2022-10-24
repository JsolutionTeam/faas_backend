package zinsoft.faas.entity;

import java.util.Date;

import javax.persistence.Column;
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
@Table(name = "tf_user_manure")
@DynamicInsert
@DynamicUpdate
public class UserManure {

    @Id
    @GeneratedValue
    @Column(name="user_manure_seq")
    private Long userManureSeq;
    @Column(name="user_id")
    private String userId;
    @Column(name="reg_dtm")
    private Date regDtm;
    @Column(name="update_dtm")
    private Date updateDtm;
    @Column(name="status_cd")
    private String statusCd;
    @Column(name="manure_nm")
    private String manureNm;

    @Column(name="pack_t_cd")
    private String packTCd;

    @Column(name="manure_t_cd")
    private String manureTCd;
    @Column(name="manure_t_cd2")
    private String manureTCd2;

    @Column(name="maker_nm")
    private String makerNm;
    @Column(name="remark")
    private String remark;

    @Column(name="cp_fer_n")
    private Integer cpFerN;
    @Column(name="cp_fer_p")
    private Integer cpFerP;
    @Column(name="cp_fer_k")
    private Integer cpFerK;

}