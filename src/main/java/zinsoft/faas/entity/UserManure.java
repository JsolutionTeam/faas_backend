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
    private Long userManureSeq;
    private String userId;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private String manureNm;

    @Column(name="pack_t_cd")
    private String packTCd;

    @Column(name="manure_t_cd")
    private String manureTCd;
    @Column(name="manure_t_cd2")
    private String manureTCd2;

    private String makerNm;
    private String remark;

    @Column(name="cp_fer_n")
    private Integer cpFerN;
    @Column(name="cp_fer_p")
    private Integer cpFerP;
    @Column(name="cp_fer_k")
    private Integer cpFerK;

}