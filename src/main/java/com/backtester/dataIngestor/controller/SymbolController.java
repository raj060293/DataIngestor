package com.backtester.dataIngestor.controller;

import com.backtester.dataIngestor.responses.SymbolDto;
import com.backtester.dataIngestor.requests.SymbolRequest;
import com.backtester.dataIngestor.service.SymbolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/symbols")
@Tag(name= "Symbols", description = "Operations related to financial symbols and tickers")
public class SymbolController {

    private final SymbolService symbolService;

    public SymbolController(SymbolService symbolService) {
        this.symbolService = symbolService;
    }

    @Operation(
            summary = "Get all symbols",
            description = "Retrieve a list of all financial symbols",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of symbols retrieved",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SymbolDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<SymbolDto>> getAllSymbols() {
        List<SymbolDto> symbolDto = symbolService.getAllSymbols();
        return ResponseEntity.ok(symbolDto);
    }

    @Operation(
            summary = "Add a new symbol",
            description = "Create a new financial symbol with its ticker and name",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Symbol created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SymbolDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Symbol already exists", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<?> addSymbol(@Valid @RequestBody SymbolRequest symbolRequest) {
        try {
            SymbolDto symbolDto = symbolService.addSymbol(symbolRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(symbolDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());

        } catch(IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    @Operation(
            summary = "Delete a symbol by ID",
            description = "Deletes a financial symbol by its unique ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Symbol deleted"),
                    @ApiResponse(responseCode = "404", description = "Symbol not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSymbol(@PathVariable Long id) {
        symbolService.deleteSymbolById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete a symbol by ticker",
            description = "Deletes a financial symbol using its ticker symbol",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Symbol deleted"),
                    @ApiResponse(responseCode = "404", description = "Symbol not found")
            }
    )
    @DeleteMapping("/ticker/{ticker}")
    public ResponseEntity<Void> deleteSymbolByTicker(@PathVariable String ticker) {
        symbolService.deleteSymbolByTicker(ticker);
        return ResponseEntity.noContent().build();
    }

}
