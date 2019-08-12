package com.kakaopay.codingtest

import com.fasterxml.jackson.annotation.JsonProperty

data class AllFinanceInfoByYear (
        val name: String,
        val data: List<FinanceInfoByYear>
)

data class FinanceInfoByYear (
        val year: Int,
        @JsonProperty("total_amount")
        val totalAmount: Int,
        @JsonProperty("detail_amount")
        val detailAmount: Map<String, Int>
)

data class MostFinanceForAllYears (
        val year: Int,
        val bank: String
)

data class MostFinanceForAYear (
        val code: String,
        val amount: Int
)

data class MostAndLeast (
        val bank: String,
        @JsonProperty("support_amount")
        val supportAmount: List<AmountOfYear>
)

data class AmountOfYear (
        val year: Int,
        val amount: Int
)