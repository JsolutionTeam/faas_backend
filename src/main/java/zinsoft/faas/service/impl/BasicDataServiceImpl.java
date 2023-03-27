package zinsoft.faas.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zinsoft.faas.dto.AccountDto;
import zinsoft.faas.dto.CropDto;
import zinsoft.faas.service.AccountService;
import zinsoft.faas.service.CropService;
import zinsoft.util.CommonUtil;
import zinsoft.util.Constants;
import zinsoft.web.common.dto.CodeDto;
import zinsoft.web.common.service.AppPropertiesService;
import zinsoft.web.common.service.BasicDataService;
import zinsoft.web.common.service.CodeService;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BasicDataServiceImpl implements BasicDataService {

    @Autowired
    ServletContext servletContext;

    @Resource
    AppPropertiesService appPropertiesService;

    @Resource
    CodeService codeService;

    @Resource
    AccountService accountService;

    @Resource
    CropService cropService;

    @Override
    public void createBasicData() {
        String ver = CommonUtil.getToday("yyyyMMddHHmmss");
        String basePath = servletContext.getRealPath("/static/js");
        String jsFilename = basePath + "/basic-data.js";
        String jsonFilename = basePath + "/basic-data.json";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> search = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer();
        String json = null;
        List<CodeDto> ynTCd = new ArrayList<CodeDto>(2);
        List<CodeDto> nyTCd = new ArrayList<CodeDto>(2);
        List<CodeDto> successYn = new ArrayList<CodeDto>(2);

        ynTCd.add(new CodeDto("yn_t_cd", Constants.YN_YES, Constants.YN_YES));
        ynTCd.add(new CodeDto("yn_t_cd", Constants.YN_NO, Constants.YN_NO));

        nyTCd.add(new CodeDto("ny_t_cd", Constants.YN_NO, Constants.YN_NO));
        nyTCd.add(new CodeDto("ny_t_cd", Constants.YN_YES, Constants.YN_YES));

        successYn.add(new CodeDto("success_yn", Constants.YN_YES, "성공", "Success"));
        successYn.add(new CodeDto("success_yn", Constants.YN_NO, "실패", "Fail"));


        search.put("inputYn", "Y");
        search.put("orderBy", "NAME_ASC");
        List<AccountDto> accountList = accountService.list(search);
        data.put("accountList", accountList);

        search.put("exprYn", "Y");
        search.put("inputYn", "N");
        List<AccountDto> upAccountList = accountService.list(search);
        data.put("upAccountList", upAccountList);

        search.put("bpTCd", "P");
        search.put("cdTCd", "D");
        search.put("exprYn", "Y");
        search.put("inputYn", "Y");
        List<AccountDto> incomeAccountList = accountService.list(search);
        data.put("incomeAccountList", incomeAccountList);

        search.put("bpTCd", "P");
        search.put("cdTCd", "C");
        search.put("exprYn", "Y");
        search.put("inputYn", "Y");
        List<AccountDto> outgoingAccountList = accountService.list(search);
        data.put("outgoingAccountList", outgoingAccountList);

        List<CropDto> cropList = cropService.list();
        data.put("cropList", cropList);


        List<CodeDto> upCropList = codeService.list("CROP_A_CD");
        data.put("upCropList", upCropList);

        try {
            data.put("ver", ver);
            data.put("yn_t_cd", ynTCd);
            data.put("ny_t_cd", nyTCd);
            data.put("success_yn", successYn);

            json = mapper.writeValueAsString(data);

            sb.append("var BASIC_DATA = ");
            sb.append(json);
            sb.append(';');

            FileUtils.writeStringToFile(new File(jsFilename), sb.toString(), "UTF-8");
            FileUtils.writeStringToFile(new File(jsonFilename), json, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        appPropertiesService.update(Constants.VERSION_BASIC_DATA, ver);
    }

}