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
@Table(name = "tf_crop")
@DynamicInsert
@DynamicUpdate
public class Crop {

    @Id
    @GeneratedValue
    private Long cropSeq;

    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private String exprNm;
    private String exprYn;
    private String updateYn;
    private String deleteYn;
    private Long serLife;
    private Long maLife;
    private String assetYn;
    private String cropingYn;

    @Column(name = "crop_a_cd")
    private String cropACd;
    @Column(name = "crop_b_cd")
    private String cropBCd;
//    @Column(name = "crop_c_cd")
//    private String cropCCd;
    @Column(name = "crop_s_cd")
    private String cropSCd;
//    @Column(name = "crop_t_cd")
//    private String cropTCd;

    @Column(name = "activity_t_cd")
    private Long activityTCd;

}