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
public class UserInoutFileId implements Serializable {

    private static final long serialVersionUID = 3243831120064416691L;
    private Long userInoutSeq;
    private Long fileSeq;

}