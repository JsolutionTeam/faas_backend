package zinsoft.faas.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name="epis_ns_fmwrk_act_cal",
        catalog = "bdm"
        // MultiSchemaPhysicalNamingStrategy 파일에서 커스텀함.
        //application-{profiles}.properties 파일 에서 catalog.config.{name} 값 가져옴
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString @Builder
public class EpisNsFmwrkActCal {

    @Id
    @Column(name="seq", insertable = false, updatable = false, nullable = false, length = 11)
    private Long id;

    @Column(name = "fmwrk_act_code", updatable = false, insertable = false, length = 30)
    private String fmwrkActCode;

    @Column(name = "row_gbn")
    private Integer rowGbn;

    @Column(name = "start_month")
    private Integer startMonth;

    @Column(name = "start_quart")
    private Integer startQuart;

    @Column(name = "end_month")
    private Integer endMonth;

    @Column(name = "end_quart")
    private Integer endQuart;

    @Column(name = "grow_step_ely")
    private String growStepEly;

    @Column(name = "grow_step_mid")
    private String growStepMid;

    @Column(name = "grow_step_lst")
    private String growStepLst;

    @Column(name = "fmwrk_nm")
    private String fmwrkNm;

    @Column(name = "fmwrk_desc", length = 65530)
    private String fmwrkDesc;














}
