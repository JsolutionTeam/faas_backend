package zinsoft.faas.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import zinsoft.faas.dto.UserCropDto;
import zinsoft.faas.service.CropService;
import zinsoft.faas.service.UserCropService;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;

@Controller
@RequestMapping("${api.prefix}/user-crop")
public class UserCropController {

    @Resource
    UserCropService userCropService;

    @Resource
    CropService cropService;

    @GetMapping(value = "/{userCropSeq}")
    @ResponseBody
    public UserCropDto get(@PathVariable("userCropSeq") Long userCropSeq, String userId, HttpServletRequest request, HttpSession session, Model model) throws Exception {

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        UserCropDto dto = new UserCropDto();
        dto.setUserCropSeq(userCropSeq);
        dto.setUserId(farmerInfo.getUserId());

        return userCropService.get(dto);
    }

    @PostMapping(value = "")
    @ResponseBody
    public Result insert(@Valid UserCropDto dto) throws Exception {

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        /*
        dto.setUserId( farmerInfo.getUserId()); // valid test로 주석처리함. 추후 주석 해제 필요 TODO
        */

        userCropService.insert(dto);
        if (farmerInfo != null) {
            farmerInfo.setCropCnt(farmerInfo.getCropCnt() + 1);
        }

        return new Result(true, Result.OK);
    }

    @GetMapping(value = "/page")
    @ResponseBody
    public DataTablesResponse<UserCropDto> page(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable, HttpSession session) throws Exception {
        if(!UserInfoUtil.isAdmin()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            search.put("userId", farmerInfo.getUserId());
        }
        search.put("cropCd", "1");

        return userCropService.page(search, pageable);
    }

    @GetMapping(value = "/list")
    @ResponseBody
    public List<UserCropDto> list(@RequestParam Map<String, Object> search) throws Exception {
        if(!UserInfoUtil.isAdmin()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            search.put("userId", farmerInfo.getUserId());
        }

        return userCropService.list(search);
    }

    @PutMapping(value = "/{userCropSeq}")
    @ResponseBody
    public Result put(@PathVariable("userCropSeq") Long userCropSeq, @Valid UserCropDto dto) throws Exception {

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        if (farmerInfo != null && farmerInfo.getUserId() != null) {
            dto.setUserId(farmerInfo.getUserId());
        } else {
            if (dto.getUserId() != null && dto.getUserId().isEmpty() == false) {
                dto.setUserId(dto.getUserId());
            } else {
                return new Result(false, "9998");
            }
        }

        if(userCropService.countByAliasNm(dto) > 0) {
            return new Result(false, "9011");
        }

        userCropService.update(dto);

        return new Result(true, "0000");
    }

    @DeleteMapping(value = "/{userCropSeq}")
    @ResponseBody
    public Result delete(@PathVariable("userCropSeq") Long userCropSeq, String userId) throws Exception {

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        if (farmerInfo != null && farmerInfo.getUserId() != null) {
            userId = farmerInfo.getUserId();
        } else if (StringUtils.isBlank(userId)) {
            return new Result(false, "9998");
        }

        // TODO Diary, inout에서 작물을 사용중인지 확인 후, 사용중일 경우 삭제되지 않게 처리 필요

        int cnt = userCropService.delete(userId, userCropSeq);
        if (farmerInfo != null) {
            farmerInfo.setCropCnt(farmerInfo.getCropCnt() - cnt);
        }

        return new Result(true, "0000");
    }

    @DeleteMapping(value = "/userId/{userId}")
    @ResponseBody
    public Result deleteByUserId(@PathVariable("userId") String userId) throws Exception {
        userCropService.deleteByUserId(userId);
        return new Result(true, "0000");
    }
}








