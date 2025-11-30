package com.backtester.dataIngestor.service;

import com.backtester.dataIngestor.entity.Symbol;
import com.backtester.dataIngestor.responses.SymbolDto;
import com.backtester.dataIngestor.requests.SymbolRequest;

import java.util.List;

public interface SymbolService {
    public List<SymbolDto> getAllSymbols();
    public SymbolDto addSymbol(SymbolRequest symbol);
    public void deleteSymbolById(Long id);
    public void deleteSymbolByTicker(String ticker);
    public Symbol getOrCreateSymbol(String ticker);
    public Symbol getSymbolByTicker(String ticker);
}
