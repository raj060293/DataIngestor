package com.backtester.dataIngestor.service;

import com.backtester.dataIngestor.entity.MarketData;
import com.backtester.dataIngestor.exception.DataProcessingException;
import com.backtester.dataIngestor.utils.MarketDataUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.backtester.dataIngestor.utils.MarketDataUtil.parseFlexibleTimeStamp;
import static com.backtester.dataIngestor.utils.MarketDataUtil.validateOhlc;

public class ExcelDataParser implements  MarketDataParser{

    @Override
    public List<MarketData> parse(InputStream inputStream) throws IOException {
        List<MarketData> marketDataList = new ArrayList<>();

        try(Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean firstRow = true;
            int rowNumber = 0;

            for (Row row : sheet) {
                rowNumber++;
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                try {
                    MarketData marketData = parseExcelRows(rowNumber, row);
                    marketDataList.add(marketData);

                } catch (Exception e) {

                }
            }
        }
        return marketDataList;
    }

    private MarketData parseExcelRows(int rowNumber, Row row) {
        if (row.getCell(0) == null) {
            return null;
        }

        LocalDateTime timestamp = parseExcelTimestamp(row.getCell(0), rowNumber);
        double open = getNumericCellValue(row.getCell(1), rowNumber, "open");
        double high = getNumericCellValue(row.getCell(2), rowNumber, "high");
        double low = getNumericCellValue(row.getCell(3), rowNumber, "low");
        double close = getNumericCellValue(row.getCell(4), rowNumber, "close");
        long volume = getLongCellValue(row.getCell(5), rowNumber, "volume");

        validateOhlc(high, low, open, close ,rowNumber);

        return new MarketData(null, timestamp, open, high, low, close, volume);
    }

    private LocalDateTime parseExcelTimestamp(Cell cell, int rowNumber) {
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue();
        } else if (cell.getCellType() == CellType.STRING){
            LocalDateTime localDateTime = parseFlexibleTimeStamp(cell.getStringCellValue().trim(), rowNumber);
            return  localDateTime;
        }
        throw new DataProcessingException("Invalid timestamp at Excel row " + rowNumber);
    }

    private double getNumericCellValue(Cell cell, int rowNumber, String field) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            throw new DataProcessingException("Invalid " + field + " (must be numeric) at row " + rowNumber);
        }
        return cell.getNumericCellValue();
    }

    private long getLongCellValue(Cell cell, int rowNumber, String field) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            throw new DataProcessingException("Invalid " + field + " (must be numeric) at row " + rowNumber);
        }
        return (long) cell.getNumericCellValue();
    }

}
