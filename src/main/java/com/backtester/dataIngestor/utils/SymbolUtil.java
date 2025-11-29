package com.backtester.dataIngestor.utils;

import com.backtester.dataIngestor.responses.SymbolDto;
import com.backtester.dataIngestor.entity.Symbol;

public class SymbolUtil {

    public static SymbolDto mapSymbolEntityToDto(Symbol symbol) {
        SymbolDto dto = new SymbolDto();
        dto.setName(symbol.getName());
        dto.setSymbolId(symbol.getId());
        dto.setTicker(symbol.getTicker());
        return dto;
    }
}
