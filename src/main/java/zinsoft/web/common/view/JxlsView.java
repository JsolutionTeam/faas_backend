package zinsoft.web.common.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jxls.common.Context;

public class JxlsView extends AbstractJxlsView {

    private Context jxlsContext = null;

    public JxlsView(Context jxlsContext) {
        super();
        this.jxlsContext = jxlsContext;
    }

    @Override
    protected Context buildContext(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        return jxlsContext;
    }

}
