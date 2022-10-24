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
@Table(name = "tf_user_manure_stock")
@DynamicInsert
@DynamicUpdate
public class UserManureStock {

    @Id
    @GeneratedValue
    @Column(name="user_manure_stock_seq")
    private Long userManureStockSeq;
    @Column(name="user_id")
    private String userId;
    @Column(name="reg_dtm")
    private Date regDtm;
    @Column(name="update_dtm")
    private Date updateDtm;
    @Column(name="status_cd")
    private String statusCd;

    @Column(name="user_manure_seq")
    private Long userManureSeq;
    @Column(name="user_diary_seq")
    private Long userDiarySeq;
    @Column(name="user_inout_seq")
    private Long userInoutSeq;
    @Column(name="crop_seq")
    private Long cropSeq;
    @Column(name="user_crop_seq")
    private Long userCropSeq;

    @Column(name="sup_inout_cd")
    private String supInoutCd;
    @Column(name="inout_dt")
    private String inoutDt;
    @Column(name="quan")
    private Double quan;
    @Column(name="pack_t_cd")
    private String packTCd;
    @Column(name="amt")
    private Long amt;
    @Column(name="remark")
    private String remark;

    @Column(name="update_yn")
    private String updateYn;
    @Column(name="delete_yn")
    private String deleteYn;

}