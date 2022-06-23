package ru.mrpetchenka.flanscore.utils;

public enum EnumLog {
    Notice("\u001B[32m"),
    Verbose("\u001B[36m"),
    Debug("\u001B[34m"),
    Warning("\u001B[33m"),
    Error("\u001B[31m"),
    Critical("\u001B[35m");

    private final String color;

    EnumLog(String color) {
        this.color = color;
    }

    public String getFormat() {
        return this.color + this + "\u001B[0m";
    }
}
