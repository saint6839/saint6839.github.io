---
layout: post
title:  "모던 자바 인 액션 스터디 - chapter5-(2)"
date:   2022-03-25T00:00:00-00:00
author: sangyeop
categories: Sproutt-2nd


---



# 새싹 개발 서적 스터디 - 모던 자바 인 액션 Chapter5-(2)







### 숫자형 스트림

이전에 `reduce()` 메서드로 스트림 요소의 합을 구하는 예제를 살펴봤다

```java
int calories = menu.stream()
  .map(Dish:Calories)
  .reduce(0, Integer::sum); // Integer를 박싱
```

#### 기본형 특화 스트림

위 코드에는 Integer를 박싱하는 비용이 포함되어 있는데, **기본형 특화 스트림**을 이용하면 다음과 같이 바꿔줄 수 있다.

```java
int calories = menu.stream()  // Stream<Dish> 반환
  .mapToInt(Dish::getCalories) // IntStream 반환
  .sum();
```

`mapToInt()` 메서드의 경우에는 IntStream을 반환하기 때문에, IntSteam 인터페이스에서 제공하는 `sum()` 메서드를 사용할 수 있다.

#### 객체 스트림으로 복원하기

그렇다면 숫자형 스트림을 다시 특화되지 않은 스트림으로 복원하려면 어떻게 해야할까? `boxed()`메서드를 이용해서 특화 스트림을 일반 스트림으로 변경할 수 있다.

```java
IntStream intStream = menu.stream().mapToInt(Dish::getCalories); // 스트림을 숫자 스트림으로 변환
Stream<Integer> stream = intStream.boxed(); // 숫자 스트림을 일반 스트림으로 변환
```

#### 기본값 : OptionalInt

이전에 값이 존재하는지 여부를 가리킬 수 있는 컨테이너 클래스  `Optional`이 등장했었다. `OptionalInt`를 이용해서 최대 값이 없을 경우에 사용할 기본값을 명시적으로 정의할 수 있다

```java
int max = maxCalories.orElse(1);
```

#### 숫자 범위

```java
IntStream evenNumbers = IntStream.rangeClosed(1, 100) // [1, 100] 의 범위 지정
  .filter(n -> n % 2 == 0); // 짝수만 필터링
System.out.println(evenNumbers.count()); // 50개의 짝수가 있다
```

#### 숫자 스트림 활용 : 피타고라스 수

- **피타고라스 수**

  `a * a + b * b = c * c` 를 만족하는 세 개의 정수 (a, b, c)가 존재

- **세 수 표현하기**

  ```java
  int numbers = new int[]{3, 4, 5};
  ```

- **좋은 필터링 조합**

  `a * a + b * b`의 제곱근이 정수인지 확인할 수 있는 방법

  ```java
  Math.sqrt(a*a + b*b) % 1 == 0;
  ```

  a라는 값이 주어지고 b는 스트림으로 제공된다고 가정 했을 때, 이를 `filter()` 메서드에 활용하면 다음과 같다

  ```java
  filter(b -> Math.sqrt(a*a + b*b) % 1 == 0)
  ```

- **집합 생성**

  필터를 이용해 좋은 조합을 갖는 a, b를 선택할 수 있게 되었다. 마지막 세 번째 수를 찾아야 하는데, `map()`을 이용해서 각 요소를 피타고라스 수로 변환이 가능하다.

  ```java
  stream.filter(b -> Math.sqrt(a*a + b*b) % 1 == 0)
    .map(b -> new int[]{a, b, (int) Math.sqrt(a*a + b*b)});
  ```

- **b값 생성**

  다음처럼 1부터 100까지 b를 생성한다.

  ```java
  IntStream.rangeClosed(1, 100) // IntStream
    .filter(b -> Math.sqrt(a*a + b*b) % 1 == 0) 
    .boxed() // Stream<Integer>로 boxing
    .map(b -> new int[]{a, b, (int) Math.sqrt(a*a + b*b)});
  ```

  이 코드를 `mapToObj`를 이용해서 `boxed()`와 `map()` 과정을 하나로 합쳐줄 수 있다.

  ```java
  IntStream.rangeClosed(1, 100)
    .filter(b -> Math.sqrt(a*a + b*b) % 1 == 0)
    .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a*a + b*b)});
  ```

- **a값 생성**

  ```java
  Stream<int[]> pythagoreanTriples = IntStream.rangeClosed(1, 100)
    .boxed()
    .flatMap(a -> IntStream.rangeClosed(a, 100)
             .filter(b ->Math.sqrt(a*a + b*b) % 1 == 0)
             .mapToObj(b -> new int[]{a, b, (int)Math.sqrt(a*a + b*b)})
             );
  ```

  `flatMap()`의 역할 : 

  - a에서 사용할 1부터 100까지의 숫자를 만들었다.
  - 주어진 a를 세 수의 스트림으로 만든다

  - a를 매핑해서 만들어진 스트림의 스트림을 하나의 평준화된 스트림으로 만들어준다.

  > *`flatMap()` 과 `map()`의 차이 :* `flatMap()`은 스트림의 스트림을 반환하는 한편 `map()`은 스트림의 단일 요소를 반환한다. 그래서 스트림의 내부 요소가 또 다른 배열일 경우 `flatMap()`을 활용하면 유용하게 사용할 수 있다.

  #### 개선할 점

  현재 코드에서는 제곱근을 두 번 계산한다. 그래서 `(a*a, b*b, a*a + b*b)` 형식을 만족하는 세 수를 만든 다음 결과만 필터링 하는 것이 더 효율적이다.

  ```java
  Stream<double[]> pythagoreanTriples2 = IntStream.rangeClosed(1, 100)
    .boxed()
    .flatMap(a -> IntStream.rangeClosed(a, 100))
    .mapToObj(b -> new double[]{a, b, Math.sqrt(a*a + b*b)}) // 만들어진 세 수
    .filter(t -> t[2] % 1 == 0)); // 세 수의 세 번째 요소는 반드시 정수
  ```



