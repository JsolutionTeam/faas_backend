package zinsoft.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import zinsoft.faas.validator.CommonValidator;

@ControllerAdvice
public class ValidatorControllerAdvice {

    @Autowired
    CommonValidator commonValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(commonValidator);
    }

}
