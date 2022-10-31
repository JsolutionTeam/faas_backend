package zinsoft.faas.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
public class MgrCropDetailId implements Serializable {
    /*
        code_id로 구분 가능하다.
        작물코드 = 'CROPS_CD'
        대분류 = 'BDM_CROP'
    * */
    @Column(name = "code_id", length = 30)
    private String codeId;

    @Column(name = "code", length = 50)
    private String code;

    public MgrCropDetailId(String codeId, String code) {
        this.codeId = codeId;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MgrCropDetailId taskId1 = (MgrCropDetailId) o;
        return Objects.equals(codeId, taskId1.codeId) && Objects.equals(code, taskId1.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeId, code);
    }
}