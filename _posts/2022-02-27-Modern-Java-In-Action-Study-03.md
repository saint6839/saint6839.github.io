---
layout: post
title:  "모던 자바 인 액션 스터디 - chapter3-(1)"
date:   2022-02-27T00:00:00-00:00
author: sangyeop
categories: Sproutt-2nd







---

# 새싹 개발 서적 스터디 - 모던 자바 인 액션 Chapter3 - (1)

분량이 많은 관계로 (1),(2) 두 차례로 나누어 스터디를 진행했다. 이번주는 114page까지를 정리하였다.


이 장의 내용

- 람다란 무엇인가?

- 어디에, 어떻게 람다를 사용하는가?

- 실행 어라운드 패턴

- 함수형 인터페이스, 형식 추론

- 메서드 참조

- 람다 만들기

  

2장에서 **동작 파라미터화**를 사용해서 변화하는 요구사항에 효과적으로 대응하는 코드를 구현할 수 있음을 확인했다. 또한 정의한 코드 블럭을 다른 메서드로 전달하고, 정의한 블록을 특정 이벤트가 발생할 때 실행되도록 설정하거나 알고리즘의 일부로 실행되도록 설정할 수 있었다. 그러나 익명 클래스로 다양한 동작을 구현할 때는 코드가 깔끔하지는 않았다. 더 깔끔한 코드를 위해 3장에서는 람다에 대해서 설명한다.

### 람다란 무엇인가?

> *람다 표현식 이란? :* 메서드로 전달할 수 있는 익명 함수를 단순화 한 것.

- **람다의 특징**
  - 익명 : 보통의 메서드와 달리 이름이 없으므로 **익명**이라고 표현한다. 네이밍에 대한 고민이 없다.
  - 함수 : 람다는 메서드처럼 특정 클래스에 종속되지 않으므로 **함수**라고 부른다. 하지만 메서드처럼 파라미터 리스트, 바디, 반환 형식, 가능한 예외 리스트를 포함한다.
  - 전달 : 람다 표현식을 메서드 인수로 **전달**하거나 변수로 저장할 수 있다.
  - 간결성 : 익명 클래스처럼 많은 자질구레한 코드를 구현할 필요가 없다.



다음은 `Comparator` 객체를 기존보다 간단하게 구현하는 예제이다.

- 기존

``` java
Comparator<Apple> byWeight = new Comparator<Apple>() {
  public int compare(Apple a1, Apple a2) {
    return a1.getWeight(). compareTo(a2.getWeight());
  }
}
```

- 람다 적용 후

```java
Comparator<Apple> byWeight = (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
```

람다 표현식을 이용하면 `compare()` 메서드의 바디를 전달하는 것처럼 코드를 전달할 수 있다. 



람다는 세 부분으로 이루어진다. 위 람다 적용 후 예제를 예로 들어보면

- 파라미터 리스트
  - Comparator의 compare 메서드 파라미터 (사과 두개). `(Apple a1 ,Apple a2)` 
- 화살표
  - 화살표는 람다의 파라미터 리스트와 바디를 구분한다.`->`
- 람다 바디
  - 두 사과의 무게를 비교한다. 람다의 반환값에 해당하는 표현식이다. `a1.getWeight().compareTo(a2.getWeight());`



다음은 자바 8 의 유효한 람다 표현식의 예제 들이다.

```java
(String s) -> s.length() // String 형식의 파라미터 하나를 가지고, int를 반환한다. return을 생략하고 있다.
(Apple a) -> a.getWeight() > 150 // Apple 형식의 파라미터를 하나 가지고, boolean을 반환한다. return 을 생략하고 있다.
(int x, int y) -> {System.out.println("Result:");
                   System.out.println(x + y);
                  } // int 형식 파라미터를 두 개를 가지며 리턴 값이 없다(void). 이 예제에서 여러 행의 문장을 포함할 수 있음을 알 수 있다.
```

