package io.nickdn.accounting.controller.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
public class AccountResponse<T> {
    private T result;
    private boolean success = true;
    private String extraInfo;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX[ VV]")
    ZonedDateTime currentDate = ZonedDateTime.now();
}
