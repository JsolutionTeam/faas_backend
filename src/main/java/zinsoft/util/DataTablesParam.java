package zinsoft.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataTablesParam {

    private Integer draw;
    private int start = 0;
    private int numOfRows = 10;
    @Min(1)
    private int pageNo = 1;
    private List<Map<String, String>> order;
    private Map<String, Object> search = new HashMap<>();

    public int getStart() {
        return start == 0 && pageNo > 1 ? numOfRows * (pageNo - 1) : start;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = Math.min(numOfRows, 2000);
    }

}
