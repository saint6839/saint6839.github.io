---
layout: post
title:  "도메인에서 Setter 사용을 지양하는 이유"
date:   2022-02-10T00:00:00-00:00
author: sangyeop
categories: Spring



---

###  



### 도메인에서 Setter 사용을 지양하는 이유

------

내가 이해한 방식으로 설명하자면 이렇다

- 무분별하게 setter 메서드를 호출해 해당 객체의 값을 바꿔주게 되면, 소스코드가 커질수록 값 변경 부분을 파악하는데 어려움이 생긴다
- 또한 각 상황별로 값을 변경하는 이유가 제 각각이고, 변경하는 값 또한 다를텐데 이를 하나의 setter로 정의하기에는 그 의미를 파악하기가 어렵다.



하나의 예시를 들어보면 이해에 도움이 된다.

다음은 setter를 사용해서 직접 호출부분에서 setter를 통해서 값을 바꿔주는 모습이다. `setStatus()` 메서드를 통해서 `false`로 값을 바꿔주는 것 만으로는 해당 값 변경이 어떤 목적으로 이루어지는지 파악하기가 어렵다.

```java
public class Order {
    public void setStatus(boolean status) {
        this.status = status;
    }
}

public void 주문서비스의_취소이벤트() {
    order.setStatus(false);
}
```

그러나 다음과 같이 별도의 메서드를 선언하여 setter대신 값을 바꿔주게 된다면, 메서드명에 그 의도가 분명하게 드러나고 도메인 안에서 값 변경이 이루어지기 때문에 변경 부분을 파악하는데도 훨씬 용이해진다.

```java

public class Order {
    public void cancelOrder() {
        this.status = false;
    }
}

public void 주문서비스의_취소이벤트() {
    order.cancelOrder();
}
```

