package zinsoft.faas.entity;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserInoutFileId implements Serializable {

    private static final long serialVersionUID = 3243831120064416691L;
    @Column(name="user_inout_seq")
    private Long userInoutSeq;
    @Column(name="file_seq")
    private Long fileSeq;

}