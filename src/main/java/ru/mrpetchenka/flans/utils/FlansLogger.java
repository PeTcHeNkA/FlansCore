package ru.mrpetchenka.flans.utils;

public class FlansLogger {
    public static void log(FlansEnumLog type, String log) {
        System.out.println("[" + type.getFormat() + "] [" + FlansBackend.name + "]: " + log);
    }
}
