package zinsoft.web.common.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

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
    private String codeId;
    @Id
    private String codeVal;
    private String codeNm;
    private String codeEngNm;
    private String upCodeVal;
    private Integer exprSeq;

}