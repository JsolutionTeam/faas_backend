package zinsoft.faas.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import zinsoft.faas.dto.UserChemicalStockDto;
import zinsoft.util.Result;
import zinsoft.web.common.dto.CodeDto;
import zinsoft.web.common.service.CodeService;

@Component
public class UserChemicalStockValidator implements Validator {

    @Autowired
    CodeService codeService;

    @Override
    public boolean supports(Class<?> clazz) {
        // TODO Auto-generated method stub
        return UserChemicalStockDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // TODO Auto-generated method stub
        //ValidationUtils.rejectIfEmpty(errors, "userId", "userCropDto.userId.empty","아이디를 입력해주세요.");

        UserChemicalStockDto userChemicalStockDto = (UserChemicalStockDto)target;
        if("I".equals(userChemicalStockDto.getSupInoutCd()) ) {
            if(userChemicalStockDto.getInoutTCd() == null) {
                errors.rejectValue("inoutTCd", "required", "결제 정보가 없습니다.");
            }else {
                List<CodeDto> codeDto =  codeService.listStartsWithCodeVal("INOUT_T_CD", userChemicalStockDto.getInoutTCd());
                if(codeDto == null || codeDto.size() == 0) {
                    errors.rejectValue("inoutTCd", Result.INVALID_INOUT_T_CD);
                }
            }

            if(userChemicalStockDto.getAmt() == null) {
                errors.rejectValue("amt", "required", "구매금액이 없습니다");
            }
        }

        if(userChemicalStockDto.getPackTCd() != null && userChemicalStockDto.getPackTCd().isEmpty() == false) {
            List<CodeDto> codeDto =  codeService.listStartsWithCodeVal("PACK_T_CD", userChemicalStockDto.getPackTCd());
            if(codeDto == null || codeDto.size() == 0) {
                errors.rejectValue("packTCd", Result.INVALID_PACK_T_CD);
            }
        }
    }

}
