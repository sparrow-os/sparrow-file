package com.sparrow.file.boot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class Controller {
    @RequestMapping("hello2")
    public ModelAndView hello() {
        return new ModelAndView("/hello");
    }
}
