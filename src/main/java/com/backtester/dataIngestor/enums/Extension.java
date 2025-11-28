package com.backtester.dataIngestor.enums;

public enum Extension {

    CSV("csv"),

    EXCEL("xlsx");

    private final String extension;

    Extension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static Extension fromExtension(String extension) {
        if (extension == null) {
            throw new IllegalArgumentException("Extension cannot be null");
        }

        for (Extension extensionValue: Extension.values()) {
            if (extensionValue.extension.equalsIgnoreCase(extension)) {
                return extensionValue;
            }
        }
        throw new IllegalArgumentException("Unknown strategy code: " + extension);
    }

}
