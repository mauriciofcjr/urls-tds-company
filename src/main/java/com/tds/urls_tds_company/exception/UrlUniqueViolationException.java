package com.tds.urls_tds_company.exception;

public class UrlUniqueViolationException extends RuntimeException{
    public UrlUniqueViolationException(String message){
        super(message);
    }

}
