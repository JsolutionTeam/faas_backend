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
@Table(name = "tf_user_emp_cost")
@DynamicInsert
@DynamicUpdate
public class UserEmpCost {

    @Id
    @GeneratedValue
    @Column(name="user_emp_cost_seq")
    private Long userEmpCostSeq;
    @Column(name="user_id")
    private String userId;
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
    @Column(name="year")
    private String year;
}