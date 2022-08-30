package zinsoft.web.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_board")
public class Board {

    @Id
    private String boardId;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private String boardNm;
    private String pushYn;
    private String pushRoles;
    private String pushEmails;
    private String rolesInsert;
    private String rolesList;
    private String rolesDetail;
    private String rolesUpdate;
    private String rolesDelete;

}