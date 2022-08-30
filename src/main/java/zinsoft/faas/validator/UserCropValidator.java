package zinsoft.faas.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import zinsoft.faas.dto.UserCropDto;
import zinsoft.faas.service.CropService;

@Component
public class UserCropValidator implements Validator {

    @Autowired
    CropService cropService;

    @Override
    public boolean supports(Class<?> clazz) {
        // TODO Auto-generated method stub
        return UserCropDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // TODO Auto-generated method stub
        //ValidationUtils.rejectIfEmpty(errors, "userId", "userCropDto.userId.empty","아이디를 입력해주세요.");

        UserCropDto userCropDto = (UserCropDto)target;
//        if(userCropDto.getUserId() == null || userCropDto.getUserId().trim().isEmpty()) {
//            errors.rejectValue("userId", "required", "필수 정보 입니다");
//        }

        if(userCropDto.getCropSeq() > 0 ) {
            boolean res =  cropService.isExistCropId(userCropDto.getCropSeq());
            if(!res) {
                errors.rejectValue("cropSeq", "required", "없는 작물입니다");
            }
        }
    }

}
