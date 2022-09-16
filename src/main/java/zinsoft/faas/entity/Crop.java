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
    private Long cropSeq;

    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private String exprNm; // 표출명
    private String exprYn;
    private String updateYn;
    private String deleteYn;
    private Long serLife;
    private Long maLife;
    private String assetYn;
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