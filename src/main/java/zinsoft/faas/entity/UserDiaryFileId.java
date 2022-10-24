package zinsoft.faas.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;

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