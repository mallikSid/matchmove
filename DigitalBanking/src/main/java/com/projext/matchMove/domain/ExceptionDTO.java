package com.projext.matchMove.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
public class ExceptionDTO {
    private Date timestamp;
    private int status;
    private String error;
    private String exception;
    private String message;
    private int level;
    private List<String> attachments;

    public ExceptionDTO(Exception e, HttpStatus status) {
        this.timestamp = Calendar.getInstance(Locale.getDefault()).getTime();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.exception = e.getClass().getCanonicalName();
        this.message = e.getMessage();
    }

    public ExceptionDTO(Exception e, String message, HttpStatus status) {
        this.timestamp = Calendar.getInstance(Locale.getDefault()).getTime();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.exception = e.getClass().getCanonicalName();
        this.message = "Oops! Something seems to be broken. Please try again after some time.";
    }

    public ExceptionDTO(Exception e, HttpStatus status, List<String> attachments) {
        this.timestamp = Calendar.getInstance(Locale.getDefault()).getTime();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.exception = e.getClass().getCanonicalName();
        this.message = e.getMessage();
        this.attachments = attachments;
    }
}