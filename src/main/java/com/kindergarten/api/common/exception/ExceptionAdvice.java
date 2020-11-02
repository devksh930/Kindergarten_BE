package com.kindergarten.api.common.exception;

import com.kindergarten.api.common.result.CommonResult;
import com.kindergarten.api.common.result.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    private final MessageSource messageSource;

    @ExceptionHandler(CUserExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CommonResult userExistException(HttpServletRequest request, CUserExistException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("existingUser.code")), getMessage("existingUser.msg"));
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult userNotfoundException(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("notfoundUser.code")), getMessage("notfoundUser.msg"));
    }

    @ExceptionHandler(CUserIncorrectPasswordException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult userIncorrectPasswordException(HttpServletRequest request, CUserIncorrectPasswordException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("incorrectPassword.code")), getMessage("incorrectPassword.msg"));
    }

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult authenticationEntryPointException(HttpServletRequest request, CAuthenticationEntryPointException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("entryPointException.code")), getMessage("entryPointException.msg"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("accessDenied.code")), getMessage("accessDenied.msg"));
    }

    @ExceptionHandler(CKinderGartenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult kindergartenNotfoundException(HttpServletRequest request, CKinderGartenNotFoundException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("notfoundKinder.code")), getMessage("notfoundKinder.msg"));
    }

    @ExceptionHandler(CReviewExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CommonResult reviewExistException(HttpServletRequest request, CKinderGartenNotFoundException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("existReview.code")), getMessage("existReview.msg"));
    }

    @ExceptionHandler(CStudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult studentNotFoundException(HttpServletRequest request, CKinderGartenNotFoundException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("notfoundStudent.code")), getMessage("notfoundStudent.msg"));
    }

    @ExceptionHandler(CNotOwnerException.class)
    @ResponseStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
    public CommonResult notOwnerException(HttpServletRequest request, CNotOwnerException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("notOwnerException.code")), getMessage("notOwnerException.msg"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult methodNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }
        return responseService.getFailResult(-1, getMessage(builder.toString()));
    }

    // code정보에 해당하는 메시지를 조회
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
