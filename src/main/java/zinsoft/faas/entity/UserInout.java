package zinsoft.faas.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tf_user_inout")
@DynamicInsert
@DynamicUpdate
public class UserInout implements Serializable{

    private static final long serialVersionUID = -988761361802541205L;
    @Id
    @GeneratedValue
    private Long userInoutSeq;
    private String userId;
    private String inoutCd;
    private String trdDt;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private Long cropSeq;
    private String acId;
    @Column(name = "grade_t_cd")
    private String gradeTCd;
    private Double unitPack;
    @Column(name = "pack_t_cd")
    private String packTCd;
    private Double quan;
    private Long unitAmt;
    private Long amt;
    @Column(name = "inout_t_cd")
    private String inoutTCd;
    private String remark;
    private String inoutContent;
    private String custNm;

    private String rTrdDt;
    @Column(name = "r_inout_t_cd")
    private String rInoutTCd;
//    private String userNm;
//    private String inoutCdNm;
//    private String cropNm;
//    private String acNm;
//    private String gradeTCdNm;
//    private String packTCdNm;
//    private String inoutTCdNm;
//    private String rInoutTCdNm;

//    private String cAcId;
//    private String dAcId;
//    private String upAcId;

    private Long userCropSeq;
//    private String userCropAliasNm;

    public UserInout(Long userInoutSeq, String userId) {
        this.userInoutSeq = userInoutSeq;
        this.userId = userId;
    }

}