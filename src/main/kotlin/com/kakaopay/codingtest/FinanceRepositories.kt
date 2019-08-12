package com.kakaopay.codingtest

import org.springframework.data.jpa.repository.JpaRepository

interface InstituteRepository: JpaRepository<Institute, Long>
interface FinanceDataRepository: JpaRepository<FinanceData, Long> {
    fun findAllByCode(code: String): List<FinanceData>
}