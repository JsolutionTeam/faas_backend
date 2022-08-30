package zinsoft.web.common.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tf_board_file")
@IdClass(BoardFileId.class)
public class BoardFile implements Serializable {

    private static final long serialVersionUID = -8993694256247778052L;

    @Id
    private String boardId;
    @Id
    private Long articleSeq;
    @Id
    private Long fileSeq;

    //@OneToOne()
    //@JoinColumn(name = "fileSeq", insertable = false, updatable = false)
    //@Where(clause = "status_cd = 'N'")
    //private FileInfo fileInfo;

    public BoardFile(String boardId, Long articleSeq) {
        this.boardId = boardId;
        this.articleSeq = articleSeq;
    }

}