---
layout: post
title:  "모던 자바 인 액션 스터디 - chapter6-(2)"
date:   2022-04-11T00:00:00-00:00
author: sangyeop
categories: Sproutt-2nd




---



# 새싹 개발 서적 스터디 - 모던 자바 인 액션 Chapter6-(2)

### 분할

**분할 함수**라고 불리는 프레디케이트를 분류 함수로 사용하는 특수한 그룹화 기능이다. 프레디케이트는 불리언을 반환하므로 맵의 키 형식은 `Boolean`이다. 다음은 요리 중 채식 요리와 채식이 아닌 요리를 분류하는 코드이다.

```java
Map<Boolean, List<Dish>> partitionedMenu = menu.stream().collect(partitioningBy(Dish::isVegetarian)); // 분할 함수
```

이는 다음을 반환한다.

```
{false=[pork, beef, chicken, prawns, salmon], true=[french fries, rice, season fruit, pizza]}
```

여기서 `true` 값을 키고 맵에서 채식 요리들을 얻을 수 있다.

```java
List<Dish> vegetarianDishes = partitionedMenu.get(true);
```

#### 분할의 장점

분할 함수가 반환하는 참, 거짓 두 가지 요소의 스트림 리스트를 모두 유지한다는 것이 장점이다. 다음과 같이 채식이 아닌 모든 요리 리스트를 얻을 수 있으며, 다음 처럼 컬렉터를 두 번째 인수로 전달할 수 있는 오버로드 버전의 `partitioningBy` 메서드도 있다.

```java
Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType =
  menu.stream().collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));
```

다음은 결과이다

```
{false={FISH=[prawns, salmon], MEAT=[pork, beef, chicken]}, true={OTHER=[french fries, rice, season fruit, pizza]}}
```

`partitioningBy`가 반환한 맵 구현은 참과 거짓 두 가지 키만 포함하므로 더 간결하고 효과적이다.

#### 숫자를 소수와 비소수로 분할하기

다음은 정수 n을 인수로 받아서 2에서 n가지의 자연수를 소수와 비소수로 나누는 프로그램을 구현하자.

```java
public boolean isPrime(int candidate) {
  int candidateRoot = (int) Math.sqrt((double)candidate); // 소수 대상을 주어진 수의 제곱근 이하로 제한
  return IntStream.range(2, candidateRoot) // 2부터 candiate 미만 자연수 생성
    .nonMatch(i -> candidate % i == 0); // 스트림 모든 숫자로 candidate 나눌 수 있으면 참, 없으면 거짓
}
```

소수인지 아닌지를 판별하기 위해 작성한 `isPrime` 메서드를 프레디케이트로 이용하고 `partitioningBy` 컬렉터로 리듀싱해서 소수와 비소수 분류가 가능하다.

```java
public Map<Boolean, List<Integer>> partitionPrimes(int n) {
  return IntStream.reangeClosed(2,n).boxed()
    .collect(partitoningBy(candidate -> isPrime(candidate)));
}
```



### Collector 인터페이스

다음은 `Collector` 인터페이스의 시그니처와 다섯 개의 메서드 정의를 보여준다.

```java
public interface Collector<T, A, R> {
  Supplier<A> supplier();
  BiConsumer<A, T> accumulator();
  Function<A, R> finisher();
  BinaryOperator<A> combiner();
  Set<Characteristics> characteristics();
}
```

- T

  수집될 스트림 항목의 제네릭 형식

- A

  누적자, 즉 수집 과정에서 중간 결과를 누적하는 객체의 형식

- R

  수집 연산 결과 객체의 형식(대개 컬렉션 형식)

#### Collector 인터페이스의 메서드 살펴보기

**supplier 메서드 : 새로운 결과 컨테이너 만들기**

수집 과정에서 빈 누적자 인스턴스를 만드는 파라미터가 없는 함수다.  `ToListCollect`에서 `supplier`는 다음처럼 빈 리스트를 반환한다.

> `ToListCollector` : `Stream<T>`의 모든 요소를 `List<T>`로 수집하는 클래스

```java
public Supplier<List<T>> supplier() {
  return () -> new ArrayList<T>();
}
```

**accumulator 메서드 : 결과 컨테이너에 요소 추가하기**

리듀싱 연산을 수행하는 함수를 반환한다. 스트림 n번째 요소를 탐색할 때 두 인수, 누적자와 n번째 요소를 함수에 적용한다. `ToListCollector`에서 `accumulator`가 반환하는 함수는 이미 탐색한 항목을 포함하는 리스트에 현재 항목을 추가하는 연산을 수행한다.

