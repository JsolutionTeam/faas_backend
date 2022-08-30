package zinsoft.web.common.repository;

public interface UserRoleHistQueryRepository {

    void insertByUserId(String workerId, String workCd, String userId);

    void insertByRoleId(String workerId, String workCd, String roleId);

}
