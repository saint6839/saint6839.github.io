---
layout: post
title:  "How to transfer data controller to view"
date:   2021-12-31T00:00:00-00:00
author: sangyeop
categories: Spring




---

###  



### Controller에서 View로 데이터를 전달하는 방법

------

#### 1. Model

model 객체를 파라미터로 받아서 넘긴다.

```java
 @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";		// model에 담은 데이터를 json화 한다고 생각하면 편하다
    }
```



