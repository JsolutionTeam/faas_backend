package zinsoft.faas.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tf_user_inout_file")
@IdClass(UserInoutFileId.class)
@DynamicInsert
@DynamicUpdate
public class UserInoutFile {

    @Id
    @Column(name="user_inout_seq")
    private Long userInoutSeq;
    @Id
    @Column(name="file_seq")
    private Long fileSeq;

    public UserInoutFile(UserInout userInout) {
        this.userInoutSeq = userInout.getUserInoutSeq();
    }

}