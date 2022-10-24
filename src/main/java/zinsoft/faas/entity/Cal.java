package zinsoft.faas.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_cal")
@IdClass(CalId.class)
public class Cal implements Serializable {

    private static final long serialVersionUID = 7251997391820800197L;

    public static final String CAL_T_CD_LUNAR_DAY = "0";
    public static final String CAL_T_CD_SOLAR_TERM = "1";

    @Id
    @Column(name="cal_dt")
    private String calDt;
    
    @Id
    @Column(name = "cal_t_cd")
    private String calTCd;
    
    @Column(name="expr_nm")
    private String exprNm;

}