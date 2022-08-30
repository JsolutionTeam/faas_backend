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
    private String acId;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private String acNm;
    @Column(name = "bp_t_cd")
    private String bpTCd;
    @Column(name = "cd_t_cd")
    private String cdTCd;
    private String upAcId;
    private String exprYn;
    private Long exprSeq;
    private String inputYn;
    @Column(name = "cost_t_cd")
    private String costTCd;
    private String updateYn;
    private String deleteYn;
    @Column(name = "ps_t_cd")
    private String psTCd;

}