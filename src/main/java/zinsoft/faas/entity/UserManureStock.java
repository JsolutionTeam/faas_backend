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
    private Long userManureStockSeq;
    private String userId;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;

    private Long userManureSeq;
    private Long userDiarySeq;
    private Long userInoutSeq;
    private Long cropSeq;
    private Long userCropSeq;

    private String supInoutCd;
    private String inoutDt;
    private Double quan;
    @Column(name="pack_t_cd")
    private String packTCd;
    private Long amt;
    private String remark;

    private String updateYn;
    private String deleteYn;

}