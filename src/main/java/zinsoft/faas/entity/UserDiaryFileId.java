package zinsoft.faas.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDiaryFileId implements Serializable {

    private static final long serialVersionUID = 7483771648542696698L;
    private Long userDiarySeq;
    private Long fileSeq;

}