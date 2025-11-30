package com.backtester.dataIngestor.service;

import com.backtester.dataIngestor.entity.MarketData;
import com.backtester.dataIngestor.responses.UploadResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MarketDataService {

    UploadResult loadMarketDataFromFile(MultipartFile file, String ticker);

    List<MarketData> getMarketData(String ticker, LocalDateTime start, LocalDateTime end);

    Page<MarketData> getMarketDataPaginated(String ticker, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Optional<MarketData> getLatestMarketData(String ticker);

    void deleteMarketDataBySymbol(String ticker);
}
