package zinsoft.faas.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

// 작기명(품목) 조회를 위해 사용
@Entity
@Table(name="comtccmmndetailcode", catalog = "mgr")
@Getter
public class MgrCropDetail {

    @EmbeddedId
    private MgrCropDetailId id;

    @Column(name="code_nm", length = 255)
    private String codeNm;

}
