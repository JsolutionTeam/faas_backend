package zinsoft.faas.service;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.util.CommonUtil;
import zinsoft.util.DataTablesParam;
import zinsoft.util.DataTablesResponse;

@Service
public class LogFileService extends EgovAbstractServiceImpl {

    public static final String LOG_DIR = "/APP/farmfile/logs";

    public DataTablesResponse<Map<String, Object>> page(DataTablesParam param) {
        DataTablesResponse<Map<String, Object>> page = new DataTablesResponse<Map<String, Object>>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        File[] files = (new File(LOG_DIR)).listFiles((FileFilter) FileFileFilter.INSTANCE);
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        int count = files.length;
        int start = param.getStart();
        int end = Math.min(start + param.getNumOfRows(), files.length);

        for (int i = start; i < end; i++) {
            File file = files[i];
            Map<String, Object> vo = new HashMap<String, Object>();

            vo.put("fileName", file.getName());
            vo.put("length", file.length());
            vo.put("lastModified", CommonUtil.formatDate(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss,SSS"));
            list.add(vo);
        }

        page.setDraw(param.getDraw());
        page.setTotalCount(count);
        page.setItems(list);

        return page;
    }

    public File get(String fileName) {
        return new File(LOG_DIR + File.separator + fileName.replaceAll("\\.\\.", ""));
    }

}
