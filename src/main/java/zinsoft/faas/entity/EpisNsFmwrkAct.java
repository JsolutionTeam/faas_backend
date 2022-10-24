package zinsoft.faas.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(
        name = "epis_ns_fmwrk_act",
        catalog = "bdm"
        // MultiSchemaPhysicalNamingStrategy 파일에서 커스텀함.
        //application-{profiles}.properties 파일 에서 catalog.config.{name} 값 가져옴
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class EpisNsFmwrkAct {

    @Id
    @Column(name = "fmwrk_act_code", updatable = false, insertable = false, length = 30)
    private String fmwrkActCode;

    @Column(name = "cropcd", length = 50, insertable = false, updatable = false)
    private String cropCde;

    @Column(name = "reg_id", length = 30, insertable = false, updatable = false)
    private String regId;

    @Column(name = "reg_dt", insertable = false, updatable = false)
    private Date regDt;

    @Column(name = "upd_id", length = 30, insertable = false, updatable = false)
    private String updId;

    @Column(name = "upd_dt", insertable = false, updatable = false)
    private Date updDt;
}
