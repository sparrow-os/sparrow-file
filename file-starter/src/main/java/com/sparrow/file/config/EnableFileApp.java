package com.sparrow.file.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({FileAutoConfiguration.class})
public @interface EnableFileApp {
}
