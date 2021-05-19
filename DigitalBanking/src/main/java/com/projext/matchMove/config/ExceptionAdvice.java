package com.projext.matchMove.config;

import com.projext.matchMove.domain.ExceptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.ws.rs.WebApplicationException;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class ExceptionAdvice {

    @ExceptionHandler(value = WebApplicationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ExceptionDTO handleWebApplicationException(final WebApplicationException ex, WebRequest req) {
        log.info("WebApplicationException", ex);
        return new ExceptionDTO(ex, HttpStatus.valueOf(ex.getResponse().getStatus()));
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionDTO handleRuntimeException(final RuntimeException ex, WebRequest req) {
        log.info("Runtime Exception", ex);
        return new ExceptionDTO(ex, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
