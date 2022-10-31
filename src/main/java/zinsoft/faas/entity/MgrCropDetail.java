package zinsoft.faas.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name="comtccmmndetailcode", catalog = "mgr")
@Getter
public class MgrCropDetail {

    @EmbeddedId
    private MgrCropDetailId id;

    @Column(name="code_nm", length = 255)
    private String codeNm;

}
