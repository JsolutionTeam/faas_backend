package zinsoft.faas.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "epis_fs_herv_info")
@Getter
@Setter
@ToString
public class EpisFsHervInfo {
    @Id
    @Column(name = "herv_seq", insertable = false, updatable = false, length = 38)
    private Long hervSeq;

    @Column(name = "HERV_NAME")
    private String hervName;

    @Column(name = "FARM_CODE")
    private String farmCode;

    @Column(name = "pestikorname")
    private String pestiKorName;

    @Column(name = "KIND_CODE")
    private String kindCode;

    @Column(name = "GROW_TYPE")
    private String growType;

    @Column(name = "TRANS_PLANT_DT")
    private Date transPlantDt;

    @Column(name = "CULT_AREA_PY")
    private String cultAreaPy;

    @Column(name = "CULT_AREA_M")
    private String cultAreaM;

    @Column(name = "CROPS_CNT")
    private String cropsCnt;

    @Column(name = "CROPS_M_CNT")
    private String cropsMCnt;

    @Column(name = "TEMP")
    private String temp;

    @Column(name = "HEAVY")
    private String heavy;

    @Column(name = "SUNLIGHT")
    private String sunLight;

    @Column(name = "META_RATE")
    private String metaRate;

    @Column(name = "INSP_MODEL")
    private String inspModel;


    @Column(name = "REG_ID")
    private String regId;

    @Column(name = "REG_DT")
    private Date regDt;

    @Column(name = "UPD_ID")
    private String updId;

    @Column(name = "UPD_DT")
    private Date updDt;

    @Column(name = "END_DT")
    private Date endDt;
}
