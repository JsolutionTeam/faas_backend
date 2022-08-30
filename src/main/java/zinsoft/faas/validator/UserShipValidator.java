package zinsoft.faas.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import zinsoft.faas.dto.UserShipDto;
import zinsoft.util.Result;
import zinsoft.web.common.dto.CodeDto;
import zinsoft.web.common.service.CodeService;

@Component
public class UserShipValidator implements Validator {
    @Autowired
    CodeService codeService;

    @Override
    public boolean supports(Class<?> clazz) {
        // TODO Auto-generated method stub
        return UserShipDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // TODO Auto-generated method stub
        //ValidationUtils.rejectIfEmpty(errors, "userId", "userCropDto.userId.empty","아이디를 입력해주세요.");

        UserShipDto userShipDto = (UserShipDto) target;

        if (userShipDto.getPlanTCd() != null && userShipDto.getPlanTCd().isEmpty() == false) {
            List<CodeDto> codeDto = codeService.listStartsWithCodeVal("PLAN_T_CD", userShipDto.getPlanTCd());
            if (codeDto == null || codeDto.size() == 0) {
                errors.rejectValue("planTCd", "required", "계획/실적 중 하나를 선택하세요");
            }
            else if(userShipDto.getPlanTCd().equals(UserShipDto.PLAN_T_CD_ACTUAL)) { //실적일때, 등급,금액 check
                if (userShipDto.getGradeTCd() != null && userShipDto.getGradeTCd().isEmpty() == false) {
                    codeDto = codeService.listStartsWithCodeVal("GRADE_T_CD", userShipDto.getGradeTCd());
                    if (codeDto == null || codeDto.size() == 0) {
                        errors.rejectValue("gradeTCd", Result.INVALID_GRADE_T_CD);
                    }
                }
            }
        }

        if (userShipDto.getPackTCd() != null && userShipDto.getPackTCd().isEmpty() == false) {
            List<CodeDto> codeDto = codeService.listStartsWithCodeVal("PACK_T_CD", userShipDto.getPackTCd());
            if (codeDto == null || codeDto.size() == 0) {
                errors.rejectValue("packTCd", Result.INVALID_PACK_T_CD);
            }
        }


    }

}
