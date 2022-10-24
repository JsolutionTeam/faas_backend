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
@Table(name = "tf_user_production")
@DynamicInsert
@DynamicUpdate
public class UserProduction {
    @Id
    @GeneratedValue
    @Column(name="user_production_seq")
    private Long userProductionSeq;
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
    @Column(name="prd_dt")
    private String prdDt;
    @Column(name="quan")
    private Double quan;
    @Column(name="pack_t_cd")
    private String packTCd;
    @Column(name="grade_t_cd")
    private String gradeTCd;
    @Column(name="remark")
    private String remark;
    @Column(name="crop_seq")
    private Long cropSeq;
    @Column(name="user_crop_seq")
    private Long userCropSeq;
    @Column(name="user_diary_seq")
    private Long userDiarySeq;
    @Column(name="update_yn")
    private String updateYn;
    @Column(name="delete_yn")
    private String deleteYn;

}