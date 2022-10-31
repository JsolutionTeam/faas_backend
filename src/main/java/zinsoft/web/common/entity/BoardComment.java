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
@Table(name = "tf_board_comment")
@DynamicInsert
@DynamicUpdate
public class BoardComment {

    @Id
    @GeneratedValue
    @Column(name="comment_seq")
    private Long commentSeq;

    @Column(name="reg_dtm")
    private Date regDtm;

    @Column(name="update_dtm")
    private Date updateDtm;

    @Column(name="status_cd")
    private String statusCd;

    @Column(name="board_id")
    private String boardId;

    @Column(name="article_seq")
    private Long articleSeq;

    @Column(name="list_order")
    private Integer listOrder;

    @Column(name="depth")
    private Short depth;

    @Column(name="up_comment_seq")
    private Long upCommentSeq;

    @Column(name="user_id")
    private String userId;

    @Column(name="user_nm")
    private String userNm;

    @Column(name="user_pwd")
    private String userPwd;

    @Column(name="email_addr")
    private String emailAddr;

    @Column(name="content")
    private String content;


}