package zinsoft.web.common.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_code")
@IdClass(CodeId.class)
public class Code implements Serializable {

    private static final long serialVersionUID = -3297568695994405669L;

    @Id
    @Column(name="code_id")
    private String codeId;

    @Id
    @Column(name="code_val")
    private String codeVal;

    @Column(name="code_nm")
    private String codeNm;

    @Column(name="code_eng_nm")
    private String codeEngNm;

    @Column(name="up_code_val")
    private String upCodeVal;

    @Column(name="expr_seq")
    private Integer exprSeq;


}