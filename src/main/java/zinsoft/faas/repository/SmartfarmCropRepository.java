package zinsoft.faas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zinsoft.faas.entity.SmartfarmCrop;

import java.util.Optional;

public interface SmartfarmCropRepository extends JpaRepository<SmartfarmCrop, Long>{
    Optional<SmartfarmCrop> findByCropCode(String cropCode);
}
