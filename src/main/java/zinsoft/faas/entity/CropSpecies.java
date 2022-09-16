package zinsoft.faas.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_crop_species")
@DynamicInsert
@DynamicUpdate
// 품목-품종
public class CropSpecies {

    @Id
    @GeneratedValue
    private Long cropSpeciesSeq;

    @Column(name = "name", nullable = false)
    private String name = "";

    @JoinColumn(
            name = "cropSeq",
            nullable = false,
            foreignKey = @ForeignKey(name = "tf_crop_species_crop_seq_FK")
    )
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Crop crop;

    private Date regDtm; // 등록일
    private Date updateDtm; // 수정일

}