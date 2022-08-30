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
@Table(name = "tf_user_chemical_stock")
@DynamicInsert
@DynamicUpdate
public class UserChemicalStock {
    @Id
    @GeneratedValue
    private Long userChemicalStockSeq;
    private String userId;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;

    private Long userChemicalSeq;
    private Long userDiarySeq;
    private Long userInoutSeq;
    private Long cropSeq;
    private Long userCropSeq;
    private String supInoutCd;
    private String inoutDt;

    @Column(name="pack_t_cd")
    private String packTCd;

    private Double quan;
    private Long amt;
    private String remark;

    private String updateYn;
    private String deleteYn;
}