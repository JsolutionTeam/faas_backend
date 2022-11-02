package zinsoft.faas.entity;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ac_detail_seq")
    private Long acDetailSeq;

    @Column(name="ac_detail_nm")
    private String acDetailNm;

    @Column(name="ac_id")
    private String acId;

    @Column(name="expr_seq")
    private Long exprSeq;


}
