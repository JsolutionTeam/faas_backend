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
@Table(name = "tf_board_article")
@DynamicInsert
@DynamicUpdate
public class BoardArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_seq")
    private Long articleSeq;

    @Column(name = "reg_dtm")
    private Date regDtm;

    @Column(name = "update_dtm")
    private Date updateDtm;

    @Column(name = "status_cd")
    private String statusCd;

    @Column(name = "board_id")
    private String boardId;

    @Column(name = "cat_seq")
    private Long catSeq;

    @Column(name = "notice_yn")
    private String noticeYn;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_nm")
    private String userNm;

    @Column(name = "user_pwd")
    private String userPwd;

    @Column(name = "email_addr")
    private String emailAddr;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

    @Column(name = "comment_cnt")
    private Integer commentCnt;

    @Column(name = "file_cnt")
    private Integer fileCnt;

    @Column(name = "read_cnt")
    private Integer readCnt;

    @Column(name = "assent_cnt")
    private Integer assentCnt;

    @Column(name = "dissent_cnt")
    private Integer dissentCnt;

    //@OneToMany(mappedBy = "articleSeq")
    //@Where(clause = "status_cd = 'N'")
    //private List<BoardComment> commentList;

    //@OneToOne()
    //@JoinColumn(name = "catSeq", insertable = false, updatable = false)
    //@Where(clause = "status_cd = 'N'")
    //private BoardCategory boardCategory;

}