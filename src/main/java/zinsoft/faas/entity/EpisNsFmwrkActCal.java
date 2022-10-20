package zinsoft.faas.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name="epis_ns_fmwrk_act_cal",
        catalog = "${catalog.config.bdm}"
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString @Builder
public class EpisNsFmwrkActCal {

    @Id
    @Column(name="seq", insertable = false, updatable = false, nullable = false, length = 11)
    private Long id;

    @Column(name = "FMWRK_ACT_CODE", updatable = false, insertable = false, length = 30)
    private String fmwrkActCode;

    @Column(name = "ROW_GBN")
    private Integer rowGbn;

    @Column(name = "START_MONTH")
    private Integer startMonth;

    @Column(name = "START_QUART")
    private Integer startQuart;

    @Column(name = "END_MONTH")
    private Integer endMonth;

    @Column(name = "END_QUART")
    private Integer endQuart;

    @Column(name = "GROW_STEP_ELY")
    private String growStepEly;

    @Column(name = "GROW_STEP_MID")
    private String growStepMid;

    @Column(name = "GROW_STEP_LST")
    private String growStepLst;

    @Column(name = "FMWRK_NM")
    private String fmwrkNm;

    @Column(name = "FMWRK_DESC", length = 65530)
    private String fmwrkDesc;














}
