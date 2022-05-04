---
layout: post
title:  "QNA 미션 코드 리뷰 정리 - @Transactional"
date:   2022-05-04T00:00:00-00:00
author: sangyeop
categories: Sproutt-2nd


---



# @Transactional

새싹 스터디에서 웹 미션을 진행하던 중 서비스 레이어에서 `@Transactional` 어노테이션을 이용하면 불필요한 코드를 줄일 수 있을 것 같다는 리뷰를 받았다. 기존에 나는 `@Transactional`을 실제 데이터베이스에 올리지 않고 롤백시키는 용도로만 이해하고 있었는데, 영속성과 관련해서 더 많은 기능들이 있음을 이번에 깨우치게 되었다.

### Transaction 이란?

먼저 트랜잭션이 무엇인지에 대해서 알아보았다. 데이터베이스 트랜잭션이란 데이터베이스 관리 시스템 또는 유사한 시스템에서 상호작용의 더 이상 쪼개질 수 없는 최소 연산 단위이다.

트랜잭션 특징은 **ACID**라고 한다

- **원자성(Atomic)**

  트랜잭션과 관련도니 작업들이 부분적으로 실행되다가 중단되지 않는 것을 보장하는 능력이다. 예를 들어 돈을 이체하고 꺼내는 과정에서, 한쪽만 성공하고 다른 한쪽이 실패해서는 안된다. 원자성은 이 처럼 중간 단계까지 실행되고 실패하는 일이 없도록 한다.

  즉 한 트랜잭션 내에서 실행한 작업들은 하나의 단위로 처리해야한다. 즉 모두 성공하거나 모두 실패해야 한다.

- **일관성(Consistencty)**

  트랜잭션이 실행을 성공하면 언제나 일관성있는 데이터베이스 상태를 유지한다.

- **독립성(Isolation)**

  트랜잭션 수행 시 다른 트랜잭션 연산 작업이 끼어들지 못하도록 보장하는 것을 의미한다. 즉 동시에 실행되는 트랜잭션들이 서로 영향을 미치지 못한다.

- **영속성(Durability)**

  트랜잭션을 성공적으로 마치면 결과가 항상 저장되어야 한다.

### @Transactional

​	다음은 어노테이션에 대해서 알아보았다. 스프링에서는 트랜잭션 처리를 지원하는데 `@Transactional` 을 선언해서 사용이 가능하며, 이를 **선언적 트랜잭션**이라고 부른다. 클래스나 메서드 위에 `@Transactional`이 추가되면 트랜잭션 기능이 적용된 프록시 객체가 생성된다.

이 프록시 객체는 `@Transactional`이 포함된 메서드가 호출될 경우, `PlatformTransactionManager`를 사용해서 트랜잭션을 시작하고, 정상 여부에 따라 Commit 또는 Rollback 한다.

```java
@Transactional
public void save() {
  ...
}
```

### 격리 수준에 따라 발생할 수 있는 문제

| 격리 수준        | Dirty Read | Non-Repeatable Read | Phantom Read |
| ---------------- | ---------- | ------------------- | ------------ |
| Read Uncommitted | O          | O                   | O            |
| Read Committed   | -          | O                   | O            |
| Repeatable Read  | -          | -                   | O            |
| Serializable     | -          | -                   | -            |

- **Dirty Read**

  아직 트랜잭션이 완료되지 않은 상황에서 데이터에 접근을 허용할 경우 생기는 데이터 불일치 문제이다.

  트랜잭션1이 수정중인 데이터를 트랜잭션2가 읽을 수 있다. 만약 트랜잭션1의 값이 정상 커밋되지 않고 롤백되면, 트랜잭션2가 롤백 전에 읽은 값은 잘못된 데이터가 된다.

- **Non-Repeatable Read**

  한 트랜잭션이 같은 쿼리를 두 번 실행했을때 발생할 수 있는 데이터 불일치 문제이다.

  트랜잭션1이 회원 A를 조회하던 중에 트랜잭션2가 회원 A의 정보를 수정하고 커밋하면 트랜잭션 1이 다시 회원 A를 조회했을 경우에는 수정된 데이터가 조회된다. 즉 반복해서 같은 데이터를 읽을 수 없는 경우에 해당한다.

- **Phantom Read**

  트랙잭션이1이 전자기기 카테고리를 조회했는데, 트랜잭션2가 컴퓨터를 추가하고 조회하면 트랜잭션1이 다시 전자기기 카테고리를 조회했을 때 상품 하나가 추가된 상태로 조회된다. 반복 조회시 결과 집합이 달라지는 경우에 해당한다.