간단하게 요약하자면 표현식 스타일 `(parameters) -> expression` , 블록 스타일 `(parameters) -> {statements;}`  두 가지 형태로 표현이 가능하다. 표현식 스타일에서는 return과 ;(세미콜론)을 생략하며, 블록 스타일 사용시에는 return문과 ;(세미콜론)을 명시적으로 사용해야한다.

### 어디에, 어떻게 람다를 사용할까?

2장에서 구현했던 필터 메서드에 람다를 활용 해보자

```java
List<Apple> greenApples = filter(inventory, (Apple a) -> GREEN.equals(a.getColor()));
```

함수형 인터페이스라는 문맥에서 람다 표현식을 사용할 수 있다. 위 예제에서는 `Predicate<T>` 를 기대하는 `filter()` 메서드의 두 번째 인수로 람다 표현식을 전달했다. 그렇다면 **함수형 인터페이스**란 무엇일까?



#### 함수형 인터페이스

2장에서 `Predicate<T>` 인터페이스로 필터 메서드를 파라미터화 했던 것이 기억나는가? 바로 `Predicate<T>`가 함수형 인터페이스이다.

```java
public interface Predicate<T> {
  boolean test(T t);
}
```

위 인터페이스를 보고 알 수 있듯이, 간단하게 말하면 **함수형 인터페이스**는 하나의 추상 메서드를 지정하는 인터페이스다. 이 외에도 아래와 같은 함수형 인터페이스 들이 있다.

```java
public interface Comparator<T> {
  int compare(T o1, T o2);
}
public interface Runnable {
  void run();
}
...
```

람다 표현식으로는 함수형 인터페이스의 추상메서드 구현을 직접 전달할 수 있으므로 **전체 표현식을 함수형 인터페이스의 인스턴스로 취급**(함수형 클인터페이스를 **구현**한 클래스의 인터페이스) 할 수 있다.

#### 함수 디스크립터

> *함수 디스크립터란? :* 람다 표현식의 시그니처를 서술하는 메서드

예를 들어서 `Runnable` 인터페이스의 유일한 추살 메서드 `run()`은 인수와 반환값이 없으므로 `Runnable` 인페이스는 인수와 반환값이 없는 시그니처로 생각할 수 있다. `() -> void` 표기는 파라미터 리스트가 없고 void를 반환하는 함수를 의미하고, `(Apple, Apple) -> int`표기는 두 개의 Apple을 인수로 받아 int를 반환하는 함수를 나타낸다.

왜 함수형 인터페이스를 인수로 받는 메서드에만 람다 표현식을 사용할 수 있을까? 언어 설계자들은 언어를 더 복잡하게 만들지 않는 현재 방법을 선택했다. 또한 대부분의 자바 프로그래머가 하나의 추상 클래스를 갖는 인터페이스에 이미 익숙하다는 점을 고려했기 때문이다.

다음 예제들 통해서 시그니처를 알아보자

```java
execute(() -> {});	
public void execute(Runnable r) {
  r.run();
}
```

`() -> {}`의 시그니처는 `() -> void`이므로 `Runnable`의 추상메서드 `run()`의 시그니처와 일치하므로 유효한 람다 표현식이다.

```java
Predicate<Apple> p = (Apple a) -> a.getWeight();
```

`(Apple a) -> a.getWeight()` 의 시그니처는 `(Apple) -> Integer` 이므로 `Predicate<Apple>`의 `(Apple) -> boolean`의 `test()`메서드의 시그니처와 일치하지 않는다. 따라서 유효하지 않다.

> *@FunctinoalInterface :* 함수형 인터페이스를 선언하는 어노테이션, 선언했는데 실제로 함수형 인터페이스가 아니라면 컴파일 에러를 발생시킨다.



### 람다 활용 : 실행 어라운드 패턴

**실제 자원을 처리하는 코드**를 **설정**과, **정리** 두 과정이 둘러싸는 형태를 **실행 어라운드 패턴** 이라고 한다. 다음 예제를 보자

