package io.nickdn.accounting.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Integer id) {
        super(String.format("Account with id = %d not found", id));
    }
}
