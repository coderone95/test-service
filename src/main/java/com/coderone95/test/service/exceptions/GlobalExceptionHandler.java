package com.coderone95.test.service.exceptions;

import com.coderone95.test.service.model.ErrorDetails;
import com.coderone95.test.service.model.ErrorResponse;
import com.coderone95.test.service.model.ExceptionDetails;
import com.coderone95.test.service.model.Status;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorList);
        return handleExceptionInternal(null, errorDetails, null, errorDetails.getStatus(), request);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorResponse> genericExceptionForQuestion(GenericException ex, WebRequest req){
        //ExceptionDetails exception = new ExceptionDetails(new Date(),ex.getMessage(), req.getDescription(false));
        ErrorResponse err = new ErrorResponse(ex.getMessage(),new Status("ERROR"));
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyAnswersForQuestion.class)
    public ResponseEntity<ExceptionDetails> emptyAnswersListForQuestion(EmptyAnswersForQuestion ex, WebRequest req){
        ExceptionDetails exception = new ExceptionDetails(new Date(),ex.getMessage(), req.getDescription(false));
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

}
