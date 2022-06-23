package ru.mrpetchenka.flanscore.utils;

public class Logger {
    public static void log(EnumLog type, String log) {
        System.out.println("[" + type.getFormat() + "] [" + Backend.name + "]: " + log);
    }
}