```java
public String processFile() throws IOException {
  try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
    return br.readLine(); // 실제 필요한 작업을 하는 행
  }
}
```

`try-with-resource` 구문을 사용하였는데, 이를 사용하면 명시적으로 닫을 필요가 없으므로(`br.close()`가 필요 없다) 간결한 코드를 구현하는데 도움을 준다.



#### 1단계 : 동작 파라미터화를 기억하라

위 예시는 파일에서 한 번에 한 줄만 읽을 수 있다. 만약 두 줄을 읽거나 자주 사용되는 단어를 반환하려면 어떻게 해야할까? 기존 설정, 정리 과정을 재사용하고 메서드만 다른 동작을 수행하도록 명령하면 된다(`processFile()` 동작을 파라미터화 한다). `BufferedReader`를 이용해서 다른 동작을 수행할 수 있도록 `processFile()`메서드로 동작을 전달해야 한다.

```java
String result = processFile((BufferedReader br) -> br.readLine() + br.readLine());
```



#### 2단계 : 함수형 인터페이스를 이용해서 동작 전달

함수형 인터페이스 자리에는 람다를 사용할 수 있다.따라서 `BufferedReader -> String`과 `IOException` 을 던질 수 있는 시그니처와 일치하는 함수형 인터페이스를 만들어야 한다. 

```java
@FunctionalInterface
public interface BufferedReaderProcessor {
  String process(BufferedReader b) throws IOException;
}
```

이렇게 정의된 인터페이스를 `processFile()` 메서드 인수로 전달할 수 있다.

```java
public String processFile(BufferedReaderProcessor p) throws IOException {
  ...
}
```



#### 3단계 : 동작 실행

이제 `BufferedReaderProcessor`에 정의된 `process()` 메서드의 시그니처(`BufferedReader -> String`)와 일치하는 람다를 전달할 수 있다. 람다 표현식으로 함수형 인터페이스의 추상 메서드 구현을 직접 전달할 수 있으며, 전달된 코드는 함수형 인터페이스의 인스턴스로 전달된 코드와 같은 방식으로 처리한다. 따라서 `processFile` 바디 내에서 `BufferedReaderProcessor` 객체의`process()` 메서드를 호출할 수 있다.

```java
public String processFile(BufferedReaderProcessor p) throws IOException {
  try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
    return p.process(br);
  }
}
```



#### 4단계 : 람다 전달

이제 람다를 이용해서 다양한 동작을 `processFile()` 메서드로 전달할 수 있다.

한 행을 처리하는 코드

```java
String oneLine = processFile((BufferedReader br) -> br.readLine());
```

두 행을 처리하는 코드

```java
String twoLine = processFile((BufferedReader br) -> br.readLine() + br.readLine());
```



### 함수형 인터페이스 사용

함수형 인터페이스는 오직 하나의 추상 메서드를 지정한다. 함수형 인터페이스의 추상 메서드는 람다 표현식의 시그니처를 묘사한다. 함수형 인터페이스의 추상 메서드 시그니처를 **함수 디스크립터** 라고 한다. 자바 8에서는 다양한 함수형 인터페이스를 제공한다.

#### Predicate

`java.util.function.Predicate<T>` 인터페이스는 T 형식의 객체를 사용하는 불리언 표현식이 필요한 상황에서 `Predicate` 인터페이스를 사용할 수 있다. 다음은 Predicate를 사용한 예제이다.

```java
@FunctionalInterface
public interface Predicate<T> {
  boolean test(T t);
}

public <T> List<T> filter(List<T> list, Predicate<T> p) {
  List<T> results = new ArrayList<>();
  for(T t: list) {
    if(p.test(t)) {
      results.add(t);
    }
  }
  return results;
}
Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
List<String> nonEmpty = filter(listOfStrings, nonEmptyStringPredicate);
```

#### Consumer

