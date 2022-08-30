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
@Table(name = "tf_board_article")
@DynamicInsert
@DynamicUpdate
public class BoardArticle {

    @Id
    @GeneratedValue
    private Long articleSeq;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private String boardId;
    private Long catSeq;
    private String noticeYn;
    private String userId;
    private String userNm;
    private String userPwd;
    private String emailAddr;
    private String subject;
    private String content;
    private Integer commentCnt;
    private Integer fileCnt;
    private Integer readCnt;
    private Integer assentCnt;
    private Integer dissentCnt;

    //@OneToMany(mappedBy = "articleSeq")
    //@Where(clause = "status_cd = 'N'")
    //private List<BoardComment> commentList;

    //@OneToOne()
    //@JoinColumn(name = "catSeq", insertable = false, updatable = false)
    //@Where(clause = "status_cd = 'N'")
    //private BoardCategory boardCategory;

}