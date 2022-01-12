---
layout: post
title:  "HTTP API Simple Example"
date:   2022-01-12T00:00:00-00:00
author: sangyeop
categories: TIL


---





### HTTP API 예시

------

회원 목록 조회:GET  `/users`

회원 등록:POST `/users`

회원 조회:GET `/users/{userId}`

회원 수정:PATCH `/users/{userId}`

회원 삭제:DELETE `/users/{userId}`



```java
@RestController
public class MappingClassController {

    @GetMapping("/mapping/users")
    public String user() {
        return "get users";
    }

    @PostMapping("/mapping/users")
    public String addUser() {
        return "post user";
    }

    @GetMapping("/mapping/users/{userId}")
    public String findUser(@PathVariable String userId) {
        return "get userId=" + userId;
    }

    @PatchMapping("/mapping/users/{userId}")
    public String updateUser(@PathVariable String userId) {
        return "update userId=" + userId;
    }

    @DeleteMapping("/mapping/users/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return "delete userId=" + userId;
    }
}

```



