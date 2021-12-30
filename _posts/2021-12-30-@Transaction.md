---
layout: post
title:  "@Transaction"
date:   2021-12-30T00:00:00-00:00
author: sangyeop
categories: Spring



---

###  



###  @Transactional

------

#### 트랜잭션이란?

'거래'라는 뜻을 가지고 있다.

- 누군가와 거래를 하기 위해서 먼저 결제를 하였다.
- 그런데 사기를 당했다.

이런 상황이라면 다시 시간을 돌려서, 결제를 하기 전으로 돌아가고 싶을 것이다.



이 처럼 모든 작업들이 성공적으로 완료되면 결과를 적용하고, 오류가 발생했다면 이전에 있던 다른 작업들이 모두 성공적이었어도 다시 원래 상태로 되돌리는 것이 트랙잭션이다.



데이터베이스에 이를 적용하면, CRUD 작업이 이루어지던 중 오류가 발생하였을 때 **모든 작업들을 원래 상태로 되돌릴 수 있음을 의미한다.**



#### 스프링에서 사용법

```java
@Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

```

이처럼 클래스, 메소드 명 상단에 ` @Transaction`을 붙여서 선언을 한다.

`@Transaction`이 붙은 메소드는 메소드가 포함하고 있는 작업 중에 하나라도 실패할 경우 전체 작업을 취소하게 된다.



결론적으로 일련의 작업들을 하나로 묶어서 하나의 단위로 처리하고 싶다면, `@Transaction`을 사용하면 된다.
주의할 점은 프로덕션 환경과 테스트 환경에서 동작 방식이 약간 다르니 찾아보고 유의하여 사용하자

