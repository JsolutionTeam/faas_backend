package zinsoft.faas.entity;

import java.util.Date;

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
@Table(name = "tf_self_labor_cost")
@DynamicInsert
@DynamicUpdate
public class SelfLaborCost {

    @Id
    @GeneratedValue
    private Long selfLaborSeq;
    private String year;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private Long manAmt;
    private Long womanAmt;

}