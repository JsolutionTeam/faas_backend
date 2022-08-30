package zinsoft.faas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.faas.entity.AccountDetail;

public interface AccountDetailRepository extends JpaRepository<AccountDetail, Long> {

    List<AccountDetail> findByAcId(String acId);

}
