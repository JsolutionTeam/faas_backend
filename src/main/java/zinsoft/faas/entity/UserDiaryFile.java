package zinsoft.faas.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_user_diary_file")
@IdClass(UserDiaryFileId.class)
@DynamicInsert
@DynamicUpdate
public class UserDiaryFile {
    @Id
    @Column(name="user_diary_seq")
    private Long userDiarySeq;
    @Id
    @Column(name="file_seq")
    private Long fileSeq;
    @Column(name = "file_k_cd")
    private String fileKCd;

}