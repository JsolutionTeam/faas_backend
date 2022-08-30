package zinsoft.web.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "tf_file_info")
@DynamicInsert
@DynamicUpdate
public class FileInfo {

    @Id
    @GeneratedValue
    private Long fileSeq;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private String userId;
    private String savedNm;
    private Long fileSize;
    private String fileNm;
    private String contentType;

}