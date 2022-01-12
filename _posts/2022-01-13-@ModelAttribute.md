---
layout: post
title:  "@ModelAttribute"
date:   2022-01-13T00:00:00-00:00
author: sangyeop
categories: Spring


---

### **HTTP 요청 파라미터 - @ModelAttribute**

------

보통 요청 파라미터를 통해서 값을 받고, 그 값을 객체에 넣어주는 방식으로 데이터를 저장한다.

```java
@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttributeV1(@RequestParam String username, @RequestParam int age) {
  
  	HelloData helloData = new HelloData();
  	helloData.setUsername(username);
  	helloData.setAge(age);
  	
    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
    log.info("helloData={}", helloData);
    return "ok";
}
```

이 일련의 과정을 자동화 해주는 것이 `@ModelAttribute`기능이다. 이 위와 아래는 같은 코드이다.

```java
@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttributeV1(@ModelAttribute HelloData helloData) {
    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
    log.info("helloData={}", helloData);
    return "ok";
}
```

스프링은 `@ModelAttribute`가 있으면 다음 순서를 실행한다.

- `HelloData`객체를 생성한다.
- 요청 파라미터의 이름으로 `HelloData` 객체의 프로퍼티를 찾는다. 그리고 해당 프로퍼티의 `setter`를 호출해서 파라미터의 값을 입력(바인딩) 한다.
- ex) 파라미터의 이름이 `username` 이면 `setUsername()`메소드를 찾아 호출하면서 값을 입력한다.

**프로퍼티**

객체에 `getUsername()`, `setUsername()`이라는 메소드가 있으면, 이 객체는`username`이라는 프로퍼티를 가지고 있다.

그래서 만약 `/model-attribute-v1?username=hello&age=20` 이라는 입력이 들어온다면 @ModelAttribute에 해당하는 객체의 프로퍼티를 찾아서 값을 입력(바인딩)한다.
그러나 만약 `/model-attribute-v1?username=hello&age=abc`와 같이 int 타입 자리에 맞지 않는 타입의 값이 입력되면 `BindException`이 발생한다.





추가적으로 `@ModelAttribute`어노테이션은 생략이 가능하다. 그런데 `@RequestParam`어노테이션도 생략이 가능하다.

```java
@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttributeV1(@ModelAttribute HelloData helloData) {
    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
    log.info("helloData={}", helloData);
    return "ok";
}
```

그렇다면 스프링은 어떤 것을 기준으로 두 어노테이션을 구분할까?

스프링은 해당 생략시 다음과 같은 규칙을 적용한다

- `String`, `int`,`Integer`같은 단순 타입 = `@RequestParam`
- 나머지는 `@ModelAttribute` (argument resolver로 지정해둔 타입 외)



