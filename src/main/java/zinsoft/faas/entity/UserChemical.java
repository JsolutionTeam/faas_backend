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
@Table(name = "tf_user_chemical")
@DynamicInsert
@DynamicUpdate
public class UserChemical {
    @Id
    @GeneratedValue
    private Long userChemicalSeq;
    private String userId;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private String cropNm;
    private String userChemicalNm;

    @Column(name="pack_t_cd")
    private String packTCd;

    @Column(name="chemical_t_cd")
    private String chemicalTCd;

    private String insect;
    private String productNm;
    private String chemicalNm;
    private String makerNm;
    private String remark;

}