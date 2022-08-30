package zinsoft.faas.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private Long userInoutSeq;
    @Id
    private Long fileSeq;

    public UserInoutFile(UserInout userInout) {
        this.userInoutSeq = userInout.getUserInoutSeq();
    }

}