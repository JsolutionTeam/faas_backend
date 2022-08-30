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
@Table(name = "tf_user_ship")
@DynamicInsert
@DynamicUpdate
public class UserShip {
    @Id
    @GeneratedValue
    private Long userShipSeq;
    private String userId;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    @Column(name="plan_t_cd")
    private String planTCd;
    private String shipDt;
    private Long unitAmt;
    private Long amt;
    private Double unitPack;
    private Double quan;
    @Column(name="pack_t_cd")
    private String packTCd;
    @Column(name="grade_t_cd")
    private String gradeTCd;
    private Double defRate;
    private String destNm;
    private String remark;
    private Long cropSeq;
    private Long userCropSeq;
    private Long userInoutSeq;
    private String updateYn;
    private String deleteYn;

}