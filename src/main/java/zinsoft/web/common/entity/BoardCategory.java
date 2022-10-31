package zinsoft.web.common.entity;

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
@Table(name = "tf_board_category")
@DynamicInsert
@DynamicUpdate
public class BoardCategory {

    @Id
    @GeneratedValue
    @Column(name = "cat_seq")
    private Long catSeq;

    @Column(name = "reg_dtm")
    private Date regDtm;

    @Column(name = "update_dtm")
    private Date updateDtm;

    @Column(name = "status_cd")
    private String statusCd;

    @Column(name = "board_id")
    private String boardId;

    @Column(name = "cat_nm")
    private String catNm;

    @Column(name = "expr_seq")
    private Short exprSeq;

}