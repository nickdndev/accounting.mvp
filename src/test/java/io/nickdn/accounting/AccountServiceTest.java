package io.nickdn.accounting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import io.nickdn.accounting.controller.input.AccountData;
import io.nickdn.accounting.controller.input.AmountRequest;
import io.nickdn.accounting.controller.input.TransferRequest;
import io.nickdn.accounting.entity.Account;
import io.nickdn.accounting.exception.AccountNotFoundException;
import io.nickdn.accounting.exception.NotAvailableBalance;
import io.nickdn.accounting.service.AccountService;

import java.util.List;

import static java.util.Arrays.asList;


@SpringBootTest
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:database/schema.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:database/data.sql")
})
@ActiveProfiles(profiles = "test")
class AccountServiceTest {
    @Autowired
    private AccountService service;

    @Test
    void get() {
        Account expected = new Account(1, 20, 100L, 0L);
        Assertions.assertEquals(expected, service.get(1));
    }

    @Test
    void getAll() {
        List<Account> expected = asList(
                new Account(1, 20, 100L, 0L),
                new Account(2, 30, 200L, 0L)
        );
        Assertions.assertIterableEquals(expected, service.getAll());
    }

    @Test
    void getNotFoundAccountException() {
        Assertions.assertThrows(AccountNotFoundException.class, () -> service.get(999));
    }

    @Test
    void refill() {
        Account expected = new Account(1, 20, 150L, 1L);
        AmountRequest req = new AmountRequest(1, 50L);
        Assertions.assertEquals(expected, service.deposit(req));
    }

    @Test
    void withdraw() {
        Account expected = new Account(1, 20, 50L, 1L);
        AmountRequest req = new AmountRequest(1, 50L);
        Assertions.assertEquals(expected, service.withdraw(req));
    }

    @Test
    void withdrawNotAvailableBalanceException() {
        AmountRequest req = new AmountRequest(1, 120L);
        Assertions.assertThrows(NotAvailableBalance.class, () -> service.withdraw(req));
    }

    @Test
    void transfer() {
        List<Account> expected = asList(
                new Account(1, 20, 250L, 1L),
                new Account(2, 30, 50L, 1L)
        );
        TransferRequest req = new TransferRequest(2, 1, 150L);
        List<Account> actual = service.transfer(req);
        Assertions.assertTrue(expected.size() == actual.size() && expected.containsAll(actual));
    }

    @Test
    void transferNotAvailableBalanceException() {
        TransferRequest req = new TransferRequest(2, 1, 500L);
        Assertions.assertThrows(NotAvailableBalance.class, () -> service.transfer(req));
    }

    @Test
    void create() {
        Account expected = new Account(3, 99, 400L, 0L);
        AccountData req = new AccountData(99, 400L);
        Assertions.assertEquals(expected, service.create(req));
    }
}