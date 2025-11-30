package com.backtester.dataIngestor.controller;

import com.backtester.dataIngestor.entity.MarketData;
import com.backtester.dataIngestor.exception.DataProcessingException;
import com.backtester.dataIngestor.responses.UploadResult;
import com.backtester.dataIngestor.service.MarketDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/market-data")
@Tag(name = "Market Data", description = "Endpoints for uploading and querying OHLCV market data")
public class MarketDataController {

    MarketDataService marketDataService;

    public MarketDataController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @Operation(summary = "Upload market data file",
            description = "Upload Csv or Excel file containing OHLCV data for a given symbol." +
                    "Existing symbol data is replaced",
            responses = {
            @ApiResponse(responseCode = "201", description = "Data uploaded successfully", content = @Content(schema = @Schema(implementation = UploadResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or file format"),
            @ApiResponse(responseCode = "500", description = "Server error while processing")
            }
    )
    @PostMapping("/upload")
    public ResponseEntity<?> uploadMarketData(
            @Parameter(description = "CSV/XLS/XLSX file with columns: timestamp,open,high,low,close,volume", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Ticker Symbol", required = true)
            @RequestParam("symbol") String ticker
    ) {
        try {
            UploadResult result = marketDataService.loadMarketDataFromFile(file, ticker);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException | DataProcessingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error while processing file: " + ex.getMessage());
        }
    }

    @Operation(
            summary = "Get Market Data for a symbol",
            description = "Returns OHLCV data for a symbol between start and end timestamps (non-paginated)."
    )
    @GetMapping
    public ResponseEntity<List<MarketData>>  getMarketData(
            @RequestParam String symbol,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<MarketData> marketData = marketDataService.getMarketData(symbol, start, end);
        if (marketData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(marketData);

    }

    @Operation(
            summary = "Get  Paginated Market Data for a symbol",
            description = "Returns Paginated OHLCV data for a symbol between start and end timestamps."
    )
    @GetMapping("/page")
    public ResponseEntity<Page<MarketData>>  getMarketDataPaginated(
            @RequestParam String symbol,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @ParameterObject Pageable pageable
            ) {

        Page<MarketData> page = marketDataService.getMarketDataPaginated(symbol, start, end, pageable);
        if (page.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(page);

    }

    @Operation(
            summary = "Get latest candle for symbol",
            description = "Returns the most OHLCV record for the given symbol"
    )
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestMarketData(@RequestParam String symbol) {
        return marketDataService.getLatestMarketData(symbol)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Operation(
            summary = "Delete all market data for symbol",
            description = "Deletes all stored OHLCV data for the given symbol."
    )
    @DeleteMapping
    public ResponseEntity<String> deleteMarketData(@RequestParam String symbol) {
        try {
            marketDataService.deleteMarketDataBySymbol(symbol);
            return ResponseEntity.ok("Deleted market data for symbol: " + symbol);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

    }






}
