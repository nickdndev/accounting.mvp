package io.nickdn.accounting.controller;

import io.nickdn.accounting.controller.input.AccountData;
import io.nickdn.accounting.controller.input.AmountRequest;
import io.nickdn.accounting.controller.input.TransferRequest;
import io.nickdn.accounting.controller.output.AccountDto;
import io.nickdn.accounting.controller.output.AccountResponse;
import io.nickdn.accounting.entity.Account;
import io.nickdn.accounting.service.AccountService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private ModelMapper modelMapper;
    private AccountService service;

    @GetMapping("/get")
    public AccountResponse<List<AccountDto>> get() {
        return new AccountResponse<List<AccountDto>>()
                .setResult(service.getAll().stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/get/{id}")
    public AccountResponse<AccountDto> get(@PathVariable Integer id) {
        return new AccountResponse<AccountDto>()
                .setResult(convertToDto(service.get(id)));
    }

    @PostMapping("/create")
    public AccountResponse<AccountDto> create(@Valid @RequestBody AccountData data) {
        return new AccountResponse<AccountDto>()
                .setResult(convertToDto(service.create(data)));
    }

    @PutMapping("/deposit")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AccountResponse<AccountDto> deposit(@Valid @RequestBody AmountRequest request) {
        return new AccountResponse<AccountDto>()
                .setResult(convertToDto(service.deposit(request)));
    }

    @PutMapping("/withdraw")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AccountResponse<AccountDto> withdraw(@Valid @RequestBody AmountRequest request) {
        return new AccountResponse<AccountDto>()
                .setResult(convertToDto(service.withdraw(request)));
    }

    @PutMapping("/transfer")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AccountResponse<List<AccountDto>> transfer(@Valid @RequestBody TransferRequest request) {
        return new AccountResponse<List<AccountDto>>()
                .setResult(service.transfer(request).stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()));
    }

    private AccountDto convertToDto(Account account) {
        return modelMapper.map(account, AccountDto.class);
    }
}
