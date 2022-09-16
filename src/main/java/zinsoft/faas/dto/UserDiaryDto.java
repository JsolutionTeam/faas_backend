package zinsoft.faas.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zinsoft.faas.entity.UserDiary;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class UserDiaryDto {

    public static final String DIARY_T_CD_PLAN = "P";//계획
    public static final String DIARY_T_CD_DIARY = "D";//일지

    private Long userDiarySeq;
    @ApiModelProperty(notes = "사용자아이디")
    private String userId;
    @NotBlank
    @ApiModelProperty(notes = "날짜 YYYYMMDD")
    @Pattern(regexp = "^\\d{4}-?(0[1-9]|1[012])-?(0[1-9]|[12][0-9]|3[01])$")
    private String actDt;
    @JsonIgnore
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;
    @NotBlank
    @ApiModelProperty(notes = "일지(D)/계획(P) 구분코드")
    @Pattern(regexp = "^(P|D)$")
    private String diaryTCd;
    //    @NotNull
//    @Min(value = 1)
//    @ApiModelProperty(notes = "품목일련번호")
    private Long cropSeq;
    //    @NotNull
//    @Min(value = 1)
//    @ApiModelProperty(notes = "사용자품목일련번호")
    private Long userCropSeq;

    // 영농일지 품목, 품종, 작형 데이터 바인딩 추가
    @Schema(name = "품목 번호")
    @Min(value = 1)
    private String cropBCd;

    @Schema(name = "품목 명")
    private String cropBCdNm;

    @Schema(name = "품종 명")
    @Min(value = 1)
    private String cropSpeciesNm;

    @Schema(name = "작형 번호")
    @Min(value = 1)
    private Long cropPatternSeq; // 작형 번호

    @Schema(name = "작형 명")
    @Min(value = 1)
    private String cropPatternNm; // 작형 번호

    @ApiModelProperty(notes = "작업단계일련번호")
    @Min(value = 1)
    private Long activitySeq;

    @ApiModelProperty(notes = "자가노동인력수(남)")
    private Double manSelf;

    @Digits(integer = 2, fraction = 0)
    @Min(1)
    @Max(24)
    @ApiModelProperty(notes = "자가노동시간(시간,남)")
    private Integer manSelfTm;
    @Digits(integer = 2, fraction = 0)
    @Min(1)
    @Max(60)
    @ApiModelProperty(notes = "자가노동시간(분,남)")
    private Integer manSelfTmm;
    @ApiModelProperty(notes = "자가노동인력수(여)")
    private Double womanSelf;
    @Digits(integer = 2, fraction = 0)
    @Min(1)
    @Max(24)
    @ApiModelProperty(notes = "자가노동시간(시간,여)")
    private Integer womanSelfTm;
    @Digits(integer = 2, fraction = 0)
    @Min(1)
    @Max(60)
    @ApiModelProperty(notes = "자가노동시간(분,여)")
    private Integer womanSelfTmm;
    @ApiModelProperty(notes = "고용노동인력수(남)")
    private Double manHire;
    @Digits(integer = 2, fraction = 0)
    @Min(1)
    @Max(24)
    @ApiModelProperty(notes = "고용노동시간(시간,남)")
    private Integer manHireTm;
    @Digits(integer = 2, fraction = 0)
    @Min(1)
    @Max(60)
    @ApiModelProperty(notes = "고용노동시간(분,남)")
    private Integer manHireTmm;
    @ApiModelProperty(notes = "고용노동인력수(여)")
    private Double womanHire;
    @Digits(integer = 2, fraction = 0)
    @Min(1)
    @Max(24)
    @ApiModelProperty(notes = "고용노동시간(시간,여)")
    private Integer womanHireTm;
    @Digits(integer = 2, fraction = 0)
    @Min(1)
    @Max(60)
    @ApiModelProperty(notes = "고용노동시간(분,여)")
    private Integer womanHireTmm;

    @ApiModelProperty(notes = "날씨코드")
    private String skyTCd;
    @Digits(integer = 2, fraction = 2)
    @ApiModelProperty(notes = "최저기온")
    private Float tmn;
    @Digits(integer = 2, fraction = 2)
    @ApiModelProperty(notes = "최고기온")
    private Float tmx;
    @Digits(integer = 4, fraction = 2)
    @ApiModelProperty(notes = "강수량")
    private Float rnf;
    @Digits(integer = 2, fraction = 2)
    @ApiModelProperty(notes = "기온")
    private Float temp;
    @Digits(integer = 3, fraction = 2)
    @Max(100)
    @ApiModelProperty(notes = "습도")
    private Float reh;

    @ApiModelProperty(notes = "참고")
    private String remark;
    @ApiModelProperty(notes = "활동명")
    private String actNm;
    @ApiModelProperty(notes = "포장단위")
    @Min(value = 0)
    @Digits(integer = 8, fraction = 2)
    private Long unitPack;
    @ApiModelProperty(notes = "포장단위구분코드")
    private String packTCd;
    @ApiModelProperty(notes = "수량")
    @Min(value = 0)
    @Digits(integer = 8, fraction = 0)
    private Double quan;

    @ApiModelProperty(notes = "사용자명")
    private String userNm;
    @ApiModelProperty(notes = "일지/계획코드명")
    private String diaryTCdNm;
    //    @ApiModelProperty(notes = "품목명")
//    private String cropNm;
    @ApiModelProperty(notes = "날씨코드명")
    private String skyTCdNm;
    @ApiModelProperty(notes = "포장단위구분코드명")
    private String packTCdNm;
    @ApiModelProperty(notes = "등급구분코드")
    private String gradeTCd;
    @ApiModelProperty(notes = "등급구분코드명")
    private String gradeTCdNm;
    //private Long activityCnt;

    @ApiModelProperty(notes = "영농활동사진일련번호")
    private List<Long> workingFileSeqs;
    //    private List<Long> receiptFileSeqs;
    @ApiModelProperty(notes = "영농활동사진")
    private Map<Long, String> workingFiles;
    //    private Map<Long, String> receiptFiles;

    @JsonIgnore
    private List<Long> deleteWorkingFileSeqs;
    @JsonIgnore
    private List<MultipartFile> working;

    @JsonIgnore
    //수입내용, 엑셀다운로드시 사용
    private List<UserInoutDto> userInList;
    @JsonIgnore
    //지출내용, 엑셀다운로드시 사용
    private List<UserInoutDto> userOutList;

    private String addr;
    private String emailAddr;

    @ApiModelProperty(notes = "사용농약")
    private List<UserChemicalStockDto> chemicalList;
    @ApiModelProperty(notes = "사용비료")
    private List<UserManureStockDto> manureList;
    @ApiModelProperty(notes = "품목별칭")
    private String userCropAliasNm;
    @ApiModelProperty(notes = "작업단계차수")
    private Long inning;
    @ApiModelProperty(notes = "작업단계카운트")
    private Long actCnt;

    @QueryProjection
    public UserDiaryDto(UserDiary userDiary
            , String cropBCdNm
            , String cropSpeciesNm
            , String cropPatternNm
            , String diaryTCdNm
            , String skyTCdNm
            , String packTCdNm
            , String gradeTCdNm
            , Long actCnt
            , String userNm
            , String addr
            , String emailAddr) {
        this.userDiarySeq = userDiary.getUserDiarySeq();
        this.userId = userDiary.getUserId();
        this.actDt = userDiary.getActDt();
        this.regDtm = userDiary.getRegDtm();
        this.updateDtm = userDiary.getUpdateDtm();
        this.statusCd = userDiary.getStatusCd();
        this.diaryTCd = userDiary.getDiaryTCd();
        this.cropSeq = userDiary.getCropSeq();
        this.userCropSeq = userDiary.getUserCropSeq();
        this.cropBCd = userDiary.getCropBCd();
        this.cropPatternSeq = userDiary.getCropPatternSeq();
        this.activitySeq = userDiary.getActivitySeq();
        this.manSelf = userDiary.getManSelf();
        this.manSelfTm = userDiary.getManSelfTm();
        this.manSelfTmm = userDiary.getManSelfTmm();
        this.womanSelf = userDiary.getWomanSelf();
        this.womanSelfTm = userDiary.getWomanSelfTm();
        this.womanSelfTmm = userDiary.getWomanSelfTmm();
        this.manHire = userDiary.getManHire();
        this.manHireTm = userDiary.getManHireTm();
        this.manHireTmm = userDiary.getManHireTmm();
        this.womanHire = userDiary.getWomanHire();
        this.womanHireTm = userDiary.getWomanHireTm();
        this.womanHireTmm = userDiary.getWomanHireTmm();
        this.skyTCd = userDiary.getSkyTCd();
        this.tmn = userDiary.getTmn();
        this.tmx = userDiary.getTmx();
        this.rnf = userDiary.getRnf();
        this.temp = userDiary.getTemp();
        this.reh = userDiary.getReh();
        this.remark = userDiary.getRemark();
        this.actNm = userDiary.getActNm();
        this.unitPack = userDiary.getUnitPack();
        this.packTCd = userDiary.getPackTCd();
        this.quan = userDiary.getQuan();
        this.gradeTCd = userDiary.getGradeTCd();

        this.cropBCdNm = cropBCdNm;
        this.cropSpeciesNm = cropSpeciesNm;
        this.cropPatternNm = cropPatternNm;

        this.diaryTCdNm = diaryTCdNm;
        this.skyTCdNm = skyTCdNm;
        this.packTCdNm = packTCdNm;
        this.gradeTCdNm = gradeTCdNm;
        this.actCnt = actCnt;
        this.userNm = userNm;
        this.addr = addr;
        this.emailAddr = emailAddr;
    }

    public UserDiaryDto() {
    }

    public UserDiaryDto(Long userDiarySeq, String userId) {
        this.userDiarySeq = userDiarySeq;
        this.userId = userId;
    }

}