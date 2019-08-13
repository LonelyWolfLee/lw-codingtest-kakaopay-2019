# Kakaopay Coding Test 2019

## Problem Information

* Problem : 사전과제3
* Documents : [주택금융 API 개발 v1.1](/doc/api-doc-problem.pdf)
* Data File : [주택금융신용보증 금융기관별 공급현황](/doc/data.csv)


## Environment

* Framework : Spring Boot 2.1.7
* Language : Kotlin
* DB : H2 (connection url : http://localhost:8080/console)
* How To Run : 
```gradle
    gradlew bootRun
``` 

## API Document with Swagger2

DESCRIPTION : API 테스트를 자체적으로 조금 더 쉽게 하기 위해 Swagger 2 기반의 Swagger UI 를 적용 하였다.
URL : http://localhost:8080/swagger-ui.html


## Authorization API

#### JWT 기반 토큰을 발행하고 user_id, password, token 기반으로 관리한다. 토큰 만료 시간은 1시간이다.

###### signup

METHOD : POST
URL : /api/auth/signup



## Finance API

#### Mandatory

###### 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API

METHOD : POST  
URL : /api/finance/load-data


###### 주택금융 공급 금융기관(은행) 목록을 출력하는 API

METHOD : GET  
URL : /api/finance/institutes


###### 년도별 각 금융기관의 지원금액 합계를 출력하는 API

COMMENT : 출력 데이터 샘플 형식에 오류(json format 이 잘못됨)가 있어서 약간 변경하였음. json은 key-value 의 객체 형식이어야 함 (또는 단독 배열 형태). 객체 형태 안에 key-value 형식과 단독 배열 형식이 공존 할 수 없음  
METHOD : GET  
URL : /api/finance/by-year  


###### 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API

METHOD : GET  
URL : /api/finance/most-for-all-years  


###### 전체 년도(2005~2016)에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력하는 API

COMMENT : 데이터는에는 2005~2016년까지 있지만 아마 17년에 외환은행의 값이 0이어서 2016년으로 쓴 듯  
METHOD : GET  
URL : /api/finance/bnk8/most-n-least


#### Optional

###### 특정 은행의 특정 달에 대해서 2018 년도 해당 달에 금융지원 금액을 예측하는 API

METHOD : POST  
URL : /api/finance/expectation
