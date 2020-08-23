package io.nickdn.accounting.service;

import io.nickdn.accounting.controller.input.AccountData;
import io.nickdn.accounting.controller.input.AmountRequest;
import io.nickdn.accounting.controller.input.TransferRequest;
import io.nickdn.accounting.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.nickdn.accounting.entity.Account;
import io.nickdn.accounting.exception.AccountNotFoundException;
import io.nickdn.accounting.exception.NotAvailableBalance;

import java.util.List;
import javax.persistence.OptimisticLockException;

import static java.util.Arrays.asList;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class AccountService {
    private AccountRepository repository;

    public Account get(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public List<Account> getAll() {
        return repository.findAll();
    }

    public Account create(AccountData data) {
        Account newAcc = new Account()
                .setUserId(data.getUserId())
                .setBalance(data.getBalance());
        return repository.save(newAcc);
    }

    @Retryable(value = OptimisticLockException.class, maxAttempts = 5)
    public Account deposit(AmountRequest req) {
        Account account = repository.findById(req.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(req.getAccountId()));
        account.setBalance(account.getBalance() + req.getAmount());
        return repository.save(account);
    }

    @Retryable(value = OptimisticLockException.class, maxAttempts = 5)
    public Account withdraw(AmountRequest req) {
        Account account = get(req.getAccountId());
        long balance = account.getBalance();
        long amount = req.getAmount();
        checkBalance(account, balance, amount);

        account.setBalance(balance - amount);
        return repository.save(account);
    }

    @Retryable(value = OptimisticLockException.class, maxAttempts = 5)
    public List<Account> transfer(TransferRequest req) {
        Account from = get(req.getFromAccountId());
        Account to = get(req.getToAccountId());

        long balanceFrom = from.getBalance();
        long amount = req.getAmount();
        checkBalance(from, balanceFrom, amount);

        from.setBalance(balanceFrom - amount);
        to.setBalance(to.getBalance() + amount);
        return repository.saveAll(asList(from, to));
    }

    private void checkBalance(Account account, long balance, long amount) {
        if (balance < amount) {
            throw new NotAvailableBalance(account.getId());
        }
    }
}
