package zinsoft.faas.entity;

import java.io.Serializable;
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
@Table(name = "tf_user_diary")
@DynamicInsert
@DynamicUpdate
public class UserDiary implements Serializable {

    private static final long serialVersionUID = -7628630067292560839L;

    @Id
    @GeneratedValue
    @Column(name="user_diary_seq")
    private Long userDiarySeq;
    @Column(name="user_id")
    private String userId;

    @Column(name="act_dt")
    private String actDt;
    @Column(name="reg_dtm")
    private Date regDtm;
    @Column(name="update_dtm")
    private Date updateDtm;
    @Column(name="status_cd")
    private String statusCd;
    @Column(name = "diary_t_cd")
    private String diaryTCd;

    @Column(name="man_self")
    private Double manSelf;
    @Column(name="man_Self_tm")
    private Integer manSelfTm;
    @Column(name="man_self_tmm")
    private Integer manSelfTmm;
    @Column(name="woman_self")
    private Double womanSelf;
    @Column(name="woman_self_tm")
    private Integer womanSelfTm;
    @Column(name="woman_self_tmm")
    private Integer womanSelfTmm;
    @Column(name="man_Hire")
    private Double manHire;
    @Column(name="man_hire_tm")
    private Integer manHireTm;
    @Column(name="man_hire_tmm")
    private Integer manHireTmm;
    @Column(name="woman_hire")
    private Double womanHire;
    @Column(name="woman_hire_tm")
    private Integer womanHireTm;
    @Column(name="woman_hire_tmm")
    private Integer womanHireTmm;

    @Column(name = "sky_t_cd")
    private String skyTCd;
    @Column(name="temp")
    private Float temp;
    @Column(name="reh")
    private Float reh;
    @Column(name="tmn")
    private Float tmn;
    @Column(name="tmx")
    private Float tmx;
    @Column(name="rnf")
    private Float rnf;

    @Column(name="memo")
    private String memo;
    @Column(name="remark")
    private String remark;
    @Column(name = "grade_t_cd")
    private String gradeTCd;
    @Column(name="unit_pack")
    private Long unitPack;
    @Column(name = "pack_t_cd")
    private String packTCd;
    @Column(name="quan")
    private Double quan;

    @Column(name = " crop_cd")
    private String cropCd;
    @Column(name = "grow_step")
    private String growStep;
    @Column(name = "fmwrk_cd")
    private String fmwrkCd;
    @Column(name = "recommend")
    private String recommend;
    @Column(name="comment")
    private String comment;
}