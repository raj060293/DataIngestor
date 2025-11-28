package com.backtester.dataIngestor.service;

import com.backtester.dataIngestor.entity.MarketData;
import com.backtester.dataIngestor.exception.DataProcessingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CsvDataParser implements MarketDataParser{

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    @Override
    public List<MarketData> parse(InputStream inputStream) throws IOException {
        List<MarketData> marketDataList = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line = "";
            int lineNumber = 0;
            boolean isFirstLine = true;

            while((line = reader.readLine()) != null) {
                lineNumber++;
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    MarketData marketData = parseCsvLine(lineNumber, line);
                    if (marketData != null) {
                        marketDataList.add(marketData);
                    }
                } catch(Exception e) {

                }
            }
        }
        return marketDataList;
    }

    private MarketData parseCsvLine(int lineNumber, String line) {
        String[] tokens = line.split(",");
        if (tokens.length < 6) {
            throw new DataProcessingException("Expected 6 columns (timestamp,open,high,low,close,volume) at line "
                    + lineNumber);
        }

        LocalDateTime timestamp = parseFlexibleTimeStamp(tokens[0].trim(), lineNumber);
        double open = parseDouble(tokens[1].trim(), lineNumber, "open");
        double high = parseDouble(tokens[2].trim(), lineNumber, "high");
        double low = parseDouble(tokens[3].trim(), lineNumber, "low");
        double close = parseDouble(tokens[4].trim(), lineNumber, "close");
        long volume = parseLong(tokens[5].trim(), lineNumber, "volume");

        validateOhlc(high, low, open, close, lineNumber);

        return new MarketData(null, timestamp, open, high, low, close, volume);

    }

    private void validateOhlc(double high, double low, double open, double close, int lineNumber) {
        if (high < low) {
            throw new DataProcessingException("High cannot be less than low at line " + lineNumber);
        }
        if (high < Math.max(open, close) || low > Math.min(open, close)) {
            throw new DataProcessingException("Invalid OHLC range at line " + lineNumber);
        }
    }

    private LocalDateTime parseFlexibleTimeStamp(String date, int lineNumber) {
        for (DateTimeFormatter dateTimeFormatter : DATE_FORMATTERS) {
            try {
                return LocalDateTime.parse(date, dateTimeFormatter);
            } catch (DateTimeParseException dateTimeParseException) {

            }
        }
        throw new DataProcessingException("Invalid timestamp format at line " + lineNumber + ": " + date);
    }

    private double parseDouble(String value, int lineNumber, String fieldName) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException numberFormatException) {
            throw  new DataProcessingException("Invalid " + fieldName + " at line " + lineNumber + ": " + value);
        }
    }

    private long parseLong(String value, int lineNumber, String fieldName) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException numberFormatException) {
            throw  new DataProcessingException("Invalid " + fieldName + " at line " + lineNumber + ": " + value);
        }
    }
}
