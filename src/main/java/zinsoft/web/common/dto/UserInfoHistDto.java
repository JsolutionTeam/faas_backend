package zinsoft.web.common.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class UserInfoHistDto extends UserInfoDto {

    private static final long serialVersionUID = 5853228217855524670L;

    private String workerId;
    private Date workDtm;
    private String workCd;

}