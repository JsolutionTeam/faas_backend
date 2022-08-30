package zinsoft.web.common.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import zinsoft.util.AppPropertyUtil;
import zinsoft.util.ApplicationContextProvider;
import zinsoft.util.CommonUtil;
import zinsoft.util.Constants;

public abstract class AbstractJxlsView extends AbstractView {

    private static final String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; // "application/vnd.ms-excel";
    private static final String EXTENSION_XLSX = ".xlsx";
    private static final String OUTPUT_TYPE_XLSX = "xlsx";
    private static final String CONTENT_TYPE_PDF = "application/pdf";
    private static final String EXTENSION_PDF = ".pdf";
    private static final String OUTPUT_TYPE_PDF = "pdf";
    private String workingDir;

    public AbstractJxlsView() {
        setContentType(CONTENT_TYPE_XLSX);
        this.workingDir = AppPropertyUtil.get(Constants.UPLOAD_DIR) + "xls_to_pdf";
        new File(workingDir).mkdirs();
    }

    private void processTemplate(InputStream templateStream, OutputStream targetStream, Context context) throws IOException {
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(templateStream, targetStream);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> functionMap = new HashMap<String, Object>();

        functionMap.put("CommonUtil", CommonUtil.class);
        evaluator.getJexlEngine().setFunctions(functionMap);
        jxlsHelper.processTemplate(context, transformer);
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String outputType = (String) model.get("outputType");

        if (outputType == null) {
            outputType = AppPropertyUtil.get(Constants.EXCEL_DEFAULT);

            if (outputType == null) {
                outputType = OUTPUT_TYPE_XLSX;
            }
        } else {
            outputType = outputType.toLowerCase();
        }

        if (!outputType.equals(OUTPUT_TYPE_XLSX) && !outputType.equals(OUTPUT_TYPE_PDF)) {
            throw new IllegalArgumentException("Output type is not supported: " + outputType);
        }

        ApplicationContext ctx = ApplicationContextProvider.applicationContext();
        ServletContext servletContext = (ServletContext) ctx.getBean("servletContext");

        Context jxlsContext = buildContext(model, request, response);
        String filename = CommonUtil.urlEncode((String) model.get("filename")) + "." + outputType;
        String browser = getBrowser(request);

        if (browser.equals("MSIE")) {
            filename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            filename = sb.toString();
        } else if (browser.equals("Opera")) {
            filename = new String(filename.getBytes("UTF-8"), "8859_1");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "8859_1");
        }

        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        if (outputType.equals(OUTPUT_TYPE_XLSX)) {
            InputStream jxls = null;
            OutputStream out = response.getOutputStream();

            try {
                jxls = new FileInputStream(servletContext.getRealPath("/jxls/" + model.get("template"))); //this.getClass().getResourceAsStream((String) context.getVar("template"));
                processTemplate(jxls, out, jxlsContext);
                out.flush();
            } catch (Exception e) {
                throw e;
            } finally {
                if (jxls != null) {
                    try {
                        jxls.close();
                    } catch (Exception e) {
                    }
                }
            }
        } else {
            String basepath = workingDir + "/" + request.getSession().getId();
            String excelFilename = basepath + EXTENSION_XLSX;
            String pdfFilename = basepath + EXTENSION_PDF;
            InputStream jxls = null;
            OutputStream out = response.getOutputStream();
            FileOutputStream xls = null;
            FileInputStream pdf = null;

            try {
                File excelFile = new File(excelFilename);
                xls = new FileOutputStream(excelFile);
                jxls = new FileInputStream(servletContext.getRealPath("/jxls/" + model.get("template"))); //this.getClass().getResourceAsStream((String) context.getVar("template"));
                JxlsHelper.getInstance().setUseFastFormulaProcessor(false).processTemplate(jxls, xls, jxlsContext);

                String xlsToPdf = AppPropertyUtil.get(Constants.EXCEL_TO_PDF);
                xlsToPdf = xlsToPdf.replaceAll("\\[in\\]", excelFilename);
                xlsToPdf = xlsToPdf.replaceAll("\\[out\\]", pdfFilename);
                xlsToPdf = xlsToPdf.replaceAll("\\[outdir\\]", workingDir);

                DefaultExecutor executor = new DefaultExecutor();
                CommandLine cmdLine = CommandLine.parse(xlsToPdf);
                /*
                String xlsToPdfParam = SiteConfig.XLS_TO_PDF_PARAM;
                xlsToPdfParam = xlsToPdfParam.replaceAll("\\[in\\]", excelFilename);
                xlsToPdfParam = xlsToPdfParam.replaceAll("\\[out\\]", pdfFilename);
                xlsToPdfParam = xlsToPdfParam.replaceAll("\\[outdir\\]", workingDir);
                String[] xlsToPdfParams = xlsToPdfParam.split(" ");
                for (int i = 0, cnt = xlsToPdfParams.length; i < cnt; i++) {
                cmdLine.addArgument(xlsToPdfParams[i]);
                }
                */
                executor.execute(cmdLine);

                setContentType(CONTENT_TYPE_PDF);

                File pdfFile = new File(pdfFilename);
                pdf = new FileInputStream(pdfFile);
                FileCopyUtils.copy(pdf, out);
                out.flush();

                if (excelFile != null) {
                    try {
                        excelFile.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (pdfFile != null) {
                    try {
                        pdfFile.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if (pdf != null) {
                    try {
                        pdf.close();
                    } catch (Exception e) {
                    }
                }
                if (xls != null) {
                    try {
                        xls.close();
                    } catch (Exception e) {
                    }
                }
                if (jxls != null) {
                    try {
                        jxls.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    protected String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        }
        return "Firefox";
    }

    protected abstract Context buildContext(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
