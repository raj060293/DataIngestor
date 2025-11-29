package com.backtester.dataIngestor.controller;

import com.backtester.dataIngestor.responses.SymbolDto;
import com.backtester.dataIngestor.requests.SymbolRequest;
import com.backtester.dataIngestor.service.SymbolService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/symbols")
public class SymbolController {

    private final SymbolService symbolService;

    public SymbolController(SymbolService symbolService) {
        this.symbolService = symbolService;
    }

    @GetMapping
    public ResponseEntity<List<SymbolDto>> getAllSymbols() {
        List<SymbolDto> symbolDto = symbolService.getAllSymbols();
        return ResponseEntity.ok(symbolDto);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSymbol(@PathVariable Long id) {
        symbolService.deleteSymbolById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteSymbolByTicker(@PathVariable String ticker) {
        symbolService.deleteSymbolByTicker(ticker);
        return ResponseEntity.noContent().build();
    }

}
