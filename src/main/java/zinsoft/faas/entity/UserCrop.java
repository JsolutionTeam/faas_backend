package zinsoft.faas.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_user_crop")
@DynamicInsert
@DynamicUpdate
public class UserCrop{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_crop_seq")
    private Long userCropSeq;
    @Column(name="user_id")
    private String userId;
//    private String userNm;
    @Column(name="year")
    private String year;
    @Column(name="reg_dtm")
    private Date regDtm;
    @Column(name="update_dtm")
    private Date updateDtm;
    @Column(name="status_cd")
    private String statusCd;
    @Column(name="crop_seq")
    private Long cropSeq;
    @Column(name="area")
    private double area;
    @Column(name="main_kind")
    private String mainKind;
    @Column(name = "crop_s_cd")
    private String cropSCd;
    @Column(name="st_crop")
    private String stCrop;
    @Column(name="ed_crop")
    private String edCrop;
    @Column(name="plant_year")
    private String plantYear;
    @Column(name="plant_num")
    private long plantNum;
    @Column(name="remark")
    private String remark;
    @Column(name="mother")
    private long mother;
    @Column(name="young")
    private long young;
    @Column(name="zipcode")
    private String zipcode;
    @Column(name="addr1")
    private String addr1;
    @Column(name="addr2")
    private String addr2;

//    private String cropNm;
//    private String cropSCdNm;
//    @Column(name = "crop_a_cd")
//    private String cropACd;

    //private String partTCdNm; // 사업유형명

    @Column(name = "expr_yn")
    private String exprYN; // 표출 여부
    @Column(name="alias_nm")
    private String aliasNm; // 사용자품목별칭
}
