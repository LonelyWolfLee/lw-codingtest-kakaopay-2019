package com.kakaopay.codingtest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class FinanceController(val service: FinanceService) {

    @GetMapping("info")
    fun getInfo(): String = "Kakaopay Coding Test 2019"


}