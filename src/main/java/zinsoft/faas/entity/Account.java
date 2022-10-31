package zinsoft.faas.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "tf_account")
@DynamicInsert
@DynamicUpdate
public class Account{
    @Id
    @Column(name="ac_id")
    private String acId;

    @Column(name="reg_dtm")
    private Date regDtm;

    @Column(name="update_dtm")
    private Date updateDtm;

    @Column(name="status_cd")
    private String statusCd;

    @Column(name="ac_nm")
    private String acNm;

    @Column(name = "bp_t_cd")
    private String bpTCd;

    @Column(name = "cd_t_cd")
    private String cdTCd;

    @Column(name="up_ac_id")
    private String upAcId;
    @Column(name="expr_yn")
    private String exprYn;
    @Column(name="expr_seq")
    private Long exprSeq;
    @Column(name="input_yn")
    private String inputYn;

    @Column(name = "cost_t_cd")
    private String costTCd;

    @Column(name="update_yn")
    private String updateYn;
    @Column(name="delete_yn")
    private String deleteYn;

    @Column(name = "ps_t_cd")
    private String psTCd;

}