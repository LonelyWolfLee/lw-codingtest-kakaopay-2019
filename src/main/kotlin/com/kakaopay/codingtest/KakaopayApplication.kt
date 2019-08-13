package com.kakaopay.codingtest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KakaopayApplication

fun main(args: Array<String>) {
    runApplication<KakaopayApplication>(*args)
}