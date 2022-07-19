package ru.mrpetchenka.flanscore.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Backend {
    String modid = "flanscore";
    String name = "FlansCore";
    String version = "0.0.5";
    String clientSide = "ru.mrpetchenka." + modid + ".client.ClientProxy";
    String serverSide = "ru.mrpetchenka." + modid + ".common.CommonProxy";
    String author = "mrpetchenka";
}
