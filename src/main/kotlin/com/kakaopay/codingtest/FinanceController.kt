package com.kakaopay.codingtest

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api")
class FinanceController(
        val service: FinanceService
) {

    @GetMapping("info")
    fun getInfo(): String = "Kakaopay Coding Test 2019"

    @PostMapping("mandatory/load-data")
    fun loadData(): String {
        val os = System.getProperty("os.name")
        val charsetName = if (os.toLowerCase().contains("mac")) "cp949" else "utf8"
        return if(service.loadData(charsetName)) "success" else "fail"
    }

    @GetMapping("mandatory/institutes")
    fun institutes(): List<String> {
        return service.getInstitutes()
    }

    @GetMapping("mandatory/finance/by-year")
    fun allFinanceInfoByYear(): AllFinanceInfoByYear {
        return service.getAllFinanceInfoByYear()
    }

    @GetMapping("mandatory/finance/most-for-all-years")
    fun mostFinanceForAllYears(): MostFinanceForAllYears {
        return service.getMostFinanceForAllYears()
    }

    @GetMapping("mandatory/finance/bnk8/most-n-least")
    fun getMostAndLeastOf(): MostAndLeast {
        val instituteCode = "bnk8"
        return service.getMostAndLeastOf(instituteCode)
    }
}