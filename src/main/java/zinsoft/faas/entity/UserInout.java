package zinsoft.faas.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_inout_seq")
    private Long userInoutSeq;
    @Column(name="user_id")
    private String userId;
    @Column(name="inout_cd")
    private String inoutCd;
    @Column(name="trd_dt")
    private String trdDt;
    @Column(name="reg_dtm")
    private Date regDtm;
    @Column(name="update_dtm")
    private Date updateDtm;
    @Column(name="status_cd")
    private String statusCd;
    @Column(name="crop_seq")
    private Long cropSeq;
    @Column(name="ac_id")
    private String acId;
    @Column(name = "grade_t_cd")
    private String gradeTCd;
    @Column(name="unit_pack")
    private Double unitPack;
    @Column(name = "pack_t_cd")
    private String packTCd;
    @Column(name="quan")
    private Double quan;
    @Column(name="unit_amt")
    private Long unitAmt;
    @Column(name="amt")
    private Long amt;
    @Column(name = "inout_t_cd")
    private String inoutTCd;
    @Column(name="remark")
    private String remark;
    @Column(name="inout_content")
    private String inoutContent;
    @Column(name="cust_nm")
    private String custNm;

    @Column(name="r_trd_dt")
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

    @Column(name="user_crop_seq")
    private Long userCropSeq;
//    private String userCropAliasNm;

    public UserInout(Long userInoutSeq, String userId) {
        this.userInoutSeq = userInoutSeq;
        this.userId = userId;
    }

}