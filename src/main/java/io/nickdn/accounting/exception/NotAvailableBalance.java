package io.nickdn.accounting.exception;

public class NotAvailableBalance extends RuntimeException {
    public NotAvailableBalance(Integer id) {
        super(String.format("Not enough cash in the account with id = %d", id));
    }
}
