---
layout: post
title:  "모던 자바 인 액션 스터디 - chapter5-(1)"
date:   2022-03-19T00:00:00-00:00
author: sangyeop
categories: Sproutt-2nd




---

# 

# 새싹 개발 서적 스터디 - 모던 자바 인 액션 Chapter5-(1)

이 장의 내용

- 필터링, 슬라이싱, 매칭
- 검색, 매칭, 리듀싱
- 특정 범위의 숫자와 같은 숫자 스트림 사용하기
- 다중 소스로부터 스트림 만들기
- 무한 스트림

**외부 반복 내부 반복으로 변환하는 예시**

- 외부 반복

```java
List<Dish> vegetarianDishes = new ArrayList<>();
for(Dish d : menu) {
  if(d.isVegetarian()) {
    vegetarianDishes.add(d);
  }
}
```

- 내부 반복

```java
List<Dish> vegetarianDishes = menu.stream()
  .filter(Dish::isVegetarian())
  .collect(toList());
```



### 필터링

#### 프레디케이트로 필터링

> *프레디케이트란? :* 불리언을 반환하는 함수

`filter()` 메서드는 프레디케이트를 인수로 받아서 프레디케이트와 일치하는 모든 요소를 포함하는 스트림을 반환한다.

```java
List<Dish> vegetarianDishes = menu.stream()
  .filter(Dish::isVegetarian()) // 채식 요리인지 확인하는 메서드를 참조함
  .collect(toList());
```

#### 고유 요소 필터링

`distinct()` 메서드를 통해 고유 요소로 이루어진 스트림을 반환하여, 중복을 제거 할 수 있다.

```java
List<Integer> numbers = Arrays.asList(1,2,1,3,3,2,4);
numbers.stream()
  .filter(i -> i % 2 == 0)
  .distinct()
  .forEach(System.out::println);
```

- `distinct()`가 없을 경우

```
2
2
4
```

- `distinct()`가 있을 경우

```
2
4
```

### 스트림 슬라이싱

#### 프레디케이트를 이용한 슬라이싱

- **TAKEWHILE 활용**

  `filter()` : 조건문이 참인지 모든 요소를 검사하며 넘어간다

  ```java
  Stream.of(1,2,3,4,5,6,7,8,9)
    .filter(n -> n%2 == 0)
    .forEach(System.out::println);
  ```

  ```
  2
  4
  6
  8
  ```

  `takeWhile()` : 조건문이 참이 아닐 경우 해당 요소에서 멈춤

  ```
  Stream.of(2,4,5,6,7,8,9)
    .takeWhile(n -> n%2 == 0)
    .forEach(System.out::println);
  ```

  ```
  2
  4
  ```

- **DROPWHILE 활용**

  `dropWhile()`은 `takeWhile()` 과 정반대 작업을 수행한다. 처음으로 거짓이 나타나는 지점까지의 값을 버리고, 그 나머지 요소들을 반환한다. 

  무한한 남은 요소를 가진 무한 스트림에서도 동작한다.

  ```java
  Stream.of(2,4,5,6,7,8,9)
    .takeWhile(n -> n<=5)
    .forEach(System.out::println);
  ```

  ```
  6
  7
  8
  9
  ```

#### 스트림 축소

`limit(n)` 메서드를 이용해서 주어진 갓 이하의 크기를 갖는 새로운 스트림을 반환 할 수 있다.

```java
Stream.of(2,4,5,6,7,8,9)
  .filter(n -> n<=7)
  .limit(3)
  .forEach(System.out::println);
```

```
2
4
5
```

`filter()` 메서드와 조합했을때 프레디케이트와 일치하는 처음 세 요소를 선택한 다음 즉시 결과를 반환한다.

또한 정렬되어 있지 않은 스트림(ex. `Set`) 에도 사용 될 수 있다. 이 경우 limit의 결과도 정렬되어 있지 않은 상태로 반환된다.

#### 요소 건너뛰기

