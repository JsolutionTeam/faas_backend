package zinsoft.faas.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CalId implements Serializable {

    private static final long serialVersionUID = 6968082950928586438L;

    @Column(name="cal_dt")
    private String calDt;
    @Column(name="cal_t_cd")
    private String calTCd;

}