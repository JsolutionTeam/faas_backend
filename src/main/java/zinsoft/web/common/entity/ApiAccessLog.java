package zinsoft.web.common.entity;

import java.util.Date;

import javax.persistence.*;

import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class ApiAccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "api_access_log_seq")
    private Long apiAccessLogSeq;

    @Column(name = "access_dtm")
    private Date accessDtm;

    @Column(name = "path")
    private String path;

    @Column(name = "method")
    private String method;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "remote_addr")
    private String remoteAddr;

    @Column(name = "note")
    private String note;

    public ApiAccessLog(String path, String method, String userId, String remoteAddr, String note) {
        this.path = path;
        this.method = method;
        this.userId = userId;
        this.remoteAddr = remoteAddr;
        this.note = note;
    }

}