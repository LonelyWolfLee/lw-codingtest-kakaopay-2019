package com.kakaopay.codingtest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
class KakaopayCodingTest2019Application

fun main(args: Array<String>) {
    runApplication<KakaopayCodingTest2019Application>(*args)
}

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(ApiInfoBuilder()
                        .version("1.0.0")
                        .title("Kakaopay Cofing Test API")
                        .description("Service API")
                        .build()
                )
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kakaopay.codingtest"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
    }
}