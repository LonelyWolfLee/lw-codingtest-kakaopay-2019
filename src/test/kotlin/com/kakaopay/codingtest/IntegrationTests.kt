package com.kakaopay.codingtest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests(
        @Autowired val restTemplate: TestRestTemplate,
        @Autowired val authService: AuthService
) {

    @Test
    fun `Auth signup can verified`() {
        val userId = "test id1"
        val password = "0000"

        val entity = restTemplate.postForEntity<AuthToken>("/api/auth/signup", Account(userId, password))
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        val authToken = entity.body
        assertThat(authService.verify(authToken!!.token)!!.userId).isEqualTo(userId)
    }

    @Test
    fun `Auth can signin after signup`() {
        val userId = "test id2"
        val password = "0000"

        val entity = restTemplate.postForEntity<AuthToken>("/api/auth/signup", Account(userId, password))
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        val entity2 = restTemplate.postForEntity<AuthToken>("/api/auth/signin", Account(userId, password))
        assertThat(entity2.statusCode).isEqualTo(HttpStatus.OK)
    }

}