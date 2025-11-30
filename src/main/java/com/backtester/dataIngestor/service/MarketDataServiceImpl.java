package com.backtester.dataIngestor.service;

import com.backtester.dataIngestor.entity.MarketData;
import com.backtester.dataIngestor.entity.Symbol;
import com.backtester.dataIngestor.exception.DataProcessingException;
import com.backtester.dataIngestor.repository.MarketDataRepository;
import com.backtester.dataIngestor.responses.UploadResult;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MarketDataServiceImpl implements MarketDataService{

    private final MarketDataRepository marketDataRepository;
    private final SymbolService symbolService;

    public MarketDataServiceImpl(MarketDataRepository marketDataRepository, SymbolService symbolService) {
        this.marketDataRepository = marketDataRepository;
        this.symbolService = symbolService;
    }

    @Override
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

    @Override
    public List<MarketData> getMarketData(String ticker, LocalDateTime start, LocalDateTime end) {
        Symbol symbol = symbolService.getSymbolByTicker(ticker);
        return marketDataRepository.findBySymbolAndTimestampBetween(symbol, start, end);
    }

    @Override
    public Page<MarketData> getMarketDataPaginated(String ticker, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        Symbol symbol = symbolService.getSymbolByTicker(ticker);
        return marketDataRepository.findBySymbolAndTimestampBetween(symbol, start, end, pageable);

    }

    @Override
    public Optional<MarketData> getLatestMarketData(String ticker) {
        Symbol symbol = symbolService.getSymbolByTicker(ticker);
        return marketDataRepository.findTopBySymbolOrderByTimestampDesc(symbol);
    }

    @Override
    public void deleteMarketDataBySymbol(String ticker) {
        Symbol symbol = symbolService.getSymbolByTicker(ticker);
        marketDataRepository.deleteBySymbol(symbol);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

    }

}
