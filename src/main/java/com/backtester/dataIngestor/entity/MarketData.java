package com.backtester.dataIngestor.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name="market_data",
        indexes = {
                @Index(name = "idx_symbol_timestamp", columnList = "symbol_id, timestamp")
        }
)
public class MarketData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "symbol_id", nullable = false)
    private Symbol symbol;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "open_price", nullable = false)
    private double open;

    @Column(name = "high_price", nullable = false)
    private double high;

    @Column(name = "low_price", nullable = false)
    private double low;

    @Column(name = "close_price", nullable = false)
    private double close;

    @Column(name = "volume", nullable = false)
    private long volume;

    public MarketData() {}

    public MarketData(Symbol symbol, LocalDateTime timestamp, double open, double high, double low, double close, long volume) {
        this.symbol = symbol;
        this.timestamp = timestamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }


}
