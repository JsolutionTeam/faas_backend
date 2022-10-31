package zinsoft.web.common.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;

@Getter
@Setter
@ToString
public class BoardFileId implements Serializable {

    private static final long serialVersionUID = 2413002628089633998L;

    @Column(name="board_id")
    private String boardId;

    @Column(name="article_seq")
    private Long articleSeq;

    @Column(name="file_seq")
    private Long fileSeq;

}