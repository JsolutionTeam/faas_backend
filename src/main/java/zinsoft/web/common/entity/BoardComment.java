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
@Table(name = "tf_board_comment")
@DynamicInsert
@DynamicUpdate
public class BoardComment {

    @Id
    @GeneratedValue
    private Long commentSeq;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private String boardId;
    private Long articleSeq;
    private Integer listOrder;
    private Short depth;
    private Long upCommentSeq;
    private String userId;
    private String userNm;
    private String userPwd;
    private String emailAddr;
    private String content;

}