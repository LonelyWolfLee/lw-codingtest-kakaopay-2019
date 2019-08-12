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

###### 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API

METHOD : POST  
URL : /api/mandatory/load-data


###### 주택금융 공급 금융기관(은행) 목록을 출력하는 API

METHOD : GET  
URL : /api/mandatory/institutes


###### 년도별 각 금융기관의 지원금액 합계를 출력하는 API (출력 데이터 샘플 형식에 오류(json format 이 잘못됨)가 있어서 약간 변경)

METHOD : GET  
URL : /api/mandatory/finance/by-year

