package zinsoft.faas.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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