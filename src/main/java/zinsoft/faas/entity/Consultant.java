package zinsoft.faas.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="consulting_data", catalog = "sangju_adv")
@Getter
public class Consultant {

    @Id
    @Column(name="idx", insertable = false, updatable = false, length = 11, nullable = false)
    private Long idx;

    @Column(name="memid", insertable = false, updatable = false, length = 20)
    private String memId;

    @Column(name="farmerno", insertable = false, updatable = false, length = 50)
    private String farmerNo;

    @Column(name="cultno", insertable = false, updatable = false, length = 50)
    private String cultNo;

    @Column(name="startdate", insertable = false, updatable = false)
    private Date startdate;

    @Column(name="enddate", insertable = false, updatable = false)
    private Date endDate;

    @Column(name="workorder", insertable = false, updatable = false, length = 2000)
    private String workOrder;

}
