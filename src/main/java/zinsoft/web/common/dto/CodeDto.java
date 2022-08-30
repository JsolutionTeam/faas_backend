package zinsoft.web.common.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zinsoft.util.KeyValueable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CodeDto implements KeyValueable<String> {

    public static final String CD_T_CD_C = "C";
    public static final String CD_T_CD_D = "D";
    public static final String SLIP_T_CD_C = "1";
    public static final String SLIP_T_CD_D = "2";

    public static final String WORK_CD_INSERT = "I";
    public static final String WORK_CD_UPDATE = "U";
    public static final String WORK_CD_DELETE = "D";

    @NotBlank
    private String codeId;
    private String codeVal;
    private String codeNm;
    private String codeEngNm;
    private String upCodeVal;
    private Integer exprSeq;

    public CodeDto(String codeId, String codeVal, String codeNm) {
        this.codeId = codeId;
        this.codeVal = codeVal;
        this.codeNm = codeNm;
    }

    public CodeDto(String codeId, String codeVal, String codeNm, String codeEngNm) {
        this.codeId = codeId;
        this.codeVal = codeVal;
        this.codeNm = codeNm;
        this.codeEngNm = codeEngNm;
    }

    @Override
    public String getKey() {
        return codeVal;
    }

    @Override
    public String getValue() {
        return codeNm;
    }

}