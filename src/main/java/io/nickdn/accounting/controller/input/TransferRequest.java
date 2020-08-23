package io.nickdn.accounting.controller.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    @NotNull
    private Integer fromAccountId;
    @NotNull
    private Integer toAccountId;
    @Min(0)
    private long amount;
}
