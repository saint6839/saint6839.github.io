---
layout: post
title:  "모던 자바 인 액션 스터디 - chapter4"
date:   2022-03-11T00:00:00-00:00
author: sangyeop
categories: Sproutt-2nd



---

# 새싹 개발 서적 스터디 - 모던 자바 인 액션 Chapter4


이 장의 내용

- 스트림이란 무엇인가?
- 컬렉션과 스트림
- 내부 반복과 외부 반복
- 중간 연산과 최종 연산

### 스트림의 등장 배경

- SQL문에서는 `SELECT name FROM dishes WHERE calorie < 400` 과 같이 속성을 이용해 어떻게 필터링 할 것 인지는 따로 구현할 필요가 없다 (반복자, 누적자 불필요). 컬렉션도 이와 같이 만들 수 있지 않을까?
- 많은 요소를 포함하는 컬렉션의 경우에는 병렬 처리 코드를 구현해 요소를 처리해야 하는데, 이는 매우 복잡하고 디버깅도 어렵다.

이 문제들의 답으로 등장하게 된 것이 **스트림**이다.

### 스트림이란 무엇인가?

- 자바 8 API에 새로 추가된 기능
- 선언형( 데이터를 처리하는 임시 구현 코드 대신 질의로 표현 가능하다는 의미 )
- 데이터 컬렉션 반복을 멋있게 처리 가능
- 멀티스레드 구현 없이 데이터 **투명하게** 병렬 처리 가능

**[자바 7을 이용한 기존 코드]**

```java
List<Dish> lowCalroricDishes = new ArrayList<>();
for(Dish dish : menu) {  // 누적자로 요소 필터링
  if(dish.getCalories() < 400) {
    lowCaloricDishes.add(dish);
  }
}
Collections.sort(lowCaloricDishes, new Comparator<Dish>() { // 익명클래스로 요리 정렬
  public int compare(Dish dish1, Dish dish2) {
    return Integer.compare(dish1.getCalories(), dish2.getCalories());
  }
}
List<String> lowCarloricDishesName = new ArrayList<>();
for(Dish dish : lowCaloricDishes) {
  lowCaloricDishesName.add(dish.getName()); // 정렬된 리스트를 처리하면서 요리 이름 선택
}                 
```

`lowCaloricDishes`는 컨테이너 역할만 하는 중간 변수이다. 자바 8은 이러한 세부 구현을 라이브러리 내에서 모두 처리한다.

**[자바 8을 이용한 최신 코드]**

```java
import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

public class StreamExample {
    public static void main(String[] args) {
        List<Dish> menu = new ArrayList<>();
        menu.add(new Dish(100, "초콜렛"));
        menu.add(new Dish(200, "쿠키"));
        menu.add(new Dish(500, "케이크"));
        
        List<String> lowCaloricDishesName = menu.stream()
                .filter(d -> d.getCalories() < 400) // 400 칼로리 이하 요리 선택
                .sorted(comparing(Dish::getCalories)) // 칼로리로 요리 정렬
                .map(Dish::getName) // 요리명 추출
                .collect(toList()); // 추출한 모든 요리명을 리스트에 저장
    }
}
```

```
초콜렛
쿠키
```

위 코드에서 `stream()`을 `parallelStream()`로 바꾸면 멀티코어 아키텍처에서 병렬로 실행할 수 있다. 정확하게 이 의미가 무엇인지에 대해서는 **7장**에서 설명한다.

#### 스트림이 주는 다양한 장점

- **선언형으로 코드를 구현할 수 있다**

  루프, if 조건문 등 없이 '저칼로리의 요리만 선택하라' 같은 동작 수행을 지정할 수 있다. 또한 동작 파라미터화를 함께 활용하면 '고칼로리의 요리만 선택하라' 와 같은 요구사항이 들어왔을때 기존 코드를 복사할 필요 없이 람다 표현식을 이용해 요구사항 변화에 대응할 수 있다.

- **복잡한 데이터처리 파이프라인 생성 가능**

  `filter()` -> `sorted()` -> `map()` -> `collect()` 순으로 데이터 처리 결과가 연결된다.

`filter()` 같은 연산들은 **고수준 빌딩 블록**으로 이루어져 있으므로 특정 스레딩 모델에 제한되지 않고 어떤 상황에서든 사용할 수 있다. 때문에 병렬화 과정에서 스레드와 락을 걱정할 필요가 없어졌다.

