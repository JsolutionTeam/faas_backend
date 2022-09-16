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
    private Long cropActivitySeq;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
//    @Column(name = "crop_a_cd")
//    private String cropACd;
    @Column(name = "activity_t_cd")
    private Long activityTCd;
    private Long activitySeq;
    private String exprYn;
    private Long exprSeq;
    @Column(name = "activity_t_cd_nm")
    private String activityTCdNm;

}