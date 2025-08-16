package com.sesame.sesamelog.controller

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping

@RestController
class MainController {
    @GetMapping("/")
    fun home(): String {
        return "Hello World!"
    }
}