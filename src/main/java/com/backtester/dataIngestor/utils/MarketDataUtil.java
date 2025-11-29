package com.backtester.dataIngestor.utils;

import com.backtester.dataIngestor.exception.DataProcessingException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MarketDataUtil {

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    public static LocalDateTime parseFlexibleTimeStamp(String date, int lineNumber) {
        for (DateTimeFormatter dateTimeFormatter : DATE_FORMATTERS) {
            try {
                return LocalDateTime.parse(date, dateTimeFormatter);
            } catch (DateTimeParseException dateTimeParseException) {

            }
        }
        throw new DataProcessingException("Invalid timestamp format at line " + lineNumber + ": " + date);
    }


    public static void validateOhlc(double high, double low, double open, double close, int lineNumber) {
        if (high < low) {
            throw new DataProcessingException("High cannot be less than low at line " + lineNumber);
        }
        if (high < Math.max(open, close) || low > Math.min(open, close)) {
            throw new DataProcessingException("Invalid OHLC range at line " + lineNumber);
        }
    }
}