### 스트림 만들기

#### 값으로 스트림 만들기

```java
Stream<String> stream = Stream.of("Modern", "Java", "In ", "Action");
stream.map(String::toUpperCase).forEach(System.out.println);
```

그리고 `empty()` 메서드를 이용해서 빈 스트림을 만들어 줄 수도 있다.

```java
Stream<String> emptyStream = Stream.empty();
```

#### null이 될 수 있는 객체로 스트림 만들기

`System.getProperty()`는 제공된 키와 일치하는 속성이 없으면 null을 반환한다. 이런 메서드를 스트림에 활용하려면 다음처럼 null을 명시적으로 확인해야 한다.

```java
String homeValue = System.getProperty("home");
Stream<String> homeValueStream = homeValue == null ? Stream.empty() : Stream.of(value);
```

 이를  `Stream.ofNullable()`을 이용하면 다음과 같이 바꿔줄 수 있다

```java
Stream<String> values = Stream.of("config", "home", "user")
  .flatMap(key -> Stream.ofNullable(System.getProperty("key")));
```

#### 배열로 스트림 만들기

배열을 인수로 받는  `Arrays.stream()`을 이용해서 스트림을 만들 수 있다

```java
int[] numbers = {2, 3, 5, 7, 11, 13};
int sum = Arrays.stream(numbers).sum(); // IntStream의 sum() 사용 -> 합계 = 41
```

#### 파일로 스트림 만들기

`Files.lines()`는 주어진 파일의 행 스트림을 문자열로 반환한다. 다음은 고유한 단어의 수를 찾는 프로그램이다

```java
long uniqueWords = 0;
try(Stream<String> lines) = Files.lines(Paths.get("data.txt"), Charset.defaultCharset())) {
  uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" "))) // 고유 단어 수 계산
    .distinct() // 중복 제거
    .count(); // 단어 스트림 생성 
}
catch(IOException e) {
  // 여는중 에러 발생시 실행
}
```

각 행의 단어를 여러 스트림으로 만드는 것이 아니라, `flatMap()`을 이용하여 하나의 스트림으로 평면화 했다.

> *Stream은 자원을 자동으로 해제할 수 있는 AutoCloseable 이므로 try-finally가 필요 없다.*

#### 함수로 무한 스트림 만들기

`Stream.iterate()`와 `Stream.generate()`를 이용해서 **무한 스트림**(크기가 고정되지 않은 스트림)을 만들 수 있다. 보통 `limit()`와 함께 사용한다.

- **iterate 메서드** 

```java
Stream.iterate(0, n -> n + 2) // 이전 결과에 2를 더한 값을 반환, 즉 여기서는 짝수만 반환
  .limit(10)
  .forEach(System.out::println);
```

일반적으로 연속된 일련의 값을 만들 때 `iterate()`를 사용한다.

두 번째 인수로 `Predicate`를 받아서 언제까지 작업을 수행할지 결정할 수도 있다

```java
IntStream.iterate(0, n -> n < 100, n -> n + 4)
  .forEach(System.out::println);
```

이를  `filter()`를 이용해서 구현할 수 있겠다고 생각할수도 있지만, 이 경우에는 같은 결과를 얻을 수 없다. `filter()` 메서드는 언제 이 작업을 중단해야하는지 알지 못하기 때문이다.

두 번째 인자를 사용하지 않고 중단 시점을 선언하기 위해서는 `takeWhile()`이 해답이 될 수 있다.

```java
IntStream.iterate(0, n -> n + 4)
  .takeWhile(n -> n < 100)
  .forEach(System.out::println);
```

- **generate 메서드**

`iterate()`와 마찬가지로 값을 계산하는 무한 스트림을 만들 수 있다. 그러나 `iterate()`와는 달리 `generate()`는 생산된 각 값을 연속적으로 계산하지 않는다. `Supplier<T>`를 인수로 받아서 새로운 값을 생성한다.

```java
Stream.generate(Math::random)
  .limit(5)
  .forEach(System.out::println);
```

`limit()`가 없다면 언바운드 상태가 된다.  또한 병렬 코드에서는 발행자에 상태가 있으면 안전하지 않다. 지금까지 배운 람다는 상태를 바꾸지 않지만, 발행자에서 익명클래스를 호출하면 상태필드를 커스터마이즈 할 수 있기 때문에 부작용이 생길 수 있다. 즉 `iterate()`는 **불변** 상태를 유지하지만 `generate()`는 **가변 상태**를 유지한다. 

스트림을 병렬로 처리하면서 올바른 결과를 얻으려면 **불변 상태 기법**을 고수해야 한다. (7장에서 설명할 예정)

