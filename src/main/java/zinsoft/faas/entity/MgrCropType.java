package zinsoft.faas.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

// 품종 조회를 위해 사용
@Entity
@Table(name = "comtccmmncode", catalog = "mgr")
@Getter
public class MgrCropType {

    @Id
    @Column(name = "CODE_ID")
    private String codeId; // 코드 ID(PK)

    @Column(name = "CODE_ID_NM")
    private String codeName; // 코드 명

    @Column(name = "CODE_ID_DC")
    private String codeDescription; // 코드 설명

    @Column(name = "USE_YN")
    private String useYn; // 사용 여부 (Y/N) 그 외 값은 확인해봐야 함

    @Column(name = "CL_CODE")
    private String clCode; // 분류 코드

    @Column(name = "FRST_REGIST_PNTTM")
    private Date createdAt; // 최초등록 시점

    @Column(name = "FRST_REGISTER_ID")
    private String createdBy; // 최초등록자 ID

    @Column(name = "LAST_UPDT_PNTTM")
    private Date updatedAt; // 최종수정 시점

    @Column(name = "LAST_UPDUSR_ID")
    private String updatedBy; // 최종수정자 ID
}
