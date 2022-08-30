package zinsoft.web.common.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class DownloadView extends AbstractView {

    public DownloadView() {
        setContentType("application/download; utf-8");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = (File) model.get("file");
        String fileName = (String) model.get("fileName");
        String contentType = (String) model.get("contentType");
        String browser = getBrowser(request);

        if (browser.equals("MSIE")) {
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Chrome")) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < fileName.length(); i++) {
                char c = fileName.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            fileName = sb.toString();
        } else if (browser.equals("Opera")) {
            fileName = new String(fileName.getBytes("UTF-8"), "8859_1");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "8859_1");
        }

        if (contentType == null || contentType.length() == 0) {
            response.setContentType(getContentType());
        } else {
            response.setContentType(contentType);
        }

        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setHeader("Content-Transfer-Encoding", "binary");

        OutputStream out = response.getOutputStream();

        try (
            FileInputStream fis = new FileInputStream(file);
        ) {
            FileCopyUtils.copy(fis, out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        out.flush();
    }

    private String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");

        if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1) {
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        }

        return "Firefox";
    }

}
