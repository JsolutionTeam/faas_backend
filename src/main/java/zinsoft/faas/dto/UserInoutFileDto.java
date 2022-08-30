package zinsoft.faas.dto;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
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
public class UserInoutFileDto {

    @NotBlank
    @ApiModelProperty(notes = "수입지출 일련번호")
    private Long userInoutSeq;
    @NotBlank
    @ApiModelProperty(notes = "파일 일련번호")
    private Long fileSeq;

    @ApiModelProperty(notes = "파일명")
    private String fileNm;

    public UserInoutFileDto(UserInoutDto userInout) {
        this.userInoutSeq = userInout.getUserInoutSeq();
    }

}