`java.util.function.Consumer<T>` 인터페이스는 제네릭 형식 T 객체를 받아서 void를 반환하는 `accept()` 라는 추상 메서드를 정의한다. 즉 반환 값이 없고 소비만 한다고 이해할 수 있다. T 형식의 객체를 인수로 받아서 어떤 동작을 수행하고 싶을때 `Consumer` 인터페이스를 사용할 수 있다. 다음은 Consumer를 사용한 예제이다.

```java
@FunctionalInterface
public interface Consumer<T> {
  void accpet(T t);
}

public <T> void forEach(List<T. list, Consumer<T> c) {
  for(T t : list) {
    c.accept(t);
  }
}

forEach(Arrays.asList(1,2,3,4,5), (integer i) -> System.out.println(i));
```

#### Function

`java.util.function.Function<T, R>` 인터페이스는 제네릭 형식 T를 인수로 받아서 제네릭 형식 R 객체를 반환하는 추상 메서드 `apply()`를 정의한다. 예를 들면 사과의 무게 정보를 추출하거나 문자열을 길이와 매핑하는데 사용할 수 있다. 다음은 String 리스트를 인수로 받아 각 String의 길이를 포함하는 Integer 리스트로 변환하는 `map()`메서드로 정의하는 예제이다.

```java
@FunctionalInterface
public interface Function<T, R> {
  R.apply(T t);
}

public <T, R> List<R> map(List<T> list, Function<T, R> f) {
  List<R> result = new ArrayList<>();
  for(T t : list) {
    result.add(f.apply(t))
  }
}

List<Integer> l = map(Arrays.asList("lamdas", "in", "action"), (String s) -> s.length());
```

**기본형 특화**

자바에서는 기본형을 참조형으로 변환하는 기능을 제공하는데 이를 **박싱**이라고 하고, 반대로 참조형을 기본형으로 바꾸는 동작을 **언박싱**이라고 한다. 또한 박싱과 언박싱이 자동으로 이루어지는 **오토 박싱**기능도 제공한다.

```java
List<Integer> list = new ArrayList<>();
for (int i = 300; i < 400; i++) {
  list.add(i);
}
```

하지만 이런 오토 박싱 과정은 비용이 소모 된다. 박싱한 값은 기본형을 감싸는 객체이므로 힙에 저장되므로 메모리를 더 소모한다. 자바 8에서는 기본형을 입출력 하는 상황에서 오토박싱을 피할 수 있도록 함수형 인터페이스를 제공한다. 아래 예제에서 `IntPredicate`는 1000이라는 값을 박싱하지 않지만, `Predicate<Integer>`의 경우 Integer 객체로 박싱한다.

```java
public interface IntPredicate {
  boolean test(int t);
}

IntPredicate evenNumbers = (int i) -> i % 2 == 0;
evenNumbers.test(1000);		<- 참(박싱 없음)

Predicate<Integer> oddNumbers = (Integer i) -> i % 2 != 0;
oddNumbers.test(1000);		<- 거짓(박싱)
```



### 형식 검사, 형식 추론, 제약

이번에는 컴파일러가 람다의 형식을 어떻게 확인하는지, 피해야 할 사항(람다 표현식에서 바디 안에 있는 지역 변수 참조 금지 등)은 무엇인지 등을 살펴본다.

#### 형식 검사

람다가 사용되는 콘텍스트를 이용해서 람다의 형식(type)을 추론할 수 있다. 어떤 콘텍스트에서 기대되는 람다 표현식의 형식을 **대상 형식**이라고 부른다.

```java
List<Apple> heavierThan150g = filter(inventory, (Apple apple) -> apple.getWeight() > 150);
```

다음 순서로 형식 확인 과정이 진행된다.

1. `filter()`메서드 선언 확인
2. `filter()` 메서드는 두 번째 파라미터로 `Predicate<Apple>` 형식(대상 형식)을 기대한다.
3. `Predicate<Apple>`은 `test()`라는 한 개의 추상 메서드를 정의하는 함수형 인터페이스다.
4. `test()`메서드는 Apple을 받아 boolean을 반환하는 함수 디스크립터를 묘사한다.

