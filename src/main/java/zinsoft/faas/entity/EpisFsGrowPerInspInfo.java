package zinsoft.faas.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "epis_fs_herv_info")
@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
public class EpisFsGrowPerInspInfo {

    @Id
    @Column(name = "GROW_INSP_INFO_SEQ")
    private Long growInspInfoSeq;

    @JoinColumn(
            name = "HERV_SEQ",
            nullable = false
    )
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private EpisFsHervInfo hervInfo;

    @Column(name = "pulpOutWidth")
    private String pulpOutWidth;
    @Column(name = "fruitLength")
    private String fruitLength;
    @Column(name = "overWgh")
    private String overWgh;
    @Column(name = "FARM_CODE")
    private String farmCode;
    @Column(name = "PLANT_SEQ")
    private String plantSeq;
    @Column(name = "KIND_CODE")
    private String kindCode;
    @Column(name = "fruitCorkYn")
    private String fruitCorkYn;
    @Column(name = "WEEK")
    private String WEEK;
    @Column(name = "flowClusterApperSeasn")
    private String flowClusterApperSeasn;
    @Column(name = "fruitCorkPttrn")
    private String fruitCorkPttrn;
    @Column(name = "flowClusterTop")
    private String flowClusterTop;
    @Column(name = "treWeight")
    private String treWeight;
    @Column(name = "bulbWeght")
    private String bulbWeght;
    @Column(name = "leafVeinLength")
    private String leafVeinLength;
    @Column(name = "fruitCnt")
    private String fruitCnt;
    @Column(name = "nodeLength")
    private String nodeLength;
    @Column(name = "internodeNum")
    private String internodeNum;

    @Column(name = "harvNode")
    private String harvNode;
    @Column(name = "marginLand")
    private String marginLand;

    @Column(name = "floweringNode")
    private String floweringNode;
    @Column(name = "harvPosition")
    private String harvPosition;
    @Column(name = "fruitTree")
    private String fruitTree;
    @Column(name = "flowClusterCnt")
    private String flowClusterCnt;
    @Column(name = "fruitBrix")
    private String fruitBrix;
    @Column(name = "floweringHeight")
    private String floweringHeight;
    @Column(name = "leafLength")
    private String leafLength;
    @Column(name = "leafNumber")
    private String leafNumber;
    @Column(name = "harvOverWgh")
    private String harvOverWgh;
    @Column(name = "cotyHeight")
    private String cotyHeight;
    @Column(name = "stemDiameter")
    private String stemDiameter;
    @Column(name = "fruitsPosition")
    private String fruitsPosition;
    @Column(name = "flowerCount")
    private String flowerCount;
    @Column(name = "floweringSeasn")
    private String floweringSeasn;
    @Column(name = "innerLeafCnt")
    private String innerLeafCnt;
    @Column(name = "deepHeight")
    private String deepHeight;
    @Column(name = "maleFlowerCnt")
    private String maleFlowerCnt;
    @Column(name = "fruitWidth")
    private String fruitWidth;
    @Column(name = "plantHeight")
    private String plantHeight;
    @Column(name = "bloomFlowerCount")
    private String bloomFlowerCount;
    @Column(name = "petioleLength")
    private String petioleLength;
    @Column(name = "YEAR", length = 4)
    private String YEAR;
    @Column(name = "leafHeight")
    private String leafHeight;
    @Column(name = "leafWidth")
    private String leafWidth;
    @Column(name = "cotySize")
    private String cotySize;
    @Column(name = "treFruitWeight")
    private String treFruitWeight;
    @Column(name = "outLeafCnt")
    private String outLeafCnt;
    @Column(name = "harvYn")
    private String harvYn;
    @Column(name = "nodeNum")
    private String nodeNum;
    @Column(name = "harvAvgOver")
    private String harvAvgOver;
    @Column(name = "acidity")
    private String acidity;
    @Column(name = "interNodeLength")
    private String interNodeLength;
    @Column(name = "peduncleWidth")
    private String peduncleWidth;
    @Column(name = "knurCnt")
    private String knurCnt;
    @Column(name = "SUBJ_CODE")
    private String SUBJ_CODE;
    @Column(name = "peduncleLength")
    private String peduncleLength;
    @Column(name = "flowerPosition")
    private String flowerPosition;
    @Column(name = "feFlowerCnt")
    private String feFlowerCnt;
    @Column(name = "fruitSetNodeNum")
    private String fruitSetNodeNum;
    @Column(name = "dmPipe")
    private String dmPipe;
    @Column(name = "cotyWidth")
    private String cotyWidth;
    @Column(name = "floweringCnt")
    private String floweringCnt;
    @Column(name = "culmLength")
    private String culmLength;
    @Column(name = "calyxLength")
    private String calyxLength;
    @Column(name = "treFruitCnt")
    private String treFruitCnt;
    @Column(name = "maxLeafLength")
    private String maxLeafLength;
    @Column(name = "growthLength")
    private String growthLength;
    @Column(name = "fruitNum")
    private String fruitNum;
    @Column(name = "fruitStalk")
    private String fruitStalk;

    @Column(name = "MEASURE_DT")
    private String MEASURE_DT;
    @Column(name = "TRANS_PLANT_DT")
    private Date transPlantDt;
    @Column(name = "REG_ID")
    private String regId;
    @Column(name = "REG_DT")
    private Date regDt;
    @Column(name = "UPD_ID")
    private String updId;
    @Column(name = "UPD_DT")
    private Date updDt;

}
