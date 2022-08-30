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
@Table(name = "tf_user_emp_cost")
@DynamicInsert
@DynamicUpdate
public class UserEmpCost {

    @Id
    @GeneratedValue
    private Long userEmpCostSeq;
    private String userId;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private Long manAmt;
    private Long womanAmt;
    private String year;
}