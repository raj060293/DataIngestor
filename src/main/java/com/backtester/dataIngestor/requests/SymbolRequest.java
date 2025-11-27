package com.backtester.dataIngestor.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SymbolRequest {

    @NotBlank(message = "Ticker is required")
    @Size(max = 20, message = "Ticker must be at most 20 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Ticker must be uppercase alphanumeric without spaces")
    private String name;

    @Size(max = 100, message = "Name must be at most 100 characters")
    private String ticker;


}
