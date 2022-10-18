package zinsoft.faas.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="crop_code", catalog = "smartfarm") // 같은 db안에있는 다른 schema 사용
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmartfarmCrop {
    @Id
    @Column(insertable = false, updatable = false)
    private Long idx;

    // 대분류
    @Column(name = "depth1", length = 2)
    private String depth1;

    @Column(name = "depth1_code", length = 100)
    private String code1;

    @Column(name = "depth2", length = 2)
    private String depth2;

    @Column(name = "depth2_code", length = 100)
    private String code2;

    @Column(name = "depth3", length = 2)
    private String depth3;

    @Column(name = "depth3_code", length = 100)
    private String code3;

    /* 작물 코드 */
    @Column(name="crop_code", length = 8)
    private String cropCode;
}
