package com.example.find_your_partner.common.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(){
        super("Invalid email or password.");
    }
}
