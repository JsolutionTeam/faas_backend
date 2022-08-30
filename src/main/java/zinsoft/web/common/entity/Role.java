package zinsoft.web.common.entity;

import java.util.Date;

import javax.persistence.Entity;
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
@Table(name = "tf_role")
@DynamicInsert
@DynamicUpdate
public class Role {

    @Id
    private String roleId;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private String roleNm;
    private Integer exprSeq;

}