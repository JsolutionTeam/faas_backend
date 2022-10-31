package zinsoft.web.common.entity;

import java.util.Date;

import javax.persistence.Column;
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

    @Column(name = "board_id")
    private String boardId;

    @Column(name = "reg_dtm")
    private Date regDtm;

    @Column(name = "update_dtm")
    private Date updateDtm;

    @Column(name = "status_cd")
    private String statusCd;

    @Column(name = "board_nm")
    private String boardNm;

    @Column(name = "push_yn")
    private String pushYn;

    @Column(name = "push_roles")
    private String pushRoles;

    @Column(name = "push_emails")
    private String pushEmails;

    @Column(name = "roles_insert")
    private String rolesInsert;

    @Column(name = "roles_list")
    private String rolesList;

    @Column(name = "roles_detail")
    private String rolesDetail;

    @Column(name = "roles_update")
    private String rolesUpdate;

    @Column(name = "roles_delete")
    private String rolesDelete;

}