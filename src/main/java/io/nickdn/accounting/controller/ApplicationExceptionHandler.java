package io.nickdn.accounting.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import io.nickdn.accounting.controller.output.AccountResponse;
import io.nickdn.accounting.exception.AccountNotFoundException;
import io.nickdn.accounting.exception.NotAvailableBalance;

import javax.persistence.OptimisticLockException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AccountNotFoundException.class, NotAvailableBalance.class, OptimisticLockException.class})
    protected ResponseEntity<AccountResponse<Object>> handleException(RuntimeException ex, WebRequest req) {
        log.error("Got exception: {}. Request: {}", ex, req);
        AccountResponse<Object> response = new AccountResponse<>()
                .setResult(null)
                .setSuccess(false)
                .setExtraInfo(ex.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }
}
