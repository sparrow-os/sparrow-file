package com.sparrow.file.config;

import com.sparrow.spring.mvc.ControllerReturnAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.sparrow.file.controller"})
public class FileControllerAdvice extends ControllerReturnAdvice {
}
