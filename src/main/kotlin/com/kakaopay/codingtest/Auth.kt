package com.kakaopay.codingtest

import com.fasterxml.jackson.annotation.JsonProperty
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.Authorization
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

class AuthException(message: String?) : RuntimeException(message)
private const val JWT_SECRET_KEY: String = "Kakaopay Web Token Verification"
private const val JWT_EXPIRED_DURATION: Long = 1000*60*60

@RestController
@RequestMapping("api/auth")
class AuthController(val service: AuthService) {

    @PostMapping("signup")
    fun signup(@RequestBody request: Account): AuthToken {
        return AuthToken(service.signup(request.userId, request.password))
    }

    @PostMapping("signin")
    fun signin(@RequestBody request: Account): AuthToken {
        return AuthToken(service.signin(request.userId, request.password))
    }

    @PostMapping("refresh")
    fun refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String): AuthToken {
        val authHeaderElement = authorization.split(" ")
        if (authHeaderElement[0] != "Bearer") {
            throw AuthException("Invalid auth format")
        }
        return AuthToken(service.refresh(authHeaderElement[1]))
    }
}

@Service
class AuthService(val authUserRepository: AuthUserRepository) {
    private val log = KotlinLogging.logger { }

    fun signup(userId: String, password: String): String {
        val newToken = generateJwt(userId)
        return authUserRepository.save(AuthUser(userId, password, newToken)).token
    }

    fun signin(userId: String, password: String): String {
        val authUser =
                authUserRepository.findAuthUserByUserIdAndPassword(userId, password) ?: throw AuthException("No valid user")
        val newToken = generateJwt(authUser.userId)
        authUser.token = newToken
        return authUserRepository.save(authUser).token
    }

    fun refresh(token: String): String {
        val authUser = verify(token) ?: throw AuthException("No valid user")
        val newToken = generateJwt(authUser.userId)
        authUser.token = newToken
        return authUserRepository.save(authUser).token
    }

    private fun generateJwt(userId: String): String {

        val signatureAlgorithm = SignatureAlgorithm.HS256
        val expireTime = Date(Date().time + JWT_EXPIRED_DURATION)
        val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(JWT_SECRET_KEY)
        val signingKey = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)

        val headerMap = HashMap<String, Any>()

        headerMap["typ"] = "JWT"
        headerMap["alg"] = "HS256"

        val claims = HashMap<String, Any>()

        claims["user_id"] = userId

        val builder = Jwts.builder()
                .setHeader(headerMap)
                .setClaims(claims)
                .setExpiration(expireTime)
                .signWith(signatureAlgorithm, signingKey)

        return builder.compact()
    }

    fun verify(token: String): AuthUser? {
        try {
            val claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(JWT_SECRET_KEY))
                    .parseClaimsJws(token).body // 정상 수행된다면 해당 토큰은 정상토큰

            log.debug("token expireTime :" + claims.expiration)
            val userId = claims["user_id"].toString()
            return authUserRepository.findAuthUserByUserIdAndToken(userId, token)

        } catch (exception: ExpiredJwtException) {
            val msg = "Auth token '$token' is expired"
            log.error(msg, exception)
            throw AuthException(msg)
        } catch (exception: JwtException) {
            val msg = "Invalid auth token '$token'"
            log.error(msg, exception)
            throw AuthException(msg)
        }
    }
}

data class Account (
        @JsonProperty("user_id")
        val userId: String,
        val password: String
)

data class AuthToken(val token: String)

