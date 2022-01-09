---
layout: post
title:  "HTTP Request Data - Outline "
date:   2021-01-09T00:00:00-00:00
author: sangyeop
categories: Spring



---

###  



### HTTP 요청 데이터

------

#### GET - 쿼리 파라미터

- /url**?username=hello&age=20**
- 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달한다
- ex) 검색, 필터, 페이징 등에서 많이 사용

#### POST - HTML Form

- Content-type:application/x-www-form-urlencoded
- **메시지 바디**에 쿼리 파라미터 형식으로 전달 username=hello&age=20
- ex) 회원 가입, 상품 주문, HTML Form 사용

#### HTTP message body 에 데이터를 직접 담아서 요청

- HTTP API(REST API)에서 주로 사용, JSON, XML, TEXT
- 데이터 형식은 주로 JSON 사용
- POST, PUT, PATCH



위 세 가지 경우에서 벗어나지 않는다.



