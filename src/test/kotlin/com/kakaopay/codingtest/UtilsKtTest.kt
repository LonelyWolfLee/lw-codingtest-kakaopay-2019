package com.kakaopay.codingtest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class UtilsKtTest {

    @Test
    fun `Each bank code is converted to proper name`() {
        assertThat(bankCodeToName("bnk1")).isEqualTo("주택도시기금")
        assertThat(bankCodeToName("bnk2")).isEqualTo("국민은행")
        assertThat(bankCodeToName("bnk3")).isEqualTo("우리은행")
        assertThat(bankCodeToName("bnk4")).isEqualTo("신한은행")
        assertThat(bankCodeToName("bnk5")).isEqualTo("한국씨티은행")
        assertThat(bankCodeToName("bnk6")).isEqualTo("하나은행")
        assertThat(bankCodeToName("bnk7")).isEqualTo("농협은행/수협은행")
        assertThat(bankCodeToName("bnk8")).isEqualTo("외환은행")
        assertThat(bankCodeToName("bnk9")).isEqualTo("기타은행")
    }

    @Test
    fun `Each bank name has code`() {
        assertThat(findBankCodeForName("주택도시기금")).isEqualTo("bnk1")
        assertThat(findBankCodeForName("국민은행")).isEqualTo("bnk2")
        assertThat(findBankCodeForName("우리은행")).isEqualTo("bnk3")
        assertThat(findBankCodeForName("신한은행")).isEqualTo("bnk4")
        assertThat(findBankCodeForName("한국씨티은행")).isEqualTo("bnk5")
        assertThat(findBankCodeForName("하나은행")).isEqualTo("bnk6")
        assertThat(findBankCodeForName("농협은행/수협은행")).isEqualTo("bnk7")
        assertThat(findBankCodeForName("외환은행")).isEqualTo("bnk8")
        assertThat(findBankCodeForName("기타은행")).isEqualTo("bnk9")
    }
}