package zinsoft.faas.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_user_ship")
@DynamicInsert
@DynamicUpdate
public class UserShip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_ship_seq")
    private Long userShipSeq;
    @Column(name="user_id")
    private String userId;
    @Column(name="reg_dtm")
    private Date regDtm;
    @Column(name="update_dtm")
    private Date updateDtm;
    @Column(name="status_cd")
    private String statusCd;
    @Column(name="plan_t_cd")
    private String planTCd;
    @Column(name="ship_dt")
    private String shipDt;
    @Column(name="unit_amt")
    private Long unitAmt;
    @Column(name="amt")
    private Long amt;
    @Column(name="unit_pack")
    private Double unitPack;
    @Column(name="quan")
    private Double quan;
    @Column(name="pack_t_cd")
    private String packTCd;
    @Column(name="grade_t_cd")
    private String gradeTCd;
    @Column(name="def_rate")
    private Double defRate;
    @Column(name="dest_nm")
    private String destNm;
    @Column(name="remark")
    private String remark;
    @Column(name="crop_seq")
    private Long cropSeq;
    @Column(name="user_crop_seq")
    private Long userCropSeq;
    @Column(name="user_inout_seq")
    private Long userInoutSeq;
    @Column(name="update_yn")
    private String updateYn;
    @Column(name="delete_yn")
    private String deleteYn;

}