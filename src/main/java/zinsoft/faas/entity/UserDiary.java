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
    private Long userDiarySeq;
    private String userId;

    private String actDt;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    @Column(name = "diary_t_cd")
    private String diaryTCd;
    private Long cropSeq;
    private Long userCropSeq;
    private Long activitySeq;

    private Double manSelf;
    private Integer manSelfTm;
    private Integer manSelfTmm;
    private Double womanSelf;
    private Integer womanSelfTm;
    private Integer womanSelfTmm;
    private Double manHire;
    private Integer manHireTm;
    private Integer manHireTmm;
    private Double womanHire;
    private Integer womanHireTm;
    private Integer womanHireTmm;

    @Column(name = "sky_t_cd")
    private String skyTCd;
    private Float temp;
    private Float reh;
    private Float tmn;
    private Float tmx;
    private Float rnf;

    private String memo;
    private String remark;
    private String actNm;
    private Long unitPack;
    @Column(name = "pack_t_cd")
    private String packTCd;
    private Double quan;

    @Column(name = "grade_t_cd")
    private String gradeTCd;

}