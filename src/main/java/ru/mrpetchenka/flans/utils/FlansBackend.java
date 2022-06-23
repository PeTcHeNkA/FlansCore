package ru.mrpetchenka.flans.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FlansBackend {
    String modid = "flans";
    String name = "FlansCore";
    String version = "0.0.1";
    String clientSide = "ru.mrpetchenka.flans.client.ClientProxy";
    String serverSide = "ru.mrpetchenka.flans.common.CommonProxy";
    String author = "mrpetchenka";
}
