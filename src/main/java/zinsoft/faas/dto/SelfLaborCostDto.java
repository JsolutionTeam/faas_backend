package zinsoft.faas.dto;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class SelfLaborCostDto {

    private Long selfLaborSeq;
    @NotBlank
    @Pattern(regexp = "^20[0-9]{2}$")
    private String year;
    @JsonIgnore
    private Date regDtm;
    @JsonIgnore
    private Date updateDtm;
    @JsonIgnore
    private String statusCd;
    @NotNull
    @Min(value = 0)
    @Digits(integer = 10, fraction = 0)
    private Long manAmt;
    @NotNull
    @Min(value = 0)
    @Digits(integer = 10, fraction = 0)
    private Long womanAmt;

}