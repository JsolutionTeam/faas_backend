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
@Table(name = "tf_activity")
@DynamicInsert
@DynamicUpdate
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="activity_seq")
    private Long activitySeq;

    @Column(name="reg_dtm")
    private Date regDtm;

    @Column(name="update_dtm")
    private Date updateDtm;

    @Column(name="status_cd")
    private String statusCd;

    @Column(name = "crop_a_cd")
    private String cropACd;

    @Column(name="act_nm")
    private String actNm;

    @Column(name="expr_yn")
    private String exprYn;

    @Column(name="expr_seq")
    private Long exprSeq;


}