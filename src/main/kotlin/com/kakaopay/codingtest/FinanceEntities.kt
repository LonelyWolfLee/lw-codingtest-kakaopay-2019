package com.kakaopay.codingtest

import javax.persistence.*

@Entity
class Institute(
        @Column(name = "institute_code")
        val code: String,
        @Column(name = "institute_name")
        val name: String,
        @Id @GeneratedValue var id: Long? = null
)

@Entity
@Table(
        name = "finance_data"
)
class FinanceData(
        val year: Int,
        val month: Int,
        @Column(name = "institute_code")
        val code: String,
        val amount: Int,
        @Id @GeneratedValue var id: Long? = null
)
