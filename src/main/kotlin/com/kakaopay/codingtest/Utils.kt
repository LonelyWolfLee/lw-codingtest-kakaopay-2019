package com.kakaopay.codingtest

import java.lang.RuntimeException

fun bankCodeToName(code: String): String = when(code) {
    "bnk1" -> "주택도시기금"
    "bnk2" -> "국민은행"
    "bnk3" -> "우리은행"
    "bnk4" -> "신한은행"
    "bnk5" -> "한국씨티은행"
    "bnk6" -> "하나은행"
    "bnk7" -> "농협은행/수협은행"
    "bnk8" -> "외환은행"
    "bnk9" -> "기타은행"
    else -> throw InvalidBankException("No bank exist (code=$code)")
}

fun findBankCodeForName(name: String): String = with(name) {
    when {
        contains("주택도시기금") -> "bnk1"
        contains("국민은행") -> "bnk2"
        contains("우리은행") -> "bnk3"
        contains("신한은행") -> "bnk4"
        contains("한국씨티은행") -> "bnk5"
        contains("하나은행") -> "bnk6"
        contains("농협은행/수협은행") -> "bnk7"
        contains("외환은행") -> "bnk8"
        contains("기타은행") -> "bnk9"
        else -> throw InvalidBankException("No bank code (name=$name)")
    }
}

class InvalidBankException(message: String?) : RuntimeException(message)