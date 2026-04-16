package org.example.dataproviderutils;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataProviderConfig {

    String key();

    String fields();
}