package com.matrix.um.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yihaosun
 * @date 2022/6/16 17:06
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "OK";
    }
}
