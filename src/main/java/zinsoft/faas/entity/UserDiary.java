package zinsoft.faas.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Double manSelf; // 자가노동인력수(남)

    @Column(name="man_Self_tm")
    private Integer manSelfTm; // 자가노동시간(시간,남)
    @Column(name="man_self_tmm")
        private Integer manSelfTmm; // 자가노동시간(분,남)
    @Column(name="woman_self")
        private Double womanSelf; // 자가노동인력수(여)
    @Column(name="woman_self_tm")
        private Integer womanSelfTm; // 자가노동시간(시간,여)
    @Column(name="woman_self_tmm")
        private Integer womanSelfTmm; // 자가노동시간(분,여)
    @Column(name="man_Hire")
        private Double manHire; // 고용노동인력수(남)
    @Column(name="man_hire_tm")
        private Integer manHireTm; // 고용노동시간(시간,남)
    @Column(name="man_hire_tmm")
        private Integer manHireTmm; // 고용노동시간(분,남)
    @Column(name="woman_hire")
        private Double womanHire; // 고용노동인력수(여)
    @Column(name="woman_hire_tm")
        private Integer womanHireTm; // 고용노동시간(시간,여)
    @Column(name="woman_hire_tmm")
        private Integer womanHireTmm; // 고용노동시간(분,여)


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

    @Column(name = "crop_cd")
    // 작기명(품목)
    private String cropCd;
    @Column(name = "crop_kind")
    // 작업분류 (대분류)
    private String cropKind;
    @Column(name = "grow_step")
    // 작업분류 (대분류)
    private String growStep;
    @Column(name = "fmwrk_cd")
    // 작업단계 (중분류)
    private String fmwrkCd;
    @Column(name = "recommend")
    private String recommend;
    @Column(name="comment")
    private String comment;
}