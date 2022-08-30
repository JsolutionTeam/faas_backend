package zinsoft.faas.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import zinsoft.faas.service.LogFileService;
import zinsoft.util.DataTablesParam;
import zinsoft.util.DataTablesResponse;

@Controller
public class LogFileController {

    @Resource
    LogFileService logFileService;

    @RequestMapping(value = "/api/logs.do", method = RequestMethod.GET)
    @ResponseBody
    public DataTablesResponse<Map<String, Object>> listLogFile(DataTablesParam param, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        return logFileService.page(param);
    }

    @RequestMapping(value = "/api/logs/download.do", method = RequestMethod.GET)
    public ModelAndView download(String fileName, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (fileName == null || fileName.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ModelAndView("error");
        }

        File file = logFileService.get(fileName);

        if (file == null || !file.exists()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ModelAndView("error");
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("file", file);
        param.put("fileName", fileName);

        return new ModelAndView("downloadView", param);
    }

}