```java
public BiConsumer<List<T>, T> accumulator() {
  return (list, item) -> list.add(item);
}
```

**finisher 메서드 : 최종 변환값을 결과 컨테이너로 적용하기**

스트림 탐색을 끝내고 누적자 객체를 최종 결과로 변환하면서 누적 과정을 끝낼 때 호출할 함수를 반환해야 한다. 때로는 누적자 객체가 이미 최종 결과인 상황도 있는데, 이대는 변환 과정이 필요하지 않으므로 `finisher` 메서드는 항등 함수를 반환한다.

```java
public Function<List<T>, List<T>> finisher() {
  return Function.identity();
}
```

 **combiner 메서드 : 두 결과 컨테이너 병합**

스트림의 서로 다른 서브파트를 병렬로 처리할 때 누적자가 이 결과를 어떻게 처리 할지 정의한다. 다음은 `toList`의 `combiner` 구현이다.

```java
public BinaryOperator<List<T>> combiner() {
  return (list1, list2) -> {
    list1.addAll(list2);
    return list1;
  }
}
```

이를 사용하면 스트림의 리듀싱을 병렬로 수행할 수 있다.

- 스트림을 분할해야 하는지 정의하는 조건이 거짓으로 바뀌기 전까지 원래 스트림을 재귀적으로 분할한다.
- 서브스트림의 각 요소에 리듀싱 연산을 순차적으로 적용해서 서브스트림을 병렬로 처리할 수 있다.
- 마지막에는 컬렉터의 `combiner` 메서드가 반환하는 함수로 모든 부분결과를 쌍으로 합친다. 즉, 분할된 모든 서브스트림의 결과를 합치면서 연산이 완료된다.

**characteristics 메서드**

컬렉터 연산을 정의하는 `Characteristics` 형식의 불변 집합을 반환한다. `Characteristics`는 스트림을 병렬로 리듀스할 것인지 그리고 병렬로 리듀스한다면 어떤 최적화를 선택해야 할 지 힌트를 제공한다. `Characteristics`는 다음 세 항목을 포함하는 열거형이다.

- UNORDERED 

  리듀싱 결과는 스트림 요소의 방문 순서나 누적 순서에 영향을 받지 않는다.

- CONCURRENT

  다중 스레드에서 `accumulator` 함수를 동시에 호출할 수 있으며 이 컬렉터는 스트림의 병렬 리듀싱을 수행할 수 있다. 만약 UNORDERED를 설정하면 데이터 소스가 정렬되어 있지 않은 상황에서도 병렬 리듀싱을 수행할 수 있다.

- IDENTITY_FINISH

  리듀싱 과정의 최종 결과로 누적자 객체를 바로 사용할 수 있다. 또한 누적자 A를 결과 R로 안전하게 변환이 가능하다.

#### 응용하기

**컬렉터 구현을 만들지 않고도 커스텀 수집 진행하기**

`IDENTIFY_FINISH` 수집 연산에서는  `Collector` 인터페이스를 새로 구현하지 않고도 같은 결과를 얻을 수 있다. 예를 들면 다음처럼 스트림의 모든 항목을 리스트에 수집하는 방법도 있다. Stream은 발행, 누적, 합침을 인수로 받는 `collect` 메서드를 오버로드하며 각각의 메서드는 `Collector` 인터페이스의 메서드가 반환하는 함수와 같은 기능을 수행한다.

```java
List<Dish> dishes = menuStream.collect(
  ArrayList::new, // 발행
  List::add, // 누적
  List::addAll // 합침
);
```



### 커스텀 컬렉터를 구현해서 성능 개선하기

다음은 커스텀 컬렉터를 이용해서 n이하의 자연수를 소수와 비소수로 분할하는 예제이다

```java
public Map<Boolean, List<Integer>> partitionPrimes(int n) {
  return IntStream.rangeClosed(2, n).boxed()
    .collect(partitioningBy(candidate -> isPrime(candidate));
}
```

이를 대상 숫자 범위를 제한해서 `isPrime` 메서드를 개선했다.

```java
public boolean isPrime(int candidate) {
  int candidateRoot = (int) Math.sqrt((double) candidate);
  return IntStream.rangeClosed(2, candidateRoot)
    .noneMatch(i -> candidate % i == 0);
}
```

#### 소수로만 나누기

