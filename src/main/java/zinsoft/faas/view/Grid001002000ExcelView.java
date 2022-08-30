package zinsoft.faas.view;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.context.ApplicationContext;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
import zinsoft.util.AppPropertyUtil;
import zinsoft.util.ApplicationContextProvider;
import zinsoft.util.Constants;

public class Grid001002000ExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        ApplicationContext ctx = ApplicationContextProvider.applicationContext();
        ServletContext servletContext = (ServletContext) ctx.getBean("servletContext");
        Map<String, String> result = (Map<String, String>) model.get("result");

        String inoutCd = result.get("inoutCd");
        String templateFileName = null;
        if ("I".equals(inoutCd)) {
            templateFileName = servletContext.getRealPath("/sample/inout_i.xlsx");
        } else {
            templateFileName = servletContext.getRealPath("/sample/inout_o.xlsx");
        }

        String destFileName = AppPropertyUtil.get(Constants.UPLOAD_DIR) + "xls_to_pdf" + "/" + request.getSession().getId() + ".xlsx";

        XLSTransformer transformer = new XLSTransformer();
        try {
            transformer.transformXLS(templateFileName, result, destFileName);
        } catch (ParsePropertyException | InvalidFormatException | IOException e) {
            e.printStackTrace();
        }

        response.setHeader("isMadeTemplate", "true");
    }

}
