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
@Table(name = "tf_user_crop")
@DynamicInsert
@DynamicUpdate
public class UserCrop{

    @Id
    @GeneratedValue
    private Long userCropSeq;
    private String userId;
//    private String userNm;
    private String year;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private Long cropSeq;
    private double area;
    private String mainKind;
    @Column(name = "crop_s_cd")
    private String cropSCd;
    private String stCrop;
    private String edCrop;
    private String plantYear;
    private long plantNum;
    private String remark;
    private long mother;
    private long young;
    private String zipcode;
    private String addr1;
    private String addr2;

//    private String cropNm;
//    private String cropSCdNm;
//    @Column(name = "crop_a_cd")
//    private String cropACd;

    //private String partTCdNm; // 사업유형명

    @Column(name = "expr_yn")
    private String exprYN; // 표출 여부
    private String aliasNm; // 사용자품목별칭
}
