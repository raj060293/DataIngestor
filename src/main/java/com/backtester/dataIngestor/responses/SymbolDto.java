package com.backtester.dataIngestor.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SymbolDto {

    private Long symbolId;

    private String name;

    private String ticker;

}
