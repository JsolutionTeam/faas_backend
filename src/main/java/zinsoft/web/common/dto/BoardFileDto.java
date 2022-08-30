package zinsoft.web.common.dto;

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
public class BoardFileDto {

    @JsonIgnore
    private String boardId;
    @JsonIgnore
    private Long articleSeq;
    private Long fileSeq;

    //@JsonUnwrapped
    //private FileInfoDto fileInfo;
    private Long fileSize;
    private String fileNm;

    public BoardFileDto() {
    }

    public BoardFileDto(String boardId, Long articleSeq) {
        this.boardId = boardId;
        this.articleSeq = articleSeq;
    }

}