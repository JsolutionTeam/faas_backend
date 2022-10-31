package zinsoft.faas.validator;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import zinsoft.faas.dto.UserChemicalStockDto;
import zinsoft.faas.dto.UserCropDto;
import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.dto.UserManureStockDto;
import zinsoft.faas.dto.UserProductionDto;
import zinsoft.faas.dto.UserShipDto;
@Component
@RequiredArgsConstructor
@Slf4j
public class CommonValidator implements Validator {

    private final UserCropValidator userCropValidator;

    private final UserDiaryValidator userDiaryValidator;

    private final UserInoutValidator userInoutValidator;

    private final UserChemicalStockValidator userChemicalStockValidator;

    private final UserManureStockValidator userManureStockValidator;

    private final UserShipValidator userShipValidator;

    private final UserProductionValidator userProductionValidator;

    List<Class<?>> list = null;

    @PostConstruct
    public void init() {
        if(list == null) {
            String packageNm = "zinsoft.faas.dto";
            String packageNmSlash = "./" + packageNm.replace(".", "/");
            URL directoryURL = Thread.currentThread().getContextClassLoader().getResource(packageNmSlash);
            if(directoryURL == null) {
                System.out.println("Could not retrive URL resource : " + directoryURL);
            }else{
                String sDirectory = directoryURL.getFile();
                if(sDirectory == null) {
                    System.out.println("Could not find directory : " + sDirectory);
                }else{
                    File dirFile = new File(sDirectory);
                    if(dirFile.exists()) {
                        String[] files = dirFile.list();
                        list = new ArrayList<>();
                        for(String filename : files) {
                            filename = filename.substring(0, filename.length()-6);
                            try {
                                Class<?> c = Class.forName(packageNm+"."+filename);
                                list.add(c);
                            } catch (Exception e) {
                                // TODO: handle exception

                                e.printStackTrace();
                            }
                        }
                        list.add(File.class);
                    }
                }
            }
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
//        return UserCropDto.class.equals(clazz)
//                || UserChemicalDto.class.equals(clazz);
        return (list.indexOf(clazz) > -1);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // TODO Auto-generated method stub
        if(target instanceof UserCropDto) {
            ValidationUtils.invokeValidator(userCropValidator, target, errors);
        }

        if(target instanceof UserDiaryDto) {
            ValidationUtils.invokeValidator(userDiaryValidator, target, errors);
        }

        if(target instanceof UserInoutDto) {
            ValidationUtils.invokeValidator(userInoutValidator, target, errors);
        }

        if(target instanceof UserChemicalStockDto) {
            ValidationUtils.invokeValidator(userChemicalStockValidator, target, errors);
        }

        if(target instanceof UserManureStockDto) {
            ValidationUtils.invokeValidator(userManureStockValidator, target, errors);
        }

        if(target instanceof UserShipDto) {
            ValidationUtils.invokeValidator(userShipValidator, target, errors);
        }

        if(target instanceof UserProductionDto) {
            ValidationUtils.invokeValidator(userProductionValidator, target, errors);
        }



    }

}
