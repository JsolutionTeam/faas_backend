package zinsoft.web.common.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CodeId implements Serializable {

    private static final long serialVersionUID = 1422631314523019537L;



    @Column(name="code_id")
    private String codeId;

    @Column(name="code_val")
    private String codeVal;

}