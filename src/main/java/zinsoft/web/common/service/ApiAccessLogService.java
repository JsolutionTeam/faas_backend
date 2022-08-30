package zinsoft.web.common.service;

public interface ApiAccessLogService {

    void insert(String path, String method, String userId, String remoteAddr, String note);

}