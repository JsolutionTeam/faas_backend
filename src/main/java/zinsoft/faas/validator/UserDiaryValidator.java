package zinsoft.faas.validator;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.service.ActivityService;
import zinsoft.faas.service.EpisNsFmwrkWrkcdService;
import zinsoft.faas.service.MgrCropDetailService;
import zinsoft.util.Result;
import zinsoft.web.common.dto.CodeDto;
import zinsoft.web.common.service.CodeService;

@Component
@RequiredArgsConstructor
public class UserDiaryValidator implements Validator {

    @Autowired
    CodeService codeService;

    //    @Autowired
    //    CropService cropService;

    @Autowired
    ActivityService activityService;

    @Autowired
    private MgrCropDetailService mgrCropDetailService;

    private final EpisNsFmwrkWrkcdService wrkcdService;

    @Override
    public boolean supports(Class<?> clazz) {
        // TODO Auto-generated method stub
        return UserDiaryDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // TODO Auto-generated method stub
        //ValidationUtils.rejectIfEmpty(errors, "userId", "userCropDto.userId.empty","아이디를 입력해주세요.");

        UserDiaryDto userDiaryDto = (UserDiaryDto) target;

        if (userDiaryDto.getDiaryTCd() != null && userDiaryDto.getDiaryTCd().isEmpty() == false) {
            List<CodeDto> codeDto = codeService.listStartsWithCodeVal("DIARY_T_CD", userDiaryDto.getDiaryTCd());
            if (codeDto == null || codeDto.size() == 0) {
                errors.rejectValue("diaryTCd", "required", "일지/계획 중 하나를 선택하세요");
            }
        }

        //        if(userDiaryDto.getCropSeq() > 0 ) {
        //            boolean res =  cropService.isExistCropId(userDiaryDto.getCropSeq());
        //            if(!res) {
        //                errors.rejectValue("cropSeq", Result.INVALID_CROP);
        //            }
        //        }

        String cropCd = userDiaryDto.getCropCd();
        // cropCd를 입력하지 않았거나 존재하지 않는 코드를 입력했을 때.
        if(cropCd == null || cropCd.length() == 0 || !mgrCropDetailService.isExistByCode(cropCd)){
            errors.rejectValue("cropCd", Result.INVALID_CROPCD);
        }

        String growStep = userDiaryDto.getGrowStep();
        // growStep을 입력하지 않았거나 존재하지 않는 코드를 입력했을 때.
        if(growStep == null || growStep.length() == 0 || !mgrCropDetailService.isExistByCode(growStep)){
            errors.rejectValue("growStep", Result.INVALID_CROPCD);
        }

        String fmwrkCd = userDiaryDto.getFmwrkCd();
        // fmwrkCd를 입력하지 않았거나 존재하지 않는 코드를 입력했을 때.
        if(fmwrkCd == null || fmwrkCd.length() == 0 || !wrkcdService.isExistByCode(fmwrkCd)){
            errors.rejectValue("fmwrkCd", Result.INVALID_FMWRKCD);
        }

        if (userDiaryDto.getPackTCd() != null && userDiaryDto.getPackTCd().isEmpty() == false) {
            List<CodeDto> codeDto = codeService.listStartsWithCodeVal("PACK_T_CD", userDiaryDto.getPackTCd());
            if (codeDto == null || codeDto.size() == 0) {
                errors.rejectValue("packTCd", Result.INVALID_PACK_T_CD);
            }
        }

        if (userDiaryDto.getGradeTCd() != null && userDiaryDto.getGradeTCd().isEmpty() == false) {
            List<CodeDto> codeDto = codeService.listStartsWithCodeVal("GRADE_T_CD", userDiaryDto.getGradeTCd());
            if (codeDto == null || codeDto.size() == 0) {
                errors.rejectValue("gradeTCd", Result.INVALID_GRADE_T_CD);
            }
        }
    }

}
