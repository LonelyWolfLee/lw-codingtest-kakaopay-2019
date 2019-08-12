# Kakaopay Coding Test 2019

## Problem Information

* Problem : 사전과제3
* Documents : [주택금융 API 개발 v1.1](/doc/api-doc-problem.pdf)
* Data File : [주택금융신용보증 금융기관별 공급현황](/doc/data.csv)


## Environment

* Framework : Spring Boot 2.1.7
* Language : Kotlin


## For API Test in UI

http://localhost:8080/swagger-ui.html

## API

#### Mandatory

###### 1. 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API 개발

METHOD : POST  
URL : /api/mandatory/load-data


###### 2. 주택금융 공급 금융기관(은행) 목록을 출력하는 API 를 개발

METHOD : GET  
URL : /api/mandatory/institutes


