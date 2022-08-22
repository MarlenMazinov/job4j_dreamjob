package ru.job4j.dreamjob.controller;

import org.junit.runner.notification.RunListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RunListener.ThreadSafe
public class IndexControl {

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}