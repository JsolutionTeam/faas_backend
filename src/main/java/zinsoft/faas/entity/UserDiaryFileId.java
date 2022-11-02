package zinsoft.faas.entity;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDiaryFileId implements Serializable {

    private static final long serialVersionUID = 7483771648542696698L;
    @Column(name="user_diary_seq")
    private Long userDiarySeq;
    @Column(name="file_seq")
    private Long fileSeq;

}