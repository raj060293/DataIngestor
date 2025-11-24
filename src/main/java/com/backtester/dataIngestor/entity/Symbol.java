package com.backtester.dataIngestor.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "symbols")
public class Symbol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ticker", nullable = false, unique = true)
    private String ticker;

    @OneToMany(mappedBy = "symbol", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MarketData> marketData;

    public Symbol() {}

    public Symbol(String ticker, String name) {
        this.ticker = ticker;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Set<MarketData> getMarketData() {
        return marketData;
    }

    public void setMarketData(Set<MarketData> marketData) {
        this.marketData = marketData;
    }
}
