package zinsoft.faas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.faas.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long>, ActivityQueryRepository {

    boolean existsByActivitySeqAndStatusCd(Long activitySeq, String statusCd);

}
