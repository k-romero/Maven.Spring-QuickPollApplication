package io.zipcoder.tc_spring_poll_application.controllers.advice;

import io.zipcoder.tc_spring_poll_application.dtos.error.ErrorDetail;
import io.zipcoder.tc_spring_poll_application.dtos.error.ValidationError;
import io.zipcoder.tc_spring_poll_application.exception.ResourceNotFoundException;
import org.apache.tomcat.jni.Time;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rnfe, HttpServletRequest request){
        ErrorDetail error = new ErrorDetail();
        error.setTitle("Not Found");
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setDetail("Wrong time, wrong place");
        error.setTimeStamp(new Date().getTime());
        error.setDeveloperMessage("This came from " + rnfe.getCause() + "\n" + rnfe.getMessage());
        return new ResponseEntity<>(error.getDetail(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException manve, HttpServletRequest request){
        ErrorDetail error = new ErrorDetail();
        error.setTitle("Not a thing");
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setDetail("You must've made a bad request, try checking over your work");
        error.setTimeStamp(new Date().getTime());
        error.setDeveloperMessage("This came from " + manve.getCause() + "\n" + manve.getMessage());

        List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();
        for(FieldError fe : fieldErrors){

            List<ValidationError> vErrorList = error.getErrors().get(fe.getField());
            if(vErrorList == null){
                vErrorList = new ArrayList<>();
                error.getErrors().put(fe.getField(),vErrorList);
            }
            ValidationError validationError = new ValidationError();
            validationError.setCode(fe.getCode());

            //todo check to see if this .setMessage is accurate
            validationError.setMessage(fe.getDefaultMessage());
            vErrorList.add(validationError);
        }
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

}
