package com.backtester.dataIngestor.repository;

import com.backtester.dataIngestor.entity.MarketData;
import com.backtester.dataIngestor.entity.Symbol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, Long> {

    // Get OHLCV data for a symbol between two timestamps (for backtesting slices)
    List<MarketData> findBySymbolAndTimestampBetween(
            Symbol symbol,
            LocalDateTime start,
            LocalDateTime end
    );

    Page<MarketData> findBySymbolAndTimestampBetween(
            Symbol symbol,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    // Get all candles for a symbol ordered by time (useful for full-history backtests)
    List<MarketData> findBySymbolOrderByTimestampAsc(Symbol symbol);

    // Delete all market data rows for a given symbol (e.g., re-upload clean history)
    void deleteBySymbol(Symbol symbol);

    Optional<MarketData> findTopBySymbolOrderByTimestampDesc(Symbol symbol);
}