소수로 나누어 떨어지는지 확인해서 범위를 좁힐 수 있었다. 제수가 소수가 아니면 소용없으므로 현재 숫자 이하에서 발견한 소수로 제한할 수 있다. 그러려면 주어진 숫자가 소수인지 아닌지 판단해야 하는데, 그렇다면 지금까지 발견한 소수 리스트에 접근해야 한다. 하지만 지금까지 살펴본 바로는 컬렉터 수집 과정에서 부분 결과에 접근할 수 없었다. 이때 커스텀 클래스로 이 문제를 해결할 수 있다.

```java
public static boolean isPrime(List<Integer> primes, int candidate) {
  return primes.stream().noneMatch(i -> candidate % i == 0);
}
```

중간 결과 리스트를 이용해서 위와 같이 구현할 수 있으며, 아래 코드처럼 정렬된 리스트와 프레디케이트를 인수로 받아 리스트의 첫 요소에서 시작해서 프레디케이트를 만족하는 가장 긴 요소로 이루어진 리스트를 반환하는 `takeWhile`이라는 메서드를 구현한다.

```java
public static boolean isPrime(List<Integer> primes, int candidate) {
  int candidateRoot = (int) Math.sqrt((double) candidate);
  return primes.stream()
    .takeWhile(i -> i <= candidateRoot)
    .noneMatch(i -> candidate % i == 0);
}
```

위에서 새로운 `isPrime` 메서드를 구현했으니, 이제 커스텀 컬렉터를 구현한다.

**1단계 : Collector 클래스 시그니처 정의**

```java
public interface Collector<T, A, R>
```

```java
public class PrimeNumbersCollector implements Collector<Integer,
Map<Boolean, List<Integer>>,
Map<Boolean, List<Integer>>>
```

T : 스트림 요소 형식

A : 중간 결과 누적 객체 형식

R : 최종 결과 형식

**2단계 : 리듀싱 연산 구현**

`Collector` 인터페이스에 구현된 다섯 메서드를 구현한다.

- `supplier`

```java
public Supplier<Map<Boolean, List<Integer>>> supplier() {
  return () -> new HashMap<Boolean, List<Integer>>() {{
    put(true, new ArrayList<Integer>());
    put(false, new ArrayList<Integer>());
  }}
}
```

누적자로 사용할 `HashMap`을 만들고 `true,false`로 키와 빈 리스트로 초기화 했다. 수집 과정에서 소수와 비소수를 추가할 것이다.

이렇게 함으로써 언제든지 원할 때 수집 과정의 중간 결과인 지금까지 발견한 소수를 포함하는 누적자에 접근할 수 있게 된다.

**3단계 : 병렬 실행할 수 있는 컬렉터 만들기(가능하다면)**

병렬 수집 과정에서 두 부분 누적자를 합칠 수 있는 메서드를 만든다

```java
public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
  return (Map<Boolean, List<Integer>> map1, Map<Boolean, List<Integer>> map2) -> {
    map1.get(true).addAll(map2.get(true));
    map1.get(false).addAll(map2.get(false));
    return map1;
  }
}
```

알고리즘 자체가 순차적이기 때문에 컬렉터를 실제 병렬로 사용할 수는 없다.

**4단계 : finisher 메서드와 컬렉터의 characteristics 메서드**

`accumulator` 형식은 컬렉터 결과 형식과 같으므로 변환 과정이 필요 없다. 따라서 항등 함수 `identity`를 반환하도록 `finisher` 메서드를 구현한다.

```java
public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
  return Function.identity();
}
```

커스텀 클래스는 CONCURRENT도 아니고 UNORDERED도 아니지만 IDENTITY_FINISH 이므로 다음처럼 `characteristics`메서드를 구현할 수 있다.

```java
public Set<Characteristics> characteristics() {
  return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH));
}
```



### 마치며

- `collect`는 스트림 요소를 요약 결과로 누적하는 다양한 방법을 인수로 갖는 최종 연산이다.
- 스트림 요소를 하나의 값으로 리듀스하고 요약하는 컬렉터뿐 아니라 최소값, 최대값, 평균값 계산 등 다양한 컬렉터들이 정의되어 있다.
- `groupingBy`로 스트림 요소륽 ㅡ룹화 하거나 `partitioningBy`로 스트림 요소를 분할할 수 있다
- 컬렉터는 다수준 그룹화, 분할, 리듀싱 연산에 적합하다.
- `Collector` 인터페이스 구현으로 커스텀 컬렉터를 개발할 수 있다.



