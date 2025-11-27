package com.backtester.dataIngestor.service;

import com.backtester.dataIngestor.dto.SymbolDto;
import com.backtester.dataIngestor.entity.Symbol;
import com.backtester.dataIngestor.repository.SymbolRepository;
import com.backtester.dataIngestor.requests.SymbolRequest;
import com.backtester.dataIngestor.utils.SymbolUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SymbolServiceImpl implements SymbolService{

    private SymbolRepository symbolRepository;

    public SymbolServiceImpl(SymbolRepository symbolRepository) {
        this.symbolRepository = symbolRepository;
    }

    @Override
    public List<SymbolDto> getAllSymbols() {
        List<Symbol> symbolList = symbolRepository.findAll();
        List<SymbolDto> symbolDtoList = symbolList.stream().map(SymbolUtil::mapSymbolEntityToDto).toList();
        return symbolDtoList;
    }

    public SymbolDto addSymbol(SymbolRequest symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("Symbol cannot be null");
        }

        String ticker = symbol.getTicker();
        if (!StringUtils.hasText(ticker)) {
            throw new IllegalArgumentException("Ticker cannot be null or empty");
        }

        // Optional: simple regex check for ticker format (uppercase letters and numbers only)
        if (!ticker.matches("^[A-Z0-9]+$")) {
            throw new IllegalArgumentException("Ticker must be uppercase alphanumeric without spaces");
        }

        // Check if ticker already exists
        if (symbolRepository.findByTicker(ticker).isPresent()) {
            throw new IllegalStateException("Ticker '" + ticker + "' already exists");
        }

        Symbol symbolEntity = new Symbol();
        symbolEntity.setName(symbol.getName());
        symbolEntity.setTicker(symbol.getTicker());
        Symbol savedSymbol = symbolRepository.save(symbolEntity);
        return SymbolUtil.mapSymbolEntityToDto(savedSymbol);
    }

    public void deleteSymbolById(Long id) {
        symbolRepository.deleteById(id);
    }

    public void deleteSymbolByTicker(String ticker) {
        Optional<Symbol> savedTicker = symbolRepository.findByTicker(ticker);
        savedTicker.ifPresent(symbol -> deleteSymbolById(symbol.getId()));
    }
}