람다 표현식이 예외를 던질 수 있다면 추상 메서드도 같은 예외를 던질 수 있도록 **throws** 를 선언해야 한다.

#### 같은 람다, 다른 함수형 인터페이스

**대상 형식** 이라는 특징 때문에 같은 람다 표현식이라도 호환되는 추상 메서드를 가진 다른 함수형 인터페이스로 사용될 수 있다.

```java
Callable<Integer> c = () -> 42;	// 대상형식 Callable<Integer>
PrivilegedAction<Integer> p = () -> 42; // 대상 형식 PrevilegedAction<Integer>
```

위와 같은 경우가 그 예시이다. 즉 하나의 람다 표현식을 다양한 함수형 인터페이스에 사용할 수 있음을 의미한다.

> *다이아몬드 연산자 :* `<>`로 콘텍스트에 따른 제네릭 형식을 추론할 수 있다. 주어진 클래스 인스턴스 표현식을 두 개 이상의 다양한 콘텍스트에 사용할 수 있다. 이때 인스턴스 표현식의 형식 인수는 콘텍스트에 의해 추론된다.

>  *특별한 void 호환 규칙 :* 람다의 바디에 일반 표현식이 있으면 void를 반환하는 함수 디스크립터와 호환된다(물론 파라미터 리스트도 호환되어야 함).
>
> ```java
> //Predicate는 boolean 반환값을 갖는다.
> Predicate<String> p = s -> list.add(s);
> //Consumer는 void 반환값을 갖는다.
> Consumer<String> b = s -> list.add(s);
> ```

#### 형식 추론

자바 컴파일러는 람다 표현식이 사용된 콘텍스트(대상 형식)을 이용해서 람다 표현식과 관련된 함수형 인터페이스를 추론한다. 즉, 대상 형식을 이용해서 함수 디스크립터를 알 수 있으므로 컴파일러는 시그니처도 추론할 수 있다. 결과적으로 컴파일러는 람다 표현식의 파라미터 형식에 접근할 수 있으므로 람다 문법에서 이를 생략할 수 있다.

```java
Comparator<Apple> c = (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWieght());
Comparator<Apple> c = (a1, a2) -> a1.getWeight().compareTo(a2.getWeight());
```

하지만 이 또한 상황에 따라서 어떤 코드가 가독성이 좋을지 개발자가 판단하여 사용해야한다.

#### 지역 변수 사용

람다 표현식에서는 익명 함수가 하는 것처럼 **자유 변수**(파라미터로 넘겨진 변수가 아닌 외부에서 정의된 변수)를 활용할 수 있다. 이와 같은 동작을 **람다 캡쳐링**이라고 한다.

```java
int portNumber = 1337;
Runnable r = () -> System.out.println(portNumber);
```

그러나 람다에서 사용되는 자유 변수는 `final`로 선언되어야 하거나, 실질적으로 `final`처럼 사용되어야 한다. 아래와 같은 경우 `portNumber`에 값이 두번 할당되므로 컴파일 되지 않는다.

```java
int portNumber = 1337;
Runnable r = () -> System.out.println(portNumber);
portNumber = 31337;
```

#### 지역 변수의 제약

인스턴스 변수는 힙에 저장되는 반면 지역 변수는 스택에 위치한다. 람다에서 지역 변에 바로 접근할 수 있다는 가정하에 람다가 스레드에서 실행된다면 변수를 할당한 스레드가 사라져서 변수 할당이 해제되었음에도 해당 변수에 접근하려 할 수 있다.따라서 자바 구현에서는 원래 변수에 접근을 허용하지 않고, 자유 지역 변수의 복사본을 제공한다. 따라서 복사본의 값이 바뀌지 않아야 하므로 `final` 이거나 `final`처럼 사용해야 한다는 제약이 생긴 것이다.