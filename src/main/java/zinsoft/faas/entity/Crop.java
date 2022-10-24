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

    @Column(name="crop_seq")
    private Long cropSeq;

    @Column(name="reg_dtm")
    private Date regDtm;

    @Column(name="update_dtm")
    private Date updateDtm;

    @Column(name="status_cd")
    private String statusCd;

    @Column(name="expr_nm")
    private String exprNm;

    @Column(name="expr_yn")
    private String exprYn;

    @Column(name="update_yn")
    private String updateYn;

    @Column(name="delete_yn")
    private String deleteYn;

    @Column(name="ser_life")
    private Long serLife;

    @Column(name="ma_life")
    private Long maLife;

    @Column(name="asset_yn")
    private String assetYn;

    @Column(name="croping_yn")
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