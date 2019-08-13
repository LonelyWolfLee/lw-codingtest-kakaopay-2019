package com.kakaopay.codingtest

import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/finance")
class FinanceController(
        val service: FinanceService
) {

    @NeedAuthorized
    @ApiImplicitParams(
            ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "authorization", required = true, dataType = "string", paramType = "header")
    )
    @PostMapping("load-data")
    fun loadData(): String {
        val os = System.getProperty("os.name")
        val charsetName = if (os.toLowerCase().contains("mac")) "cp949" else "utf8"
        return if(service.loadData(charsetName)) "success" else "fail"
    }

    @NeedAuthorized
    @ApiImplicitParams(
            ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "authorization", required = true, dataType = "string", paramType = "header")
    )
    @GetMapping("institutes")
    fun institutes(): List<String> {
        return service.getInstitutes()
    }

    @NeedAuthorized
    @ApiImplicitParams(
            ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "authorization", required = true, dataType = "string", paramType = "header")
    )
    @GetMapping("by-year")
    fun allFinanceInfoByYear(): AllFinanceInfoByYear {
        return service.getAllFinanceInfoByYear()
    }

    @NeedAuthorized
    @ApiImplicitParams(
            ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "authorization", required = true, dataType = "string", paramType = "header")
    )
    @GetMapping("most-for-all-years")
    fun mostFinanceForAllYears(): MostFinanceForAllYears {
        return service.getMostFinanceForAllYears()
    }

    @NeedAuthorized
    @ApiImplicitParams(
            ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "authorization", required = true, dataType = "string", paramType = "header")
    )
    @GetMapping("bnk8/most-n-least")
    fun getMostAndLeastOf(): MostAndLeast {
        val instituteCode = "bnk8"
        return service.getMostAndLeastOf(instituteCode)
    }
}