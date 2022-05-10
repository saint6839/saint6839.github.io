---
layout: post
title:  "BeanFactory과 ApplicationContext의 차이점"
date:   2022-05-10T00:00:00-00:00
author: sangyeop
categories: Spring



---

### Bean

스프링이 제어권을 가져서 직접 생성하고 의존관계를 부여하는 객체를 **빈**이라고 부른다.

### BeanFactory vs ApplicationContext

- `BeanFactory`

  - 빈을 생성하고 의존관계를 설정하는 기능을 담당하는 가장 기본적인 IoC 컨테이너이자 클래스를 말한다. 스프링 빈 컨테이너에 접근하기 위한 최상위 인터페이스이다.

  - **Lazy-loading** 방식을 사용한다

    빈을 사용할 때 빈을 로딩한다. ( 필요할 때만 로딩하기 때문에, 가벼운 경량 컨테이너이다. )

- `ApplicationContext`

  - BeanFactory를 구현하고 있어 BeanFactory의 확장된 버전이라고 생각해도 좋다.

  - **Eager-loading** 방식을 사용한다

    런타임 실행시 모든 빈을 미리 로딩시킨다.

> BeanFactory라고 말을 할때는 빈을 생성하고 관계를 설정하는 IoC의 기본 기능에 초점을 맞춘 것이다.
>
> ApplicationContext라고 말을 할때는 별도의 정보를 참고해서 빈의 생성, 관계 설정 등의 제어의 총괄에 초점을 맞춘 것이다.

스프링 공식문서에 의하면 특별한 경우가 아니라면 BeanFactory의 모든 기능을 포함하고 추가 기능을 제공하는 ApplicaionContext를 사용하기를 권장하고 있다고 한다.

