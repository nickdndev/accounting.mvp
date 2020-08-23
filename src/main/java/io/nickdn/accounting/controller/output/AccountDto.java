package io.nickdn.accounting.controller.output;

import lombok.Data;

@Data
public class AccountDto {
    private Integer id;
    private Integer userId;
    private long balance;
}