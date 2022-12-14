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
@Table(name = "tf_user_chemical")
@DynamicInsert
@DynamicUpdate
public class UserChemical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_chemical_seq")
    private Long userChemicalSeq;
    @Column(name="user_id")
    private String userId;
    @Column(name="reg_dtm")
    private Date regDtm;
    @Column(name="update_dtm")
    private Date updateDtm;
    @Column(name="status_cd")
    private String statusCd;
    @Column(name="crop_nm")
    private String cropNm;
    @Column(name="user_chemical_nm")
    private String userChemicalNm;

    @Column(name="pack_t_cd")
    private String packTCd;

    @Column(name="chemical_t_cd")
    private String chemicalTCd;

    @Column(name="insect")
    private String insect;
    @Column(name="product_nm")
    private String productNm;
    @Column(name="chemical_nm")
    private String chemicalNm;
    @Column(name="maker_nm")
    private String makerNm;
    @Column(name="remark")
    private String remark;

}