package zinsoft.faas.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zinsoft.faas.service.AdmZipcodeService;
import zinsoft.faas.vo.AdmZipcode;

@Controller
public class AdmZipcodeController {

    @Resource
    AdmZipcodeService admZipcodeService;

    @RequestMapping(value = "/api/adm/list.do", method = RequestMethod.GET)
    @ResponseBody
    public List<AdmZipcode> list(String upAdmCd, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        List<AdmZipcode> list = admZipcodeService.list(upAdmCd);
        return list;
    }

}
