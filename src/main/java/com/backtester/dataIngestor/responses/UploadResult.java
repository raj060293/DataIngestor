package com.backtester.dataIngestor.responses;

import lombok.Getter;

@Getter
public class UploadResult {

    private final int recordsProcessed;
    private final String symbolTicker;

    public UploadResult(int recordsProcessed, String symbolTicker) {
        this.recordsProcessed = recordsProcessed;
        this.symbolTicker = symbolTicker;
    }
}