> 고수준 빌딩 블록이 무엇인지 아직 잘 이해가 가지 않는다. 질의형과 같은 선언형으로 작성되어진 것을 고수준 빌딩 블록이라고 하는 것일까?

**요약**

- **선언형** : 더 간결하고 좋은 가독성
- **조립할 수 있음** : 유연성이 좋아진다
- **병렬화** : 성능이 좋아진다



### 스트림 시작하기

> *스트림이란? :* 데이터 처리 연산을 지원하도록 소스에서 추출된 연속된 요소

- **연속된 요소** 

  컬렉션과 마찬가지로 스트림은 특정 요소 형식으로 이루어진 연속된 값 집합의 인터페이스를 제공한다. 컬렉션은 `ArrayList`를 사용할 것인지 `LinkedList`를 사용할 것인지에 대한 시간과 공간 복잡성과 관련된 요소 저장 및 접근 연산이 주를 이루지만, 스트림은 `fitler()`, `sorted()` , `map()` 과 같은 표현 계산식이 주를 이룬다.

  **컬렉션의 주제는 데이터고 스트림의 주제는 계산이다**

- **소스**

  컬렉션, 배열, I/O 자원 등에서 제공 받은 소스로부터 데이터를 소비한다. ( ex. 리스트로 스트림을 만들면 스틀미 요소는 리스트의 요소와 같은 순서를 유지한다.)

- **데이터 처리 연산**

  함수형 프로그래밍 언어에서 지원하는 연산, 데이터베이스와 비슷한 연산을 지원한다. ( ex. filter, map, reduce, find, match, sort 등으로 데이터 조작 가능)

#### 스트림의 두 가지 중요 특징

- **파이프라이닝**

  스트림 연산들은 스트림 연산끼리 연결해서 커다란 파이프 라인을 구성할 수 있도록 스트림 자신을 반환한다.

- **내부 반복**

  반복자를 명시적으로 사용해 반복(외부 반복)하는 컬렉션과 달리 스트림은 내부 반복을 지원한다.

```java
import static java.util.stream.Collectors.*;

public class StreamExample {
    public static void main(String[] args) {
        List<Dish> menu = new ArrayList<>();
        menu.add(new Dish(100, "초콜렛"));
        menu.add(new Dish(200, "쿠키"));
        menu.add(new Dish(500, "케이크"));
        menu.add(new Dish(600, "라면"));
        menu.add(new Dish(700, "떡볶이"));
        menu.add(new Dish(800, "치킨"));
        menu.add(new Dish(900, "피자"));


        List<String> threeHighCaloricDishNames = menu.stream() // 메뉴(요리 리스트)에서 스트림을 얻는다
                .filter(dish -> dish.getCalories() > 300) // 파이프라인 연산 만들기, 첫 번째로 고칼로리 요리 필터링
                .map(Dish::getName) // 요리명 추출
                .limit(3) // 선착순 세 개만 선택
                .collect(toList()); // 결과를 다른 리스트로 저장
        System.out.println(threeHighCaloricDishNames); // 케이크, 라면, 떡볶이
    }
}
```

- `filter`

  람다를 인수로 받아 스트림에서 특정 요소를 **필터링(제외)** 시킨다.

- `map`

  람다를 이용해 한 요소를 다른 요소로 **변환하거나 정보를 추출**한다.

- `limit`

  정해진 개수 이상의 요소가 스트림에 저장되지 못하게 **스트림의 크기를 축소하기 위해서 자른다**

- `collect`

  **스트림을 다른 형식으로 변환**한다. 여기서는 `toList()`를 이용해 스트림을 리스트로 변환했다.

  

### 스트림과 컬렉션

스트림과 컬렉션의 가장 큰 차이는 **데이터를 언제 계산 하느냐**에 있다.

- **컬렉션**

  - **모든** 값을 메모리에 저장하는 자료구조이다. 컬렉션에 요소를 추가하거나 컬렉션의 요소를 삭제할 수 있다.
  - 컬렉션은 적극적으로 생성된다(**생산자 중심**)
  - 비유 : DVD

