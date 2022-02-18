---
layout: post
title:  "모던 자바 인 액션 스터디 - chapter2"
date:   2022-02-18T00:00:00-00:00
author: sangyeop
categories: Sproutt-2nd






---

# 새싹 개발 서적 스터디 - 모던 자바 인 액션 Chapter2



이 장의 내용

- 변화하는 요구사항에 대응

- 동작 파라미터화

- 익명 클래스

- 람다 표현식 미리보기

- 실전 예제 : Comparator, Runnable, GUI

  

## 동작 파라미터화 코드 전달하기

시시각각 변하는 사용자의 요구사항에 어떻게 대응하는게 좋을까? 당연히 엔지니어링적 시간과 비용이 적게 들도록 대응하는 것이 좋을 것이다. 또한 새로 추가되는 기능은 쉽게 구현할 수 있어야 하며 장기적인 관점에서 유지보수가 쉬워야한다.

**동작 파라미터화**를 이용하면 자주 바뀌는 요구사항에 효과적으로 대응할 수 있다. 동작 파라미터화란 아직은 어떻게 실행할 것인지 결정하지 않은 코드 블록을 의미한다. 이 코드의 블록의 실행은 나중으로 미뤄진다. 예를 들면 나중에 실행될 메서드의 인수로 코드 블록을 전달할 수 있다. 이렇게 되면 코드 블록에 따라 메서드의 동작이 파라미터화 된다.

예를 들어서 룸메이트는 차로 운전해서 슈퍼에 들렸다가 집에 돌아오는 길을 알고 있다. 그래서 룸메이트에게 심부름을 부탁하고, 이 동작은 `goAndBuy()` 라는 메서드를 호출하면서 사려는 물품을 파라미터로 제공한다. 그런데 만약 우체국에서 소포를 받아와 달라는 부탁을 해야하는데, 룸메이트는 우체국에서 소포를 가져오는 방법을 모른다고 하자. 이럴 경우 좀더 포괄적인 메서드인 `go()`라는 메서드에 원하는 동작(''우체국에 가서, 이 고객 번호를 사용하고, 관리자에게 이야기 한 다음에, 소포를 가져오면 된다'')을 상세하게 담아 메서드의 파라미터로 넘겨 줌으로써 요청을 보낼 수 있다.



#### 변화하는 요구사항에 대응하기

------

사과농장 재고목록 애플리케이션에 **녹색** 사과만 필터링하는 기능을 추가한다고 가정하자.

- **첫 번째 시도**

  ```
  enum Color { RED, GREEN }
  ```

  ```java
  public static List<Apple> filterGreenApples(List<Apple> inventory) {
    List<Apple> result = new ArrayList<>();					// 사과 누적 리스트
    for(Apple apple : inventory) {
    	if(GREEN.equals(apple.getColor())) {				// 녹색 사과만 선택
      	result.add(apple);
      }
    }
  }
  ```

  이렇게 필터링을 주었는데 만약 나중에 **빨간** 사과를 필터링 하고 싶어진다면 어떻게 할까? 아마도 `filterRedApples()` 라는 새로운 메서드를 정의하고 조건문을 빨간색에 맞게 바꿔줄 것이다. 그러나 이런 식이라면 필터링은 가능하겠지만 더 **다양한 색**(옅은 녹색, 어두운 빨간색, 노란색 등)이 요구될 경우 변화에 대응할 수 없다.

  > *거의 비슷한 코드가 반복 존재한다면 그 코드를 추상화 시킨다*

