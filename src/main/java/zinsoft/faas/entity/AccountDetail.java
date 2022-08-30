package zinsoft.faas.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_account_detail")
public class AccountDetail {

    @Id
    @GeneratedValue
    private Long acDetailSeq;
    private String acDetailNm;
    private String acId;
    private Long exprSeq;

}
