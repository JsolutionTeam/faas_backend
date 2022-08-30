package zinsoft.web.common.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardFileId implements Serializable {

    private static final long serialVersionUID = 2413002628089633998L;

    private String boardId;
    private Long articleSeq;
    private Long fileSeq;

}