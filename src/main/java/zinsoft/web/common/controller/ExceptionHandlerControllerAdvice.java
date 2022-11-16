package zinsoft.web.common.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import zinsoft.util.AppPropertyUtil;
import zinsoft.util.ApplicationContextProvider;
import zinsoft.util.Constants;
import zinsoft.util.HttpLoggingUtil;
import zinsoft.util.LocaleUtil;
import zinsoft.util.Result;
import zinsoft.web.exception.CodeMessageException;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerControllerAdvice {

    private Result handleResult(Result result, HttpServletRequest request, HttpServletResponse response) {
        boolean projectWithFrontEnd = Constants.BOOLEAN_TRUE.equals(AppPropertyUtil.get(Constants.PROJECT_WITH_FRONTEND));
        String accept = request.getHeader("Accept");

        if (!projectWithFrontEnd || accept == null || !accept.contains("text/html")) {
            return result;
        } else {
            RequestDispatcher rd = request.getRequestDispatcher("/content/error");

            request.setAttribute("result", result);

            try {
                rd.forward(request, response);
            } catch (ServletException | IOException sioe) {
                return result;
            }

            return null;
        }
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result handleException(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        String logId = HttpLoggingUtil.error("", ex, request);
        return handleResult(new Result(false, Result.INTERNAL_SERVER_ERROR, (Object[]) null, logId), request, response);
    }

    @ExceptionHandler(CodeMessageException.class)
    @ResponseBody
    public Result handleException(CodeMessageException ex, HttpServletRequest request, HttpServletResponse response) {
        String statusCode = ex.getCode() != null && ex.getCode().length() >= 3 ? ex.getCode().substring(0, 3) : Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value());
        HttpStatus httpStatus = null;
        String logId = null;

        try {
            httpStatus = HttpStatus.valueOf(Integer.parseInt(statusCode, 10));
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        response.setStatus(httpStatus.value());

        if (httpStatus.is5xxServerError()) {
            Throwable e = ex.getCause() != null ? ex.getCause() : ex;
            logId = HttpLoggingUtil.error("", e, request);
        }

        Result result = ex.getResult();

        if (result == null) {
            result = new Result(false, ex.getCode(), new String[] { ex.getArg() }, logId);
        }

        return handleResult(result, request, response);
    }

    @ExceptionHandler({ BindException.class, MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> errors = new HashMap<>();

        BindingResult br = null;
        if (ex instanceof BindException) {
            br = ((BindException) ex).getBindingResult();
        } else {
            br = ((MethodArgumentNotValidException) ex).getBindingResult();
        }

        List<FieldError> feList = br.getFieldErrors();
        for (FieldError fe : feList) {
            if (!errors.containsKey(fe.getField())) {
                if(fe.getDefaultMessage() == null && (StringUtils.isBlank(fe.getCode()) == false)) {
                    String resultMsg = StringEscapeUtils.unescapeJava(ApplicationContextProvider.applicationContext().getMessage("code." + fe.getCode(), null, LocaleUtil.locale()));
                    errors.put(fe.getField(), resultMsg);
                } else {
                    errors.put(fe.getField(), fe.getDefaultMessage());
                }
            }
        }

        List<ObjectError> geList = br.getGlobalErrors();
        for (ObjectError ge : geList) {
            errors.put(ge.getObjectName(), ge.getDefaultMessage());
        }

        return handleResult(new Result(false, Result.BAD_REQUEST, errors), request, response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleException(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> errors = new HashMap<>();
        errors.put(ex.getParameterName(), ApplicationContextProvider.applicationContext().getMessage("org.hibernate.validator.constraints.NotBlank.message", null, Locale.getDefault()));

        return handleResult(new Result(false, Result.BAD_REQUEST, errors), request, response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleException(MethodArgumentTypeMismatchException ex, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> errors = new HashMap<>();
        errors.put(ex.getName(), ApplicationContextProvider.applicationContext().getMessage("z.validation.constraints.TypeMismatch.message", null, Locale.getDefault()));
        //ex.getRequiredType().getName();
        //ex.getValue().getClass().getSimpleName();

        return handleResult(new Result(false, Result.BAD_REQUEST, errors), request, response);
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleException(InvalidFormatException ex, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> errors = new HashMap<>();
        List<Reference> paths = ex.getPath();
        errors.put(paths.get(paths.size() - 1).getFieldName(), ApplicationContextProvider.applicationContext().getMessage("z.validation.constraints.TypeMismatch.message", null, Locale.getDefault()));

        return handleResult(new Result(false, Result.BAD_REQUEST, errors), request, response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleException(ConstraintViolationException ex, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> errors = new HashMap<>();
        Set<ConstraintViolation<?>> cv = ex.getConstraintViolations();

        if (cv != null) {
            for (@SuppressWarnings("rawtypes")
            ConstraintViolation c : cv) {
                errors.put(c.getPropertyPath().toString(), c.getMessage());
            }
        }

        return handleResult(new Result(false, Result.BAD_REQUEST, errors), request, response);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Result handleException(AuthenticationException ex, HttpServletRequest request, HttpServletResponse response) {
        log.error("로그인되지 않은 사용자가 접근함.");
        log.error("ex.getMessage() : " + ex.getMessage());
        ex.printStackTrace();
        return handleResult(new Result(false, Result.UNAUTHORIZED), request, response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public Result handleException(AccessDeniedException ex, HttpServletRequest request, HttpServletResponse response) {
        log.error("AccessDeniedException ex.message : {}", ex.getMessage());
        ex.printStackTrace();
        return handleResult(new Result(false, Result.FORBIDDEN), request, response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Result handleException(NoHandlerFoundException ex, HttpServletRequest request, HttpServletResponse response) {
        return handleResult(new Result(false, Result.NOT_FOUND), request, response);
    }

}
