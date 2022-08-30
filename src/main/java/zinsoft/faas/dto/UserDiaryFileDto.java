package zinsoft.faas.dto;

import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
public class UserDiaryFileDto {

    public static final String FILE_K_CD_WORKING = "W";
    public static final String FILE_K_CD_RECEIPT = "R";

    @NotBlank
    @ApiModelProperty(notes = "영농일지 일련번호")
    private Long userDiarySeq;
    @NotBlank
    @ApiModelProperty(notes = "파일 일련번호")
    private Long fileSeq;
    @NotBlank
    @Pattern(regexp = "^(W|R)$")
    @ApiModelProperty(notes = "파일 종류")
    private String fileKCd;

    @ApiModelProperty(notes = "파일명")
    private String fileNm;


    public UserDiaryFileDto(UserDiaryDto userDiary) {
        this.userDiarySeq = userDiary.getUserDiarySeq();
    }

    public UserDiaryFileDto(UserDiaryDto userDiary, String fileKCd) {
        this.userDiarySeq = userDiary.getUserDiarySeq();
        this.fileKCd = fileKCd;
    }

}