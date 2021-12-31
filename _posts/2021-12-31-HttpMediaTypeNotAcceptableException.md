---
layout: post
title:  "HttpMediaTypeNotAcceptableException(@Getter)"
date:   2021-12-31T00:00:00-00:00
author: sangyeop
categories: Spring



---

###  



### HttpMediaTypeNotAcceptableException 해결 방법

------

**스프링부트와 AWS로 혼자 구현하는 웹 서비스** 교재 예제를 따라 학습 중 h2 database에 값을 insert하고 난 뒤 

`localhost:8080/api/v1/posts/1` 에서 값을 값을 조회할 때 상단의 에러가 발생함을 확인하였다. 

```
HttpMediaTypeNotAcceptableException: could not find acceptable representation
```

에러의 전문은 위와 같은데, 위 문장을 통해서 해당 요청이 이루어졌을때 반환값이 정상적으로 찾아지고 있지 않음을 알 수 있었다.



```java
@RequiredArgsConstructor
@RestController
public class PostApiController {
  ...

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }
}
```

조회 하는 부분이 findById 이기 때문에 컨트롤러에서 해당 부분을 먼저 찾아보았다. 이후

```java
@RequiredArgsConstructor
@Service
public class PostsService {
  ...

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }
}

```

서비스 클래스에서 해당 비즈니스 로직을 확인해보니 PostsResponseDto 객체를 반환함을 확인했고 Dto 객체를 찾아 따라가보니

```java
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}

```

다음과 같이 Lombok 라이브러리의 ` @Getter` 메서드가 누락 되어 있음을 확인하여 다음과 같이 수정해줌으로써 해결 할 수 있었다.

```java
@Getter
public class PostsResponseDto {
	...
}
```

반환할때 기본적으로 Json 타입으로 반환을하기 때문에, 이를 정상적으로 반환하기 위해서는 Getter메소드가 필요하다는 사실을 이번 기회를 통해서 배울 수 있었다.