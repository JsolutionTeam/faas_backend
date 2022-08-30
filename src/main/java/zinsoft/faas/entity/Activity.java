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
@Table(name = "tf_activity")
@DynamicInsert
@DynamicUpdate
public class Activity {

    @Id
    @GeneratedValue
    private Long activitySeq;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    @Column(name = "crop_a_cd")
    private String cropACd;
    private String actNm;
    private String exprYn;
    private Long exprSeq;

}