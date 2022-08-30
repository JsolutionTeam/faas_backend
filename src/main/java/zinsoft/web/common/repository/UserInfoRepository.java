package zinsoft.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, String>, UserInfoQueryRepository {

}
