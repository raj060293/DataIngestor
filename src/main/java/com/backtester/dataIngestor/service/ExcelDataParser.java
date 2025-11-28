package com.backtester.dataIngestor.service;

import com.backtester.dataIngestor.entity.MarketData;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelDataParser implements  MarketDataParser{

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    @Override
    public List<MarketData> parse(InputStream inputStream) throws IOException {
        return List.of();
    }

}
