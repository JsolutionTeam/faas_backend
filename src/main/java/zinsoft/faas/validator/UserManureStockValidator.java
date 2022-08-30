package zinsoft.faas.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import zinsoft.faas.dto.UserManureStockDto;
import zinsoft.util.Result;
import zinsoft.web.common.dto.CodeDto;
import zinsoft.web.common.service.CodeService;

@Component
public class UserManureStockValidator implements Validator {

    @Autowired
    CodeService codeService;

    @Override
    public boolean supports(Class<?> clazz) {
        // TODO Auto-generated method stub
        return UserManureStockDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // TODO Auto-generated method stub
        //ValidationUtils.rejectIfEmpty(errors, "userId", "userCropDto.userId.empty","아이디를 입력해주세요.");

        UserManureStockDto userManureStockDto = (UserManureStockDto)target;
        if("I".equals(userManureStockDto.getSupInoutCd()) ) {
            if(userManureStockDto.getInoutTCd() == null) {
                errors.rejectValue("inoutTCd", "required", "결제 정보가 없습니다.");
            }else {
                List<CodeDto> codeDto =  codeService.listStartsWithCodeVal("INOUT_T_CD", userManureStockDto.getInoutTCd());
                if(codeDto == null || codeDto.size() == 0) {
                    errors.rejectValue("inoutTCd", Result.INVALID_INOUT_T_CD);
                }
            }

            if(userManureStockDto.getAmt() == null) {
                errors.rejectValue("amt", "required", "구매금액이 없습니다");
            }
        }

        if(userManureStockDto.getPackTCd() != null && userManureStockDto.getPackTCd().isEmpty() == false) {
            List<CodeDto> codeDto =  codeService.listStartsWithCodeVal("PACK_T_CD", userManureStockDto.getPackTCd());
            if(codeDto == null || codeDto.size() == 0) {
                errors.rejectValue("packTCd", Result.INVALID_PACK_T_CD);
            }
        }
    }

}
