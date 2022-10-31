package zinsoft.faas.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

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
    // 작물? 품종?

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
    private String cropACd;  // 부류
    @Column(name = "crop_b_cd")
    private String cropBCd; // 품목

    @Column(name = "crop_s_cd")
    private String cropSCd; // 재배유형

    @Column(name = "activity_t_cd")
    private Long activityTCd;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "crop", cascade = {})
    private List<CropSpecies> cropSpeciesList;


}