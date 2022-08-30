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
public class MenuAccessLogDto {

    private Long menuAccessLogSeq;
    private Date accessDtm;
    private String menuId;
    private String userId;
    private String remoteAddr;
    private String note;

    public MenuAccessLogDto() {
    }

    public MenuAccessLogDto(String menuId, String userId, String remoteAddr, String note) {
        this.menuId = menuId;
        this.userId = userId;
        this.remoteAddr = remoteAddr;
        this.note = note;
    }

}