---
layout: post
title:  "Spring MVC(Simple)"
date:   2021-12-31T00:00:00-00:00
author: sangyeop
categories: Spring


---



### Spring MVC 작동 원리

------

![spring_mvc_structure](/Users/chaesang-yeob/Desktop/blog/saint6839.github.io/images/spring_mvc_structure.png)



#### Controller

- 사용자의 요청이 진입하는 지점, 어떤 처리를 할지 결정한 후 Service에 넘겨준다.

  

#### Service 

- Controller로부터 전달받은 요청에 따라 핵심 비즈니스 로직을 수행한 뒤, DB접근이 필요하면 Repository에 요청한다.

  

#### Repository

- JPA를 사용할 경우 데이터베이스와 직접 접근하는 DAO(Database Access Object)다.
- DB CRUD 작업처리
- DB 관리 (연결, 해제, 자원 관리)

JPA를 사용할 때 Entity 클래스와 Repository의 관계

: Repository 인터페이스를 JpaRepository<T,ID>를 확장하여 구현한다. 이때 T의 인자로 엔티티 클래스를 넘겨주면 된다.



> **Entity**  
>
> 2차원 테이블 자체를 Entity라고 함
>
> Table 내의 모든 Column은 Field로 반드시 정의해야 함
>
> Setter 사용을 지양하고, Builder 패턴을 사용하도록 한다.(@Builder)