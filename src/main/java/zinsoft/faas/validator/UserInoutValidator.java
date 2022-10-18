package zinsoft.faas.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import zinsoft.faas.dto.AccountDto;
import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.service.AccountService;
import zinsoft.util.Result;
import zinsoft.web.common.dto.CodeDto;
import zinsoft.web.common.service.CodeService;

@Component
public class UserInoutValidator implements Validator {

    @Autowired
    CodeService codeService;

//    @Autowired
//    SmartfarmCropService cropService;

    @Autowired
    AccountService accountService;

    @Override
    public boolean supports(Class<?> clazz) {
        // TODO Auto-generated method stub
        return UserInoutDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserInoutDto userInoutDto = (UserInoutDto)target;

//        if(userInoutDto.getCropSeq() > 0 ) {
//            boolean res =  cropService.isExistCropId(userInoutDto.getCropSeq());
//            if(!res) {
//                errors.rejectValue("cropSeq", Result.INVALID_CROP);
//            }
//        }

        if(userInoutDto.getPackTCd() != null && userInoutDto.getPackTCd().isEmpty() == false) {
            List<CodeDto> codeDto =  codeService.listStartsWithCodeVal("PACK_T_CD", userInoutDto.getPackTCd());
            if(codeDto == null || codeDto.size() == 0) {
                errors.rejectValue("packTCd", Result.INVALID_PACK_T_CD);
            }
        }

        if(userInoutDto.getGradeTCd() != null && userInoutDto.getGradeTCd().isEmpty() == false) {
            List<CodeDto> codeDto =  codeService.listStartsWithCodeVal("GRADE_T_CD", userInoutDto.getGradeTCd());
            if(codeDto == null || codeDto.size() == 0) {
                errors.rejectValue("gradeTCd", Result.INVALID_GRADE_T_CD);
            }
        }

        if(userInoutDto.getInoutTCd() != null && userInoutDto.getInoutTCd().isEmpty() == false) {
            List<CodeDto> codeDto =  codeService.listStartsWithCodeVal("INOUT_T_CD", userInoutDto.getInoutTCd());
            if(codeDto == null || codeDto.size() == 0) {
                errors.rejectValue("inoutTCd", Result.INVALID_INOUT_T_CD);
            }
        }

        Map<String, Object> param = new HashMap<>();
        if(UserInoutDto.INOUT_CD_INCOME.equals(userInoutDto.getInoutCd()) == true) {
            param.clear();
            param.put("upAcId", "500");//영업수익
            param.put("acId", userInoutDto.getUpAcId());
            List<AccountDto> listAccountDto =  accountService.list(param);
            if(listAccountDto == null || listAccountDto.size() == 0) {
                errors.rejectValue("upAcId", Result.INVALID_AC_ID);
            }
        } else if(UserInoutDto.INOUT_CD_OUTGOING.equals(userInoutDto.getInoutCd()) == true) {
            param.clear();
            param.put("upAcId", "400");//비용
            param.put("acId", userInoutDto.getUpAcId());
            List<AccountDto> listAccountDto =  accountService.list(param);
            if(listAccountDto == null || listAccountDto.size() == 0) {
                errors.rejectValue("upAcId", Result.INVALID_AC_ID);
            }
        }

        if(userInoutDto.getAcId() != null && userInoutDto.getAcId().isEmpty() == false) {
            param.clear();
            param.put("upAcId", userInoutDto.getUpAcId());
            param.put("acId", userInoutDto.getAcId());
            List<AccountDto> listAccountDto =  accountService.list(param);
            if(listAccountDto == null || listAccountDto.size() == 0) {
                errors.rejectValue("acId", Result.INVALID_AC_ID);
            }
        }
    }

}