격리 수준이 높아질 수록 데이터 무결성을 유지할 수 있다. 하지만 무조건 상위 격리 수준을 사용할 경우 **Locking**으로 인해 동시에 수행되는 많은 트랜잭션들이 순차 처리되면서 DB의 성능이 떨어진다. 그렇다고 **Locking**의 범위를 줄이게 되면 잘못된 값이 처리될 가능성도 생기므로 적절하게 잘 사용하는 것이 중요하다.




여기까지가 내가 스스로 찾아본 `@Transactional`과 트랜잭션에 대한 내용이다. 해당 내용들을 찾아보고 어느정도 개념에 대해서 감을 잡았으나, 여전히 내가 받은 코드 리뷰를 어떻게 적용해야 할 지에 대해서는 전혀 감이 잡히지 않았다. 내가 받은 코드 리뷰의 내용은 아래와 같다. 

> @Transactional 애노테이션만 잘 붙이면 불필요한 코드를 많이 줄일 수 있겠네요.
>
> Persistent Lifecycle 은 Transaction 의 시작과 끝과 바운딩 되어 있습니다.
>
> The lifecycle of a Session is bounded by the beginning and end of a logical transaction.
>
> 즉, Persistent Context 를 제대로 활용하기 위해선 @Transactional 을 같이 잘 사용해야 합니다.

 `The lifecycle of a Session is bounded by the beginning and end of a logical transaction.` 이 문장이 전혀 감이 잡히지 않았는데, 리뷰어와 함께 공부하는 스터디원의 부가 설명이 이해하는데 큰 도움이 되었다. 내가 설명을 이해한 바는 다음과 같다.

- `The lifecycle of a Session is bounded by the beginning and end of a logical transaction.` 

  위에서 설명했듯 스프링에서는 트랜잭션을 `@Transactional` 어노테이션을 통해 처리한다. 해당 어노테이션이 메서드 레벨에 붙은 경우, 메서드가 호출 될 경우 `PlatformTransactionManager 에 의해 `Hibernate Session이 열리고 

  이 안에서 영속성 객체가 JPA로부터 반환되어지면(Persistent Object,`findById()` 등으로 불러와진 객체를 영속성 객체라고 한다),  JPA의 객체 상태가 Persistent 상태가 되었음을 의미한다. 

  이 객체는 JPA로부터 불러와진 시점의 상태가 snapshot으로 찍히고, 이 객체의 값이 트랜잭션 내에서 변경된다면 Hibernate Session이 닫힌 이후(메서드 호출이 끝난 이후)에 JPA는 snapshot과 변경된 엔티티의 상태를 비교하고 update 쿼리를 날려서 변경을 감지하고 동기화를 할 수 있다.

```java
@DeleteMapping("/api/questions/{question-id}/answers/{answer-id}")
public Result<Answer> remove(@PathVariable("question-id") Long questionId, @PathVariable("answer-id") Long answerId, HttpSession session) {
        if (!HttpSessionUtils.isLoginUser(session)) {
            return Result.fail("로그인이 필요한 서비스 입니다.");
        }
        User loginUser = HttpSessionUtils.getUserFromSession(session);
        Answer answer = answerRepository.findById(answerId)
                                          .orElseThrow(NoSuchElementException::new);

        if (!answer.isSameWriter(loginUser)) {
            return Result.fail("다른 사용자의 답변을 삭제할 수 없습니다.");
        }
        answer.setDeletedFlag(true);
        answerRepository.save(answer);

        return Result.ok(answer);
    }
```

`remove()` 메서드는 답글을 삭제하는 컨트롤러의 메서드이다. 그런데 이 코드를 보면 반환시점 전에 제거 메서드 임에도 불구하고 데이터베이스를 동기화 시키기 위해 `save()`를 호출하는 모습을 볼 수 있다. 만약 이 `remove()`라는 메서드가 `@Transactional` 로 선언되어 호출되어졌다면, 여기서 `Answer` 객체는 **영속성 객체**가 되었을 것이고, `answer.setDeletedFlag(true);` 로 객체의 변경된 값이 메서드 호출이 끝난 시점에 JPA에 의해 값의 변경이 자동으로 감지되고 동기화되어, 별도로 `save()`라는 메서드를 이용해서 데이터베이스와 동기화를 해줄 필요가 없었을 것이다.

이상이 내가 이해한 내용의 전부이다. 아직 여러가지 옵션이나 특징들이 많은 것 같지만, 일단으 이 정도로만 정리를 해보았다. JPA를 그저 쿼리작성을 쉽게해주고 `@Transactional`을 롤백시켜주는 용도로만 쓰던 나에게는 신선한 충격이었으며, 새로운걸 나름 잘 이해한 것 같아 재밌는 경험이었다.





> 참고자료
>
> - https://goddaehee.tistory.com/167
> - https://ko.wikipedia.org/wiki/ACID
> - https://velog.io/@kdhyo/JavaTransactional-Annotation-%EC%95%8C%EA%B3%A0-%EC%93%B0%EC%9E%90-26her30h