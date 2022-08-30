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
    private Long userProductionSeq;
    private String userId;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    @Column(name="plan_t_cd")
    private String planTCd;
    private String prdDt;
    private Double quan;
    @Column(name="pack_t_cd")
    private String packTCd;
    @Column(name="grade_t_cd")
    private String gradeTCd;
    private String remark;
    private Long cropSeq;
    private Long userCropSeq;
    private Long userDiarySeq;
    private String updateYn;
    private String deleteYn;

}