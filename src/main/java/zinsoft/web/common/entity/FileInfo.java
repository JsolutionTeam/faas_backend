package zinsoft.web.common.entity;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_file_info")
@DynamicInsert
@DynamicUpdate
public class FileInfo {

    @Id
    @GeneratedValue
    @Column(name="file_seq")
    private Long fileSeq;
    
    @Column(name="reg_dtm")
    private Date regDtm;
    
    @Column(name="update_dtm")
    private Date updateDtm;
    
    @Column(name="status_cd")
    private String statusCd;
    
    @Column(name="user_id")
    private String userId;
    
    @Column(name="saved_nm")
    private String savedNm;
    
    @Column(name="file_size")
    private Long fileSize;
    
    @Column(name="file_nm")
    private String fileNm;
    
    @Column(name="content_type")
    private String contentType;

    
}