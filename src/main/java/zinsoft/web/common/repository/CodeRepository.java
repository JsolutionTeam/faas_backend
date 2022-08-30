package zinsoft.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.Code;
import zinsoft.web.common.entity.CodeId;

public interface CodeRepository extends JpaRepository<Code, CodeId>, CodeQueryRepository {

}
