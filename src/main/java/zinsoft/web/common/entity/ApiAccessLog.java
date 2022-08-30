package zinsoft.web.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_api_access_log")
@DynamicInsert
public class ApiAccessLog {

    @Id
    @GeneratedValue
    private Long apiAccessLogSeq;
    private Date accessDtm;
    private String path;
    private String method;
    private String userId;
    private String remoteAddr;
    private String note;

    public ApiAccessLog(String path, String method, String userId, String remoteAddr, String note) {
        this.path = path;
        this.method = method;
        this.userId = userId;
        this.remoteAddr = remoteAddr;
        this.note = note;
    }

}