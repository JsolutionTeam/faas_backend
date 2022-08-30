package zinsoft.faas.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.SelfLaborCost;

public interface SelfLaborCostRepository extends JpaRepository<SelfLaborCost, Long>, SelfLaborCostQueryRepository {

    SelfLaborCost findBySelfLaborSeqAndStatusCd(Long selfLaborCostSeq, String statusCd );

    SelfLaborCost findByYearAndStatusCd(String year, String statusCd );

    Long countByYearAndStatusCd(String year, String statusCd );

    Long countByYearAndStatusCdAndSelfLaborSeqNot(String year, String statusCd,Long selfLaborSeq);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE SelfLaborCost a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.selfLaborSeq in :selfLaborSeqs")
    void deleteBySelfLaborSeqs(Long[] selfLaborSeqs);

}
