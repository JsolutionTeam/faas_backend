package zinsoft.faas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class AccountDetailDto {

    private Long acDetailSeq;
    private String acDetailNm;
    private String acId;
    private Long exprSeq;

}
