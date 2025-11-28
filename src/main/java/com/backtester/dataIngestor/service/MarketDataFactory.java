package com.backtester.dataIngestor.service;

import com.backtester.dataIngestor.enums.Extension;
import org.springframework.web.multipart.MultipartFile;

public class MarketDataFactory {

    private MarketDataFactory() {

    }

    public static MarketDataParser getParser(MultipartFile file) {
        String extension = extractFileExtension(file.getOriginalFilename()).toLowerCase();
        Extension extensionEnum = Extension.fromExtension(extension);
        switch (extensionEnum) {
            case CSV -> new CsvDataParser();
            case EXCEL -> new ExcelDataParser();
        }
        throw new IllegalArgumentException("Unsupported file type: " + extension);
    }

    private static String extractFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
