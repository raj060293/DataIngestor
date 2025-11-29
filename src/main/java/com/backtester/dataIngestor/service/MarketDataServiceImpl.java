package com.backtester.dataIngestor.service;

import com.backtester.dataIngestor.entity.MarketData;
import com.backtester.dataIngestor.entity.Symbol;
import com.backtester.dataIngestor.exception.DataProcessingException;
import com.backtester.dataIngestor.repository.MarketDataRepository;
import com.backtester.dataIngestor.responses.UploadResult;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MarketDataServiceImpl implements MarketDataService{

    private final MarketDataRepository marketDataRepository;
    private final SymbolService symbolService;

    public MarketDataServiceImpl(MarketDataRepository marketDataRepository, SymbolService symbolService) {
        this.marketDataRepository = marketDataRepository;
        this.symbolService = symbolService;
    }

    public UploadResult loadMarketDataFromFile(MultipartFile file, String ticker) {
        validateFile(file);
        Symbol symbol = symbolService.getOrCreateSymbol(ticker);
        MarketDataParser parser = MarketDataParserFactory.getParser(file);
        List<MarketData> marketDataList;
        try {
            marketDataList = parser.parse(file.getInputStream());
        } catch (IOException e) {
            throw new DataProcessingException("Failed to parse file: " + e.getMessage());
        }
        marketDataRepository.deleteBySymbol(symbol);
        List<MarketData> savedMarketDataList = marketDataRepository.saveAll(marketDataList);
        return new UploadResult(savedMarketDataList.size(), symbol.getTicker());
    }

    public List<MarketData> getMarketData(String ticker, LocalDateTime start, LocalDateTime end) {
       // Symbol s = symbolService
        //return marketDataRepository.findBySymbolAndTimestampBetween()
        return null;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

    }


}
