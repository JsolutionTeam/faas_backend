package zinsoft.web.common.service;

public interface MenuAccessLogService {

    void insert(String menuId, String userId, String note);

}