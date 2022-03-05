---
layout: post
title:  "모던 자바 인 액션 스터디 - chapter3-(2)"
date:   2022-02-27T00:00:00-00:00
author: sangyeop
categories: Sproutt-2nd


---

# 새싹 개발 서적 스터디 - 모던 자바 인 액션 Chapter3 - (1)

분량이 많은 관계로 (1),(2) 두 차례로 나누어 스터디를 진행했다. 이번주는 114page 이후를 정리하였다.


이 장의 내용

- 람다란 무엇인가?

- 어디에, 어떻게 람다를 사용하는가?

- 실행 어라운드 패턴

- 함수형 인터페이스, 형식 추론

- 메서드 참조

- 람다 만들기

  

### 메서드 참조

메서드 참조를 활용하면 기존 메서드를 재활용해서 **람다처럼** 전달할 수 있다.

```java
inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()); // 기존 코드
inventory.sort(comparing(Apple::getWeight)); //메서드 참조               
```

#### 요약

메서드 참조는 특정 메서드만을 호출하는 람다의 축약형이다. 예를 들어 람다가 '이 메서드 직접 호출해' 라고 명령한다면 메서드를 어떻게 호출해야하는지에 대한 설명을 참조하는 것보다, 메서드 명을 참조하는 것이 편리하다.



#### 메서드 참조를 만드는 방법

1. **정적 메서드 참조**
   - ex) `Integer.parseInt()` 는 `Integer::parseInt` 로 표현
2. **다양한 형식의 인스턴스 메서드 참조**
   - Ex String의 `length()` 메서드는 `String::length` 로 표현
3. **기존 객체의 인스턴스 메서드 참조**
   - `Transaction expensiveTransaction = new Transaction()` 에서 , `Transaction` 객체에는 `getValue()` 메서드가 있다면
     `expensiveTransaction::getValue`로 표현

예를 들어 List의 `sort()`메서드는 인수로 `Comparator`를 기대한다. `Comparator`는 `(T, T) -> int`라는 함수 디스크립터를 갖는다.

- 메서드 참조 사용 전

```java
List<String> str = Arrays.asList("a","b","A","B");
str.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
```

- 메서드 참조 사용 후

```java
List<String> str = Arrays.asList("a","b","A","B");
str.sort(String::compareToIgnoreCase);
```

#### 생성자 참조

`ClassName::new` 처럼 클래스명과 new 키워드를 사용해서 기존 생성자의 참조를 만들 수 있다.

```java
Supplier<Apple> c1 = Apple::new; 
Apple a1 = c1.get(); // Supplier의 get메서드를 호출해서 새로운 Apple 객체 생성 가능
```

위 코드는 다음과 같다.

```java
Supplier<Apple> c1 = () -> new Apple(); // 람다표현식으로 디폴트 생성자를 가진 Apple 객체 생성
Apple a1 = c1.get(); // Supplier의 get메서드를 호출해서 새로운 Apple 객체 생성 가능
```

`Apple(Integer weight)` 라는 시그니처를 갖는 생성자는 `Function`인터페이스의 시그니처와 같다. 따라서

```java
Function<Integer, Apple> c2 = Apple::new;
Apple a2 = c2.apply(110);
```

이 코드는 다음과 같다

```java
Function<Integer, Apple> c2 = (weight) -> new Apple(weight);
Apple a2 = c2.apply(110);
```

다음 예제를 통해 마지막으로 복습해보자 `Color(int, int, int)`와 같이 인수가 세 개인 생성자의 생성자 참조를 사용하려면 어떻게 해야 할까? 기존에 인수 두 개까지는 `BiFunction`이라는 함수형 인터페이스가 지원 되었으나, 세 개부터는 지원되는 인터페이스가 없으므로 생성자 참조와 일치하는 시그니처를 갖는 함수형 인터페이스를 생성해야한다.

```java
public interface TriFunction<T, U, V, R> {
  R apply(T t, U u, V v);
}
```

이는 다음과 같이 생성자 참조로 사용할 수 있다.

```java
TriFunction<Integer, Integer, Integer, Color> colorFactory = Color::new;
Color color = colorFactory.apply(1,2,3);
```



+ 메서드 참조 / 생성자 참조 스트림에 적용해보기

  + **메서드 참조**

  ```java
  private List<String> toStrings(String str) {
    return Arrays.stream(str.split(",")) // 현재 Stream에는 ","를 기준으로 파싱된 String 배열이 들어가 있음
      .map(String::trim) // 
      .collect(Collectors.toList());
  }
  ```

  - **생성자 참조**

  ```java
  public class Car {
    String name;
    
    public Car(String name) {
      this.name = name;
    }
  }
  ```

  ```java
  public List<Car> of(String names) {
    String[] carNames = names.split(",");
    
    return Arrays.stream(carNames) // String 타입을 가지고 있는 stream
      .map(String::trim) // String에서 공백 제거
      .map(Car::new) // name -> new Car(name) 에 name을 넣어서 Car 객체 생성
      .collect(Collectors.toList()); // List화 시켜줌
  }
  ```

  

#### 람다, 메서드 참조 활용하기

- **1단계 : 코드 전달**

`void sort(Comparator<? super E)` 이 코드는 `Comparator`객체를 인수로 받아 두 사과를 비교한다. 객체 안에 동작을 포함하는 방식으로 다양한 전략을 전달할 수 있다.

이제 sort의 **동작**은 **파라미터화**되었다고 할 수 있다. 이는 sort에 전달된 정렬 전략에 따라 sort의 동작이 달라짐을 의미한다.

```java
public class AppleComparator implements Comparator<Apple> {
  public int compare(Apple a1, Apple a2) {
    return a1.getWeight().compareTo(a2.getWeight());
  }
}
```

```java
inventory.sort(new AppleComparator());
```

- **2단계 : 익명 클래스 사용**

```java
inventory.sort(new Comparator<Apple>() {
  public int compare(Apple a1, Apple a2) {
    return a1.getWeight().compareTo(a2.getWeight());
  }
});
```

- **3단계 : 람다 표현식 사용**

```java
inventory.sort((a1, a2) -> a1.getWeight().compareTo(a2.getWeight()));
```

- **4단계 : 메서드 참조 사용**

```java
import java.util.Comparator.comparing;

inventory.sort(comparing(Apple::getWeight));
```

Apple을 weight별로 비교해서 inventory를 sort하라는 의미를 한 눈에 파악할 수 있다.



### 람다 표현식을 조합할 수 있는 유용한 메서드

**디폴트 메서드**는 **함수형 인터페이스**에 추가로 제공할 수 있는 메서드 같은 것이다. 함수형 인터페이스는 하나의 추상 메서드만 정의하는 인터페이스라고 했는데, 어떻게 또 다른 메서드가 정의 될 수 있을까? 이는 디폴트 메서드는 추상 메서드가 아니기 함수형 인터페이스의 정의에서 벗어나지 않는다.

#### Comparator 조합

정적 메서드 `Comparator.comparing`을 이용해서 비교에 사용할 키를 추출하는 `Function` 기반의 `Comparator`를 반환할 수 있다.

```java
Comparator<Apple> c = Comparator.comparing(Apple::getWeight);
```

- **역정렬**

  오름차순으로 기본으로 정렬된 사과의 무게를 내림차순으로 정렬하고 싶으면 어떻게 할까? `Comparator`는 `reversed()`라는 **디폴트 메서드**를 제공한다.

```java
inventory.sort(comparing(Apple::getWeight).reversed());
```

#### Compartor 연결

그런데 만약 무게로 정렬을 했는데, 원산지는 다르지만 무게가 서로 같다면 어떻게 정렬해야 할까? 이럴 경우를 위해서 `thenComparing()` 메서드로 두 번째 비교자를 만들 수 있다.

```java
inventory.sort(comparing(Apple::getWeight))
  .reversed()
  .thenComparing(Apple::getCountry));
```



### Predicate 조합

- `negate()`

  - 특정 프레디케이트를 반전(~가 아닌)

    ```java
    Predicate<Apple> notRedApple = redApple.negate(); // 기존  redApple의 객체 반전
    ```

- `and()`

  - 두 가지 경우 모두를 충족하는(~이면서 ~인)

    ```java
    // 두 프레디케이트를 연결해서 새로운 프레디케이트 객체를 만듦
    Predicate<Apple> redAndHeavyApple = redApple.and(apple -> apple.getWeight() > 150); 
    ```

- `or()`

  - 두 가지 경우 중 하나를 충족하는(~또는 ~인)

    ```java
    // 프레케이트 메서드를 연결해서 더 복잡한 프레디케이트 객체를 만든다.
    Predicate<Apple> redAndHeavyAppleOrGreen = 
      redApple.and(apple -> apple.getWeight() > 150)
      .or(apple -> GREEN.equals(a.getColor()));
      
    ```

### Function 조합

- `andThen()`

  주어진 함수를 먼저 적용한 결과를 다른 함수의 입력으로 전달하는 함수를 반환한다.

  ```java
  Function<Integer, Integer> f = x -> x + 1;
  Function<Integer, Integer> g = x -> x * 2;
  Function<Integer, Integer> h = f.andThen(g); // f를 먼저 계산하고 g를 계산한다. 수학적으로는 g(f(x)) 가 됨
  int result = h.apply(1); // 4
  ```

- compose()

  인수로 주어진 함수를 먼저 실행한 다음에 그 결과를 외부 함수의 인수로 제공한다.

  ```java
  Function<Integer, Integer> f = x -> x + 1;
  Function<Integer, Integer> g = x -> x * 2;
  Function<Integer, Integer> h = f.compose(g); // g를 먼저 계산하고 f를 계산한다. 수학적으로는 f(g(x)) 가 됨
  int result = h.apply(1); // 3
  ```

  













