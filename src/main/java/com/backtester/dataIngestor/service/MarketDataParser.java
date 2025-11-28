package com.backtester.dataIngestor.service;

import com.backtester.dataIngestor.entity.MarketData;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface MarketDataParser {

    List<MarketData> parse(InputStream inputStream) throws IOException;
}
