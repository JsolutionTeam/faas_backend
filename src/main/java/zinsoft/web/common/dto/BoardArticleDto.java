package zinsoft.web.common.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

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
public class BoardArticleDto {

    private Long articleSeq;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;
    private String boardId;
    private Long catSeq;
    @Size(min = 1, max = 1)
    private String noticeYn;
    @Size(max = 255)
    private String userId;
    @Size(max = 50)
    private String userNm;
    @JsonIgnore
    private String userPwd;
    @Size(max = 255)
    private String emailAddr;
    @NotEmpty
    @Size(max = 255)
    private String subject;
    private Integer commentCnt;
    private Integer fileCnt;
    private Integer readCnt;
    private Integer assentCnt;
    private Integer dissentCnt;
    @NotEmpty
    private String content;

    private String catNm;

    private List<MultipartFile> files;
    private List<Long> delFiles;

    private List<BoardFileDto> fileList;
    private List<BoardCommentDto> commentList;

}