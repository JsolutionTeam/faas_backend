package zinsoft.web.common.entity;

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
@Table(name = "tf_board_category")
@DynamicInsert
@DynamicUpdate
public class BoardCategory {

    @Id
    @GeneratedValue
    private Long catSeq;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private String boardId;
    private String catNm;
    private Short exprSeq;

}