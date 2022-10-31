package zinsoft.web.common.entity;

import java.io.Serializable;

import javax.persistence.*;

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
    @Column(name="board_id")
    private String boardId;

    @Id
    @Column(name="article_seq")
    private Long articleSeq;

    @Id
    @Column(name="file_seq")
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