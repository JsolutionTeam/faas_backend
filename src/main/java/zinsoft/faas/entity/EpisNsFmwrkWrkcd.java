package zinsoft.faas.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(
        name="epis_ns_fmwrk_wrkcd",
        catalog = "bdm"
        // MultiSchemaPhysicalNamingStrategy 파일에서 커스텀함.
        //application-{profiles}.properties 파일 에서 catalog.config.{name} 값 가져옴
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class EpisNsFmwrkWrkcd {
    @Id
    @Column(name = "fmwrk_cd", length = 10, nullable = false, updatable = false, insertable = false)
    private String fmwrkCd;

    @Column(name = "cropcd", length = 50, nullable = false, updatable = false, insertable = false)
    private String cropCd;

    @Column(name = "fmwrk_nm", length = 50, nullable = false, updatable = false, insertable = false)
    private String fmwrkNm;

    @Column(name = "grow_step", length = 10, nullable = false, updatable = false, insertable = false)
    private String growStep;

    @Column(name = "reg_id", length = 30, nullable = false, updatable = false, insertable = false)
    private String regId;

    @Column(name = "upd_id", length = 30, nullable = false, updatable = false, insertable = false)
    private String updId;

    @Column(name = "reg_dt", nullable = false, updatable = false, insertable = false)
    private Date regDt;

    @Column(name = "upd_dt", nullable = false, updatable = false, insertable = false)
    private Date updDt;
}
