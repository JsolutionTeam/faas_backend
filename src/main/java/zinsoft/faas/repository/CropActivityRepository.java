package zinsoft.faas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.faas.entity.CropActivity;

public interface CropActivityRepository extends JpaRepository<CropActivity, Long>, CropActivityQueryRepository {


}
