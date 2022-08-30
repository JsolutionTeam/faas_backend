package zinsoft.web.common.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_menu_api")
@IdClass(MenuApi.class)
public class MenuApi implements Serializable {

    private static final long serialVersionUID = 1981236647129976993L;

    @Id
    private String menuId;
    @Id
    private String actCd;
    @Id
    private String pathPattern;
    @Id
    private String method;

}