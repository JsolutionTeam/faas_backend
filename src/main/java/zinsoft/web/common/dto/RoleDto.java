package zinsoft.web.common.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zinsoft.util.KeyValueable;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class RoleDto implements KeyValueable<String> {

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_ADMIN = ROLE_PREFIX + "ADMIN";
    public static final String ROLE_MANAGER = ROLE_PREFIX + "MANAGER";
    public static final String ROLE_ANONYMOUS = ROLE_PREFIX + "ANONYMOUS";

    @Size(max = 50)
    private String roleId;
    @JsonIgnore
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;
    @NotEmpty
    @Size(max = 60)
    private String roleNm;
    private Integer exprSeq;

    @Override
    @JsonIgnore
    public String getKey() {
        return roleId;
    }

    @Override
    @JsonIgnore
    public String getValue() {
        return roleNm;
    }

}