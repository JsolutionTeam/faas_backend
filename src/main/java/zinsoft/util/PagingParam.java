package zinsoft.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class PagingParam {

    private long page;
    private long start;
    private Integer limit;
    private Map<String, String> cond = new HashMap<String, String>();
    private String userId;

    public void setCondition(HttpServletRequest request) {
        @SuppressWarnings("rawtypes")
        Enumeration paramNames = request.getParameterNames();

        while (paramNames.hasMoreElements()) {
            String name = (String) paramNames.nextElement();
            if (!name.equals("page") && !name.equals("start") && !name.equals("limit")) {
                cond.put(name, request.getParameter(name));
            }
        }

        if (limit == null) {
            limit = 20;
        }

        if (limit > 0) {
            page = start / limit + 1;
        } else {
            page = 1;
        }
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Map<String, String> getCond() {
        return cond;
    }

    public void setCond(Map<String, String> cond) {
        this.cond = cond;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
