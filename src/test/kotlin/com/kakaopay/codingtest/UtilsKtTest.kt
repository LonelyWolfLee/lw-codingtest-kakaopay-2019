package com.kakaopay.codingtest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class UtilsKtTest {

    @Test
    fun `Each bank code is converted to proper name`() {
        assertThat(instituteCodeToName("bnk1")).isEqualTo("주택도시기금")
        assertThat(instituteCodeToName("bnk2")).isEqualTo("국민은행")
        assertThat(instituteCodeToName("bnk3")).isEqualTo("우리은행")
        assertThat(instituteCodeToName("bnk4")).isEqualTo("신한은행")
        assertThat(instituteCodeToName("bnk5")).isEqualTo("한국시티은행")
        assertThat(instituteCodeToName("bnk6")).isEqualTo("하나은행")
        assertThat(instituteCodeToName("bnk7")).isEqualTo("농협은행/수협은행")
        assertThat(instituteCodeToName("bnk8")).isEqualTo("외환은행")
        assertThat(instituteCodeToName("bnk9")).isEqualTo("기타은행")
    }

    @Test
    fun `Each bank name has code`() {
        assertThat(findInstituteCodeForName("주택도시기금")).isEqualTo("bnk1")
        assertThat(findInstituteCodeForName("국민은행")).isEqualTo("bnk2")
        assertThat(findInstituteCodeForName("우리은행")).isEqualTo("bnk3")
        assertThat(findInstituteCodeForName("신한은행")).isEqualTo("bnk4")
        assertThat(findInstituteCodeForName("한국시티은행")).isEqualTo("bnk5")
        assertThat(findInstituteCodeForName("하나은행")).isEqualTo("bnk6")
        assertThat(findInstituteCodeForName("농협은행/수협은행")).isEqualTo("bnk7")
        assertThat(findInstituteCodeForName("외환은행")).isEqualTo("bnk8")
        assertThat(findInstituteCodeForName("기타은행")).isEqualTo("bnk9")
    }
}