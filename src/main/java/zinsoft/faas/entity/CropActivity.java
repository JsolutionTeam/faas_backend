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
@Table(name = "tf_crop_activity")
@DynamicInsert
@DynamicUpdate
public class CropActivity {
// 작물별 작업
    @Id
    @GeneratedValue
    @Column(name="crop_activity_seq")
    private Long cropActivitySeq;
    @Column(name="reg_dtm")
    private Date regDtm;
    @Column(name="update_dtm")
    private Date updateDtm;
    @Column(name="status_cd")
    private String statusCd;
//    @Column(name = "crop_a_cd")
//    private String cropACd;
    @Column(name = "activity_t_cd")
    private Long activityTCd;
    @Column(name="activity_seq")
    private Long activitySeq;
    @Column(name="expr_yn")
    private String exprYn;
    @Column(name="expr_seq")
    private Long exprSeq;
    @Column(name = "activity_t_cd_nm")
    private String activityTCdNm;

}