---
layout: post
title:  "Naming of Static Factory Method"
date:   2021-08-25T14:25:52-05:00
author: sangyeop
categories: Java
---



# 정적 팩토리 메소드의 명명 방식

- #### from

  매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 메소드

  ```java
  Date d = Date.from(instant);
  ```

- #### of

  여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메소드

  ```java
  Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);
  ```

- #### valueOf

  from과 of의 더 자세한 버전

  ```java
  BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);
  ```

- #### instance 혹은 getInstance

  (매개변수를 받는다면) 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장하지는 않는다.

  ```java
  StackWalker luke = StackWalker.getInstance(options);
  ```

- #### create 혹은 newInstatnce

  instance 혹은 geInstance와 같지만, 매번 새로운 인스턴스를 생성해 반환함을 보장한다.

  ```java
  Object newArray = Array.newInstance(classObject, arrayLen);
  ```

- #### getType

  getInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩토리 메소드를 정의할 때 쓴다. "Type"은 팩토리 메소드가 반환할 객체의 타입이다.

  ```java
  FileStore fs = Files.getFilesStore(path);
  ```

- #### newType

  newInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩토리 메소드를 정의할 때 쓴다. "Type"은 팩토리 메소드가 반환할 객체의 타입이다.

  ```java
  BufferedReader br = Files.newBufferedReader(path);
  ```

- #### type

  getType과 newType의 간결한 버전

  ```java
  List<Complaint> litany = Collections.list(legacyLitany);
  ```

  

> *정적 팩토리 메소드와 public 생성자는 각자의 쓰임새가 있으니 상대적인 장단점을 이해하고 사용하는 것이 좋다. 그러나 정적 팩토리를 사용하는 경우가 더 유리한 경우가 많으므로 무작정 public 생성자를 제공하는 습관은 고치는 것이 좋다.*



#### 참고자료

------

- Effective Java