- **스트림**

  - **요청할 때만 요소를 계산**하는 고정된 자료구조이다. 즉, 스트림에 요소를 추가하거나 스트림에서 요소를 제거할 수 없다. 
  - 결과적으로 **생산자**와 **소비자** 관계를 형성한다
  - 사용자가 데이터를 요청할 때만 값을 계산하므로(**요청자 중심**), 게으르게 만들어지는 컬렉션 같다고도 한다.
  - 비유 : 인터넷 스트리밍

  

스트림과 컬렉션의 차이로는 다음과 같은 것들이 있다.

#### 딱 한 번만 탐색할 수 있다

반복자와 마찬가지로 스트림도 한 번만 탐색할 수 있다.

```java
List<String> title = Arrays.asList("Java8", "In", "Action");
Stream<String> s = title.stream();
s.forEach(System.out::println); // title의 각 단어를 출력
s.forEach(System.out::println); // java.lang.IllegalStateException: 스트림이 이미 소비되었거나 닫힘
```

 #### 외부 반복과 내부 반복

> *외부 반복 :* 요소를 사용하려면 사용자가 직접 요소를 반복해야 한다(컬렉션)

> *내부 반복 :* 반복을 알아서 처리하고 결과 스트림값을 어딘가에 저장해준다. 함수에 어떤 작업을 수행할지만 지정하면 모든 것이 알아서 처리된다.

- 컬렉션 : for-each 루프를 이용하는 외부 반복

```java
List<String> names = new ArrayList<>();
for(Dish dish : menu) { // 메뉴 리스트를 명시적으로 순차 반복
  names.add(dish.getName()); // 이름을 추출해서 리스트에 추가
}
```

- 스트림 : 내부 반복

```java
List<String> names = menu.stream()
  .map(Dish::getName) // map 메서드를 getName 메서드로 파라미터화 해서 요리명을 추출한다.
  .collect(toList()); // 파이프라인 실행, 반복자 필요 X
```

내부반복을 이용하면 작업을 투명하게 병렬로 처리하거나, 더 최적화된 다양한 순서로 처리할 수 있게 된다. 



### 스트림 연산

다음은 이전에 등장했던 예제이다

```java
List<String> names = menu.stream() // 요리 리스트에서 스트림 열기
  .filter(dish -> dish.getCalories() > 300) // 중간 연산
  .map(Dish::getName) // 중간 연산
  .limit(3) // 중간 연산
  .collect(toList()); // 스트림 리스트로 반환, 스트림 닫기
```

-  `filter`, `map` , `limit` 는 서로 연결되며 파이프라인 형성
- `collect`로 파이프라인을 실행한 다음 스트림을 닫음

**중간 연산** : 연결할 수 있는 스트림 연산

**최종 연산** : 스트림을 닫는 연산



#### 중간 연산

`filter`나 `sorted` 같은 중간 연산은 다른 스트림을 반환한다. 

> *스트림은 게으르다(lazy)의 의미 :* 단말 연산을 스트림 파이프라인에 시작하기 전까지는 아무 연산도 수행하지 않는다는 의미

#### 최종 연산

스트림 파이프라인에서 결과를 도출한다. 보통 `List` ,`Integer` ,`void`등 스트림 이외의 결과가 반환된다.

```java
menu.stream().forEach(System.our::println); // menu에서 만든 스트림의 모든 요리 출력, void 반환
```

#### 스트림 이용하기

**[스트림 이용 과정]**

- 질의를 수행할 (컬렉션 같은) 데이터 소스
- 스트림 파이프라인을 구성할 중간 연산 연결
  - `filter`
  - `map`
  - `limit`
  - `sorted`
  - `distinct` 등
- 스트림 파이프라인을 실행하고 결과를 만들 최종 연산
  - `forEach`
  - `count`
  - `collect 등`



### 요약

- 연속된 요소의 데이터 처리 연산을 지원한다.
- 내부 반복을 지원한다, `filter`, `map`, `sorted` 같은 연산으로 반복을 추상화한다.
- 중간 연산
  - `filter`, `map` 등 스트림을 반환하는 연산. 파이프라인을 구성할 수 있지만 결과를 도출할 순 없다.
- 최종 연산
  - `forEach`, `count` 등 스트림을 반환하는 것이 아닌 결과를 반환하는 연산
- 스트림의 요소는 **게으르게** 계산된다.