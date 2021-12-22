---
layout: post
title:  "What is Rest Api?"
date:   2021-12-23T14:25:52-05:00
author: sangyeop
categories: Android
---



### 1. REST 구성

- 자원 - URI
- 행위 - HTTP 메소드
- 표현

### 2. REST API 디자인 가이드 (중요)

- 첫 번째, URI는 정보의 자원을 표현해야 한다.
- 두 번째, 자원에 대한 행위는 HTTP 메소드( GET, POST, PUT, DELETE )로 표현한다.

### 2-1. REST API 중심 규칙

- URI는 정보의 자원을 표현해야 한다. (리소스명은 동사보다 명사 사용)

1. 잘못된 예시

URI는 자원을 표현하는데 중점이 되어야 한다. delete와 같은 행위에 대한 내용이 URI 에 포함되어서는 안된다.

```json
GET /members/delete/1
```

2. 올바른 예시

회원정보를 가져올 때는 GET, 회원 등록 등의 행동을 표현하고자 할 때는 POST 를 사용한다.

```json
DELETE /members/1
```

### 2-2 URI 설계 시 주의 사항

- 슬래시 구분자(/) 는 계층 관계를 나타낼 때 사용
- URI 마지막 문자로 슬래시(/)를 포함하지 않는다.
- 불가피하게 긴 URI를 사용하게 될 경우 하이픈(-)을 사용하여 구분
- 밑줄(_)은 사용하지 않는다.
- URI 경로에는 소문자가 적합하다.
- 파일 확장자( ex) .jpg )는 URI에 포함하지 않는다.



현재 진행하고 있는 소개팅 어플리케이션의 메인화면에 뷰에 필요한 정보를 조회하여 반영하는 Rest Api 설계를 해보았다.

| 메소드                      | GET                                             |
| --------------------------- | ----------------------------------------------- |
| 기능                        | read                                            |
| /users                      | users 리스트 반환                               |
| /users/{userId}             | {userId}에 해당하는 사용자의 users 정보 반환    |
| /users/{userId}/datas       | {userId}에 해당하는 사용자의 데이터 리스트 조회 |
| /users/{userId}/age         | {userId}에 해당하는 사용자의 age 조회           |
| /users/{userId}/religion    | {userId}에 해당하는 사용자의 religion 조회      |
| /users/{userId}/location    | {userId}에 해당하는 사용자의 location 조회      |
| /users/{userId}/smoking     | {userId}에 해당하는 사용자의 smoking 여부 조회  |
| /users/{userId}/drinking    | {userId}에 해당하는 사용자의 drinking 여부 조회 |
| /users/{userId}/college     | {userId}에 해당하는 사용자의 college 조회       |
| /users/{userId}/height      | {userId}에 해당하는 사용자의 height 조회        |
| /users/{userId}/mbti        | {userId}에 해당하는 사용자의 mbti 조회          |
| /users/{userId}/personality | {userId}에 해당하는 사용자의 personality 조회   |