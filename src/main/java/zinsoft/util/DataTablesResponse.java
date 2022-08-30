package zinsoft.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class DataTablesResponse<T> {

    private static final ModelMapper MAPPER = new ModelMapper();
    //static {
    //    MAPPER.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    //}

    private Integer draw;
    private int numOfRows;
    private int pageNo;
    private long totalCount;
    private List<T> items;
    private Object extraData;

    public DataTablesResponse() {
    }

    public DataTablesResponse(DataTablesParam dtParam) {
        this.draw = dtParam.getDraw();
        this.numOfRows = dtParam.getNumOfRows();
        this.pageNo = dtParam.getPageNo();
    }

    public static <T> DataTablesResponse<T> of(Page<T> page) {
        return DataTablesResponse.of(page, null);
    }

    public static <T> DataTablesResponse<T> of(Page<T> page, Object extraData) {
        Integer draw = null;

        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            draw = Integer.valueOf(request.getParameter("draw"), 10); // for Datatables
        } catch (Exception e) {
            // ignore
        }

        return new DataTablesResponse<>(draw, page.getSize(), page.getNumber() + 1, page.getTotalElements(), page.getContent(), extraData);
    }

    public static <T, E> DataTablesResponse<T> of(Class<T> t, Page<E> page) {
        return DataTablesResponse.of(t, page, null);
    }

    public static <T, E> DataTablesResponse<T> of(Class<T> t, Page<E> page, Object extraData) {
        return DataTablesResponse.of(page.map(e->MAPPER.map(e, t)), extraData);
    }

}
