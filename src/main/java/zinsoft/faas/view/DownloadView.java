package zinsoft.faas.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import net.coobird.thumbnailator.Thumbnails;
import zinsoft.util.AppPropertyUtil;
import zinsoft.util.CommonUtil;
import zinsoft.util.Constants;

public class DownloadView extends AbstractView {

    private int imageWidth = 800;
    private String[] imageExt = {"jpg", "jpeg", "png", "gif"};

    public DownloadView() {
        String imageWidth = AppPropertyUtil.get(Constants.IMAGE_THUMB_WIDTH);

        try {
            this.imageWidth = Integer.parseInt(imageWidth, 10);
        } catch (Exception e) {
            this.imageWidth = 800;
        }

        setContentType("application/download; utf-8");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = (File) model.get("file");
        String fileName = (String) model.get("fileName");
        String fileExt = CommonUtil.getFileExt(fileName, false).toLowerCase();
        String contentType = (String) model.get("contentType");
        String browser = getBrowser(request);

        if (fileName != null && ArrayUtils.indexOf(imageExt, fileExt) >= 0) {
            String thumbName = file.getAbsoluteFile() + "_" + imageWidth;
            File img = new File(thumbName);
            if (!img.exists()) {
                OutputStream os = new FileOutputStream(new File(thumbName));
                Thumbnails.of(file).size(imageWidth, imageWidth).toOutputStream(os);
                os.close();
            }
            file = img;
        }

        if (browser.equals("MSIE")) {
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
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
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                }
            }
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
