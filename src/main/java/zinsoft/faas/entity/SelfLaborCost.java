package zinsoft.faas.entity;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_self_labor_cost")
@DynamicInsert
@DynamicUpdate
public class SelfLaborCost {

    @Id
    @GeneratedValue

    @Column(name="self_labor_seq")
    private Long selfLaborSeq;

    @Column(name="year")
    private String year;

    @Column(name="reg_dtm")
    private Date regDtm;

    @Column(name="update_dtm")
    private Date updateDtm;

    @Column(name="status_cd")
    private String statusCd;

    @Column(name="man_amt")
    private Long manAmt;

    @Column(name="woman_amt")
    private Long womanAmt;

}