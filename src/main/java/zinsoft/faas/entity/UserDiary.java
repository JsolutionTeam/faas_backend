package zinsoft.faas.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private Long userDiarySeq; // '영농일지일련번호'
    private String userId; // '사용자ID'

    private String actDt; // 영농일자
    private Date regDtm; // 등록일시
    private Date updateDtm; // 수정일시
    private String statusCd; // 상태코드
    @Column(name = "diary_t_cd")
    private String diaryTCd; // 일지/계획
    private Long cropSeq; // 품목일련번호
    private Long userCropSeq; // 품목재배일련번호

//    private Long activityCropSeq; // 작물 번호
    private Long activitySeq; // 작업단계일련번호


    private Double manSelf; // 자가노동인력수(남)
    private Integer manSelfTm; // 자가노동시간(시간,남)
    private Integer manSelfTmm; // 자가노동시간(분,남)
    private Double womanSelf; // 자가노동인력수(여)
    private Integer womanSelfTm; // 자가노동시간(시간,여)
    private Integer womanSelfTmm; // 자가노동시간(분,여)
    private Double manHire; // 고용노동인력수(남)
    private Integer manHireTm; // 고용노동시간(시간,남)
    private Integer manHireTmm; // 고용노동시간(분,남)
    private Double womanHire; // 고용노동인력수(여)
    private Integer womanHireTm; // 고용노동시간(시간,여)
    private Integer womanHireTmm; // 고용노동시간(분,여)

    @Column(name = "sky_t_cd")
    private String skyTCd; // 날씨일련번호
    private Float temp; // 기온
    private Float reh; // 습도
    private Float tmn; // 최저기온
    private Float tmx; // 최고기온
    private Float rnf; // 강수량

    private String memo; // 메모
    private String remark; // 비고
    private String actNm; // 활동명
    private Long unitPack; // 포장단위금액
    @Column(name = "pack_t_cd")
    private String packTCd; // 포장단위구분
    private Double quan; // 수량

    @Column(name = "grade_t_cd")
    private String gradeTCd; // 등급구분

    @Column(name="crop_b_cd")
    private String cropBCd; // 품목 코드

    @JoinColumn(name = "crop_species_seq", foreignKey = @ForeignKey(name = "tf_user_diary_crop_species_seq_fk")
//            ,referencedColumnName = "crop_species_seq"
        )
    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private CropSpecies cropSpecies; // 품종 번호

    private Long cropPatternSeq; // 작형 번호

    // 추후에 JoinColumn 설정 하기
//    private Long cropPatternSeq; // 작형 번호

    public void insertSubInfo(CropSpecies cropSpecies) {
        this.cropSpecies = cropSpecies;
    }



}