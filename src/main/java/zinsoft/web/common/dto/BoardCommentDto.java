package zinsoft.web.common.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class BoardCommentDto {

    private Long commentSeq;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;
    @JsonIgnore
    private String boardId;
    private Long articleSeq;
    @JsonIgnore
    private Integer listOrder;
    private Short depth;
    @JsonIgnore
    private Long upCommentSeq;
    @Size(max = 255)
    private String userId;
    @Size(max = 50)
    private String userNm;
    @JsonIgnore
    private String userPwd;
    @Size(max = 255)
    private String emailAddr;
    @NotEmpty
    private String content;

    public BoardCommentDto() {
    }

    public BoardCommentDto(Long commentSeq, String boardId, Long articleSeq) {
        this.commentSeq = commentSeq;
        this.boardId = boardId;
        this.articleSeq = articleSeq;
    }

}