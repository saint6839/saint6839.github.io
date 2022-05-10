---
layout: post
title:  "HttpSession에서 세션이 만들어지는 시점"
date:   2022-05-10T00:00:00-00:00
author: sangyeop
categories: Spring

---

### 문제

#### HttpSession 테스트

- UserController.class

~~~java
@PostMapping("/login")
public String login(String userId, String password, HttpSession httpSession) {
    try {
        User user = userService.login(userId, password);
        httpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
        log.debug("컨트롤러 세션 값 =" + httpSession.getAttribute(HttpSessionUtils.USER_SESSION_KEY));
    } catch (UnAuthenticationException unAuthenticationException) {
        return "user/login_failed";
    }
    return "redirect:/users";
}
~~~



```java
@PostMapping("/login")
public String login(String userId, String password, HttpSession httpSession) {
    try {
        User user = userService.login(userId, password);
        httpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
        log.debug("컨트롤러 세션 값 =" + httpSession.getAttribute(HttpSessionUtils.USER_SESSION_KEY));
    } catch (UnAuthenticationException unAuthenticationException) {
        return "user/login_failed";
    }
    return "redirect:/users";
}
```

- LoginAcceptanceTest.class

```java
@Test
public void login_failed() throws Exception {
    // given
    HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodeForm();
    htmlFormDataBuilder.addParameter("userId", "wrongId");
    htmlFormDataBuilder.addParameter("password", password);
    HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder.build();

    // when
    ResponseEntity<String> response = template().postForEntity("/users/login", request, String.class);
    log.debug("response의 헤더 = {}", response.getHeaders());

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getHeaders()
                       .get("Set-Cookie")).isNull();
}
```

로그인에 실패했을 경우에는 세션에 값을 저장하지 않기 때문에, response의 헤더 쿠키가 Null이 나올 것을 기대하고 테스트를 작성하였다. 

- `UserController.class`에서 `User user = userService.login(userId, password);`에서 로그인이 실패하기 때문에 예외가 던져져 `httpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);`이 호출되지 않음을 확인 함
- 그럼에도 불구하고 `LoginAcceptanceTest.class`의 로그에서는 헤더 값에 `JSESSIONID`가 담겨 있음을 확인했다.



### 분석

이와 관련해서 JSESSIONID와 HttpSession간에 관계를 알아보았다. 

문제 원인은 Session의 생성 시점과 관련이 있었다. `HttpSession`을 사용할 수 있는 방법은 크게 세 가지가 있다.

- `@Autowired`를 이용해 객체를 주입하여 사용하는 방법

  ```java
  @Autowired
  private HttpSession httpSession;
  
  @PostMapping("/login")
  public String login(...) {
    ...
    httpSession.setAttribute("test", "test");
    ...
  }
  ```

- 메서드의 파라미터로 주입하여 사용하는 방법

  ```java
  @PostMapping("/login")
  public String login(..., HttpSession httpSession) {
    ...
    httpSession.setAttribute("test","test");
    ...
  }
  ```

- `@SessionAttribute`, `@ModelAttribute`로 주입하여 사용하는 방법

  이 방식은 세션에 이미 저장된 데이터를 조회할 때 적합하므로 로그인을 처리하는 로직에는 적절하지 않다.

  ```java
  @Controller
  @SessionAttributes("test") // 세션 단위로 스코프를 지정하도록 명시함
  public class TestController {
    
    @PostMapping("/login")
    // @ModelAttribute로 "test"라는 키를 가진 값을 조회한 뒤, Test로 변환해 test 파라미터로 주입한다.
    public String login(Model model, @ModelAttribute("test") Test test) {
      ...
     	model.addAttribute("test", test.getName());
     	...
    }
  }
  ```

다시 테스트하고자 했던 부분을 정리해보면 이렇다.

> 로그인에 실패했을 경우에는 세션에 값을 저장하지 않기 때문에, response의 헤더 쿠키가 Null이 나올 것을 기대하고 테스트를 작성하였다. 

위 세가지 방법중에서 내가 사용한 **메서드의 파라미터로 주입하여 사용하는 방법**은 `setAttribute()` 메서드가 호출 되었을 때 세션값을 생성하는것이 아닌, `HttpSession`이 파라미터로 포함되어있는 `login()` 메서드가 호출될 때 `JSESSIONID`를 만들어 쿠키에 저장한다. 이 때문에 로그인 로직에서 로그인에 실패하여 `setAttribute()`가 호출되지 않았음에도 세션이 계속해서 만들어졌던 것이었다.

### 해결

세 가지 방법중에서 실제로 `setAttribute()`가 호출되는 시점에 세션 값을 저장하는 방법이 있는데, 바로 **`@Autowired`를 이용해 객체를 주입하여 사용하는 방법**이다. 이 방법은 컨트롤러의 메서드가 호출되는 시점이 아닌, 실제 `setAttribute()`가 호출되는 시점에 세션을 생성하기 때문에, 내가 테스트하고자 하는 목적에 부합하는 방법이었다.

이 방법을 사용해서 로그인이 성공 했을때만 세션이 만들어지도록 구현할 수 있었다.

```java
@Autowired
private HttpSession httpSession;

PostMapping("/login")
public String login(String userId, String password) {
    try {
        User user = userService.login(userId, password);
        httpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
        log.debug("컨트롤러 세션 값 =" + httpSession.getAttribute(HttpSessionUtils.USER_SESSION_KEY));
    } catch (UnAuthenticationException unAuthenticationException) {
        return "user/login_failed";
    }
    return "redirect:/users";
}
```

