package com.sparrow.file.boot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class Controller {
    @RequestMapping("upload")
    public ModelAndView upload() {
        return new ModelAndView("/upload");
    }

    @RequestMapping("spring-upload")
    public ModelAndView springUpload() {
        return new ModelAndView("/spring-upload");
    }
}
