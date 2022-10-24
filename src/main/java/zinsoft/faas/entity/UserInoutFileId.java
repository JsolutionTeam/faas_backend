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
public class UserInoutFileId implements Serializable {

    private static final long serialVersionUID = 3243831120064416691L;
    @Column(name="user_inout_seq")
    private Long userInoutSeq;
    @Column(name="file_seq")
    private Long fileSeq;

}