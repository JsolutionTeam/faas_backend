package zinsoft.faas.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import zinsoft.faas.dto.UserProductionDto;
import zinsoft.util.Result;
import zinsoft.web.common.dto.CodeDto;
import zinsoft.web.common.service.CodeService;

@Component
public class UserProductionValidator implements Validator {

    @Autowired
    CodeService codeService;

    @Override
    public boolean supports(Class<?> clazz) {
        // TODO Auto-generated method stub
        return UserProductionDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // TODO Auto-generated method stub
        //ValidationUtils.rejectIfEmpty(errors, "userId", "userCropDto.userId.empty","아이디를 입력해주세요.");

        UserProductionDto userProductionDto = (UserProductionDto) target;

        if (userProductionDto.getPlanTCd() != null && userProductionDto.getPlanTCd().isEmpty() == false) {
            List<CodeDto> codeDto = codeService.listStartsWithCodeVal("PLAN_T_CD", userProductionDto.getPlanTCd());
            if (codeDto == null || codeDto.size() == 0) {
                errors.rejectValue("planTCd", "required", "계획/실적 중 하나를 선택하세요");
            }
        }

        if (userProductionDto.getPackTCd() != null && userProductionDto.getPackTCd().isEmpty() == false) {
            List<CodeDto> codeDto = codeService.listStartsWithCodeVal("PACK_T_CD", userProductionDto.getPackTCd());
            if (codeDto == null || codeDto.size() == 0) {
                errors.rejectValue("packTCd", Result.INVALID_PACK_T_CD);
            }
        }

        if (userProductionDto.getGradeTCd() != null && userProductionDto.getGradeTCd().isEmpty() == false) {
            List<CodeDto> codeDto = codeService.listStartsWithCodeVal("GRADE_T_CD", userProductionDto.getGradeTCd());
            if (codeDto == null || codeDto.size() == 0) {
                errors.rejectValue("gradeTCd", Result.INVALID_GRADE_T_CD);
            }
        }
    }

}