`skip(n)` 메서드를 이용해서 처음 n개 요소를 제외한 스트림을 반환 할 수 있다.

```java
Stream.of(2,4,5,6,7,8,9)
  .filter(n -> n<=7)
  .limit(2)
  .forEach(System.out::println);
```

```
5
6
7
```

### 매핑

#### 스트림의 각 요소에 함수 적용하기

`map()` 메서드는 함수를 인수로 받는다. 인수로 제공도니 함수는 각 요소에 적용되며 함수를 적용한 결과가 새로운 요소로 매핑된다.

```java
List<String> dishNames = menu.stream()
  .map(Dish::getName)
  .collect(toList());
```

`getName()`은 문자열을 반환하므로 메서드의 출력 스트림은 `Stream<String>`형식을 갖는다.

#### 스트림 평면화

**[고유 문자로 이루어진 리스트 반환 예제]**

["Hello", "World"] 리스트가 있을때 결과로 ["H","e","l","o","W","r","d"] 리스트가 반환되도록 한다.

- **distinct 를 사용해 중복제거를 할 경우**

  우리가 원하는 반환 타입은 `Stream<String>` 이지만 각 문자열의 배열을 담은 `Stream<String[]>`이 반환된다. 

```java
List<String> words = new ArrayList<>();
words.add("Hello");
words.add("World");
List<String[]> result = words.stream()
    .map(word -> word.split(""))
    .collect(toList());

String[] hello = result.get(0);	// Hello 별개
String[] world = result.get(1); // World 별개
for (String s : hello) {
    System.out.print(s + " ");
}
System.out.println();
for (String s : world) {
    System.out.print(s + " ");
}
```

이 문제를 다음과 같은 방법을 통해 해결할 수 있다.

- **map과 Arrays.stream 활용**

  `Arrays.stream()` :  문자열을 받아 스트림을 만들어주는 메서드

  ```java
  List<Stream<String>> result = words.stream()
    .map(word -> word.split(""))
    .map(Arrays::stream)
    .distinct()
    .collect(toList());
  ```

- **flatMap 사용**

  ```java
  List<String> uniqueCharacters = words.stream()
    .map(word -> word.split("")) // 각 단어를 개별 문자를 포함하는 배열로 변환
    .flatMap(Arrays::stream) // 생성된 스트림을 하나의 스트림으로 평면화
    .distinct()
    .collect(toList());
  ```

  즉 `flatMap()` 메서드는 스트림의 각 값을 다른 스트림으로 만든 다음에 모든 스트림을 **하나의 스트림으로** **연결(평면화)** 하는 기능을 수행한다.

  

### 검색과 매칭

#### 프레디케이트가 적어도 한 요소와 일치하는지 확인

`anyMatch()` : 스트림에서 **적어도 한 요소**가 프레디케이트와 일치하는지 검사한다.

#### 프레디케이트가 모든 요소와 일치하는지 검사

`allMatch()` : 스트림에서 **모든 요소**가 프레디케이트와 일치하는지 검사한다.

#### 프리디케이트와 모든 요소가 일치하지 않는지 검사

`nonMatch()` : 스트림에서 **일치하는 요소가 없는지** 검사한다.

위 세 메서드는 자바의 &&, || 같은 **쇼트 서킷** 기법을 활용한다.

> *쇼트 서킷이란? :* 전체 스트림을 처리하지 않고도 결과를 반환할 수 있음을 의미한다. 예를 들어 여러 and 연산으로 연결된 커다란 불리언 표현식에서 하나라도 거짓이라는 결과가 나오면 나머지 표현식의 결과와. 상관없이 전체 결과도 거짓이 된다.

#### 요소 검색

`findAny()` : 현재 스트림에서 임의의 요소를 반환한다. `filter()` 메서드와 연결해서 특정한 요소를 반환 받을 수 있다.



### Optional이란?

`Optional<T>` 클래스는 값의 존재나 부재 여부를 표현하는 컨테이너 클래스이다.

