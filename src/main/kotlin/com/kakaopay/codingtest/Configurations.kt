package com.kakaopay.codingtest

import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@Configuration
class ServerConfig(private val interceptor: ApiInterceptor) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(interceptor)
    }
}

@Component
class ApiInterceptor(val authService: AuthService) : HandlerInterceptorAdapter() {
    private val log = KotlinLogging.logger { }

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        log.info { "PATH : ${request.requestURI}" }
        if (handler is HandlerMethod && handler.hasMethodAnnotation(NeedAuthorized::class.java)) {
            val authHeaderElement = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")
            if (authHeaderElement[0] != "Bearer" || authService.verify(authHeaderElement[1]) == null) {
                throw AuthException("No valid user")
            }
        }
        return true
    }

    @Throws(Exception::class)
    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        super.afterCompletion(request, response, handler, ex)

    }
}

@ControllerAdvice
class ServerExceptionHandler {

    private val log = KotlinLogging.logger { }

    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun handleException(e: Exception, response: HttpServletResponse) {
        val errMsg = e.message ?: "System error. Please retry later."
        log.error(e) {"exception : $errMsg"}
        response.sendError(HttpStatus.BAD_REQUEST.value(), errMsg)
    }

    @ExceptionHandler(AuthException::class)
    @ResponseBody
    fun handleDepositoryException(e: AuthException, response: HttpServletResponse) {
        val errMsg = e.message!!
        log.error(e) {"exception : $errMsg"}
        response.sendError(HttpStatus.BAD_REQUEST.value(), errMsg)
    }
}

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(ApiInfoBuilder()
                        .version("1.0.0")
                        .title("Kakaopay Coding Test API")
                        .description("Service API")
                        .build()
                )
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kakaopay.codingtest"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
    }
}