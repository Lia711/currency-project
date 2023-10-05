package com.example.CurrencyApplication;

public class ServerErrorException extends RuntimeException{
    public ServerErrorException() {
        super("Server error. Could not retrieve list of currencies.");
    }
}