- **두 번째 시도**

  그렇다면 어떻게 `filterGreenApples()` 코드를 반복하지 않고 `filterRedApples()` 를 구현할 수 있을까? 색을 **파라미터화** 할 수 있도록 **메서드에 파라미터를 추가**하면 변화하는 요구사항에 좀 더 유연하게 대응하는 코드를 만들 수 있다.

  ```java
  public static List<Apple> filterApplesByColor(List<Apple> inventory, Color color) {
    List<Apple> result = new ArrayList<>();					// 사과 누적 리스트
    for(Apple apple : inventory) {
    	if(apple.getColor().equals(color)) {				// 파라미터로 지정된 색(color)와 같으면 선택
      	result.add(apple);
    	}
    }
  }
  ```

  위 메서드를 다음과 같이 호출할 수 있다

  ```java
  List<Apple> greenApples = filterApplesByColor(inventory, GREEN);
  List<Apple> redApples = filterApplesByColor(inventory, RED);
  ```

  그런데 만약 여기서 요구사항이 색 이외에도 가벼운 사과와 무거운 사과 등으로 **무게**를 이용해 구분해달라는 요구사항이 추가되었다고 하자. 여기에 대응할 수 있도록 다음 메서드도 추가 했다.

  ```java
  public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
  	List<Apple> result = new ArrayList<>();
  	for(Apple apple : inventory) {
  		if(apple.getWeight() > weight) {
  			result.add(apple);
  		}
  	}
  }
  ```

  위와 같이 메서드를 추가하는 것도 좋은 해결책이라고 할 수 있지만, 각 사과에 필터링 조건을 적용하는 부분의 코드가 색 필터링 코드와 대부분 **중복**됨을 눈치 챌 수 있다. 소프트웨어공학의 **DRY**(Don't Repeat Yourself) 원칙을 어기는 것이다.

- **세 번째 시도 : 가능한 모든 속성으로 필터링**

  모든 속성을 메서드 파라미터로 추가한 경우이다. 최악의 경우이므로 예제는 생략하겠다.

  

#### 동작 파라미터화

------

앞서서 등장한 세 가지 시도 방법을 통해서 파라미터 추가가 아닌 변화하는 요구사항에 더 유연하게 대응할 수 있는 방법이 절실하다는 것을 확인했다.  공통적으로 `if`문을 사용하면서 어떤 선택 조건에 해당 하는지를 `boolean` 값을 통해서 확인하고 있다. 참 또는 거짓을 반환하는 함수를 **프레디케이트**라고 한다. **선택 조건을 결정하는 인터페이스**를 정의하자.

```java
public interface ApplePredicate {
	boolean test (Apple apple);								// 참 거짓을 판별하는 코드가 반복되고 있으므로 추상화 한다.
}
```

```java
public class AppleHeavyWeightPredicate implements ApplePredicate {			// 무거운 사과만 선택
	public boolean test(Apple apple) {
		return apple.getWeight > 150;
	}
}
```

```java
public class AppleGreenColorPredicate implements ApplePredicate {				// 초록 사과만 선택
	public boolean test(Apple apple) {
		return GREEN.equals(apple.getColor());
	}
}
```

이렇게 구성하면 위 조건에 따라서 filter 메서드가 다르게 동작할 것을 예상할 수 있다. 이를 **전략 디자인 패턴** 이라고 부른다. 

> *전략디자인 패턴 : 각 알고리즘(전략이라고 불리는)을 캡슐화하는 알고리즘 패밀리를 정의해둔 다음, 런타임에 알고리즘을 선택하는 기법이다*

여기서는 `ApplePredicate` 가 알고리즘 패밀리가 되고, `AppleHeavyWeightPredicate`, `AppleGreenColorPredicate`가 알고리즘(전략)이 된다.

그렇다면 어떻게 `ApplePredicate`는 다양한 동작을 수행할 수 있을까? `filterApples()` 메서드에서 `ApplePredicate` 객체를 받아서 사과의 조건을 검사하도록 조건문을 고쳐야 한다. 이렇게 **동작 파라미터화**, 즉 메서드가 다양한 동작( 또는 전략 ) 을 **받아서** 내부적으로 다양한 동작을 **수행** 할 수 있다.

- **네 번째 시도 : 추상적 조건으로 필터링**

  ```java
  public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
  	List<Apple> result = new ArrayList<>();
  	for(Apple apple : inventory) { 
  		if(p.test(apple)) {								// ApplePredicate 객체로 감싸서 test()메서드 전달
  			result.add(apple);
  		}
  	}
    return result;
  }
  ```

  만약 여기 추가적으로 농부가 **150그램이 넘고** 색깔이 **빨간** 사과를 검색해달라고 부탁하면 `ApplePredicate`를 적절하게 구현하는 클래스만 만들면 된다. 

  ```java
  public class AppleRedAndHeavyPredicate implements ApplePredicate {
  	public boolean test(Apple apple) {
  		return RED.equals(apple.getColor()) && apple.getWeight() > 150;
  	}
  }
  ```

  그리고 다음과 같이 사용하면 된다

  ```java
  List<Apple> redAndHeavyApples = filterApples(inventory, new AppleRedAndHeavyPredicate());
  ```

  **우리가 전달한** `ApplePredicate` 객체에 의해 `filterApples()` 메서드의 동작이 결정된다. 우리는 `filterApples()` 메서드의 동작을 파라미터화 한 것이다. 

  여기서 가장 중요한 구현은 `test()` 메서드이다. 안타깝게도 메서드는 객체만 인수로 받으므로 `test()` 메서드를 `ApplePredicate` 객체로 감싸서 전달해야한다. 그러나 `test()`메서드를 구현하는 객체를 이용해서 `boolean` 표현식 등을 전달할 수 있으므로 **'코드를 전달'** 할 수 있는 것이나 다름 없다.

  그러나 여러 클래스를 구현해서 인스턴스화 하는 과정이 조금 귀찮아 보일 수 있다. 이를 어떻게 개선하는지 확인해보자

  

#### 복잡한 과정 간소화

------

- **익명 클래스**

  익명 클래스란 자바의 블록 내부에 선언된 클래스를 의미한다. 즉 이름이 없는 클래스이다. 익명 클래스를 이용하면 클래스 선언과 인스턴스화를 동시에 할 수 있다.

- **다섯 번째 시도 : 익명 클래스 사용**

  다음은 익명 클래스를 이용해서 `ApplePredicate`를 구현하는 객체를 만드는 방법으로 필터링 예제를 다시 구현한 코드이다

  ```java
  List<Apple> redApples = filterApples(inventory, new ApplePredicate() {
  	public boolean test(Apple apple) {
  		return RED.equals(apple.getColor());
  	}
  })
  ```

  그러나 위 코드를 보면 알 수 있듯이 여전히 익명클래스를 사용해도 **많은 공간**을 차지한다. 또한 프로그래머가 익명 클래스를 이해하고 사용하는데 익숙하지 않은 문제를 발생시킨다.

  한눈에 이해할 수 있어야 좋은 코드다.

- **여섯 번째 시도 : 람다 표현식 사용**

  자바 8의 람다 표현식을 이용해서 위 코드를 다음과 같이 바꿀 수 있다

  ```java
  List<Apple> redApples = filterApples(inventory, (Apple apple) -> RED.equals(apple.getColor()));
  ```

- **일곱 번째 시도 : 리스트 형식으로 추상화**

  ```java
  public interface Predicate<T> {
  	boolean test(T t);
  }
  ```

  ```java
  public static <T> List<T> filter(List<T> list, Predicate<T> p) {
  	List<T> result = new ArrayList<>();
  	for(T e : list) {
  		if(p.test(e)) {
  			result.add(e);
  		}
  	}
    return result;
  }
  ```

  다음은 람다 표현식을 이용해 위의 메서드를 사용한 예제이다.

  ```java
  List<Apple> redApples = filter(inventory, (Apple apple) -> RED.equals(apple.getColor()));
  List<Integer> evenNumbers = filter(numbers, (Integer i) -> i % 2 == 0);
  ```

  이렇게 해서 자바 8을 이용하여 유연성과 간결함 이라는 두 마리 토끼를 모두 잡을 수 있었다.

