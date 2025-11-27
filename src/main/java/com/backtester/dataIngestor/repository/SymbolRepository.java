package com.backtester.dataIngestor.repository;

import com.backtester.dataIngestor.entity.Symbol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SymbolRepository extends JpaRepository<Symbol, Long> {

    // Find a symbol by its ticker (e.g., "RELIANCE", "AAPL")
    Optional<Symbol> findByTicker(String ticker);

}
