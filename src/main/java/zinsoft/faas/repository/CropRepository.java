package zinsoft.faas.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.faas.entity.Crop;

public interface CropRepository extends JpaRepository<Crop, Long>, CropQueryRepository {

    boolean existsByCropSeqAndStatusCd(Long cropSeq,  String statusCd);

    Crop findByCropSeqAndStatusCd(Long cropSeq, String string);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Crop a SET a.updateDtm = now(), a.statusCd = 'D' WHERE a.cropSeq in :cropSeqs")
    void deleteAllById(Long[] cropSeqs);


}