- `isPresent()`

  Optional이 값을 포함하면 `true`를 반환하고, 값을 포함하지 않으면 `false`반환한다.

- `ifPresent(Consumer<T> block)` 

  값이 있으면 주어진 블록을 실행한다. T형식 인수를 받으며 void 반환 람다를 전달할 수 있다.

- `T get()`

  값이 존재하면 값을 반환, 값이 없으면 `NoSuchElementException`을 일으킨다

  - `T orElse(T other)`

    값이 있으면 값을 반환하고, 값이 없으면 기본값을 반환한다.

```java
menu.stream()
  .filter(Dish::isVegetarian)
  .findAny()
  .ifPresent(dish -> System.out.println(dish.getName())); // 값이 있으면 출력, 없으면 아무 일도 안일어남
```

#### 첫 번째 요소 찾기

`findFirst()` : 아이템 순서가 정해져 있는 일부 스트림에서 첫 번째 요소를 찾기 위해서 사용하는 메서드

> *`findFirst()` vs `findAny()` 차이* : 병렬 실행시에는 첫 번쨰 요소를 찾기 어렵기 때문에, 요소 반환 순서가 상관 없다면 병렬 스트림에서는 제약이 적은 `findAny()`를 사용한다.



### 리듀싱

> *리듀싱 이란? :* 모든 스트림 요소를 처리해서 값으로 도출하는 과정

> *폴드 란? :* 함수형 프로그래밍 용어로 이 과정이 종이(스트림)를 작은 조각이 될 때까지 반복해서 접는 것과 비슷하다고 하여 붙인 용어

#### 요소의 합

- `reduce()` 사용 전

  ```java
  int sum = 0;
  for (int x : numbers) {
    sum += x;
  }
  ```

  사용한 매개 변수

  - sum 변수의 초기값 0
  - 리스트의 모든 요소를 조합하는 연산(+)

- `reduce()` 사용 후

  ```java
  int sum = numbers.stream().reduce(0, (a, b) -> a + b);
  ```

  사용한 매개 변수 

  - 초기값 0
  - 두 요소를 조합해서 새로운 값을 만드는 `BinaryOperator<T>`인 `(a, b) -> a + b`

- 메서드 참조 이용

  ```java
  int sum = numbers.stream().reduce(0, Integer::sum);
  ```

  

### 초기값 없음

초기 값을 받지 않도록 오버로드 된 `reduce()` 메서드는 `Optional<T>` 객체를 반환한다.

```java
Optional<Integer> sum = numbers.stream().reduce((a, b) -> (a + b));
```

#### 최대값과 최소값

`reduce()` 는 두 인수를 받는다

- 초기값
- 스트림의 두 요소를 합쳐서 하나의 값으로 만드는데 사용할 람다

다음 처럼 최대값과 최소값을 찾을 수 있다.

```java
Optional<Integer> max = numbers.stream().reduce(Integer::max); // 최대값
Optional<Integer> min = numbers.stream().reduce(Integer::min); // 최소값
```



#### 리듀스 메서드 장점과 병렬화

`reduce()`를 이용하면 내부 반복이 추상화 되면서 내부 구현에서 병렬로 `reduce()` 메서드를 실행할 수 있다. 반복적인 합계에서는 sum 변수를 공유해야 하므로 쉽게 병렬화하기 어렵다. 스트림에서는 모든 요소를 더하는 코드를 병렬로 만드는 방법이 있는데 `parallelStream()` 이라는 메서드를 이용해서 병렬화가 가능하다.

```java
int sum = numbers.parallelStream().reduce(0, Integer::sum);
```

#### 스트림 연산 : 상태 없음과 상태 있음

스트림의 요소를 정렬하거나 중복을 제거하려면 과거 이력을 알고 있어야 한다. 어떤 요소를 출력 스트림으로 추가하려면 **모든 요소가 버퍼가 추가 되어 있어야 한다.** 이러한 연산을 **내부 상태를 갖는 연산**이라고 한다.

