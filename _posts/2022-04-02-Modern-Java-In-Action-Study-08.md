---
layout: post
title:  "모던 자바 인 액션 스터디 - chapter6-(1)"
date:   2022-04-02T00:00:00-00:00
author: sangyeop
categories: Sproutt-2nd



---



# 새싹 개발 서적 스터디 - 모던 자바 인 액션 Chapter6-(1)

이 장의 내용

- Collectors 클래스로 컬렉션을 만들고 사용하기
- 하나의 값으로 데이터 스트림 리듀스하기
- 특별한 리듀싱 요약 연산
- 데이터 그룹화와 분할
- 자신만의 커스텀 컬렉터 개발

이 장에서는 `reduce()`가 그랬던 것 처럼 `collect()` 역시 다양한 누적 방식을 인수로 받아서 스트림을 최종 결과로 도출하는 리듀싱 연산을 수행할 수 있음을 설명한다. 다양한 요소 누적 방식은 `Collector` 인터페이스에 정의되어 있다.

다음은 `collect()`와 컬렉터로 구현할 수 있는 질의 예제다.

- 통화별로 트랜잭션을 그룹화한 다음에 해당 통화로 일어난 모든 트랜잭션 합계를 계산하시오(`Map<Currency, Integer>` 반환).
- 트랜잭션을 비싼 트랜잭션과 저렴한 트랙잭션 두 그룹으로 분류하시오(`Map<Boolean, List<Tansaction>>` 반환 ).
- 트랜잭션을 도시 등 다수준으로 그룹화하시오. 그리고 각 트랜잭션이 비싼지 저렴한지 구분하시오(`Map<String, Map<Boolean, List<Transaction>>>` 반환).



**통화별 트랜잭션을 그룹화한 코드(명령형 버전)**

```java
// 그룹화한 트랜잭션을 저장할 맵
Map<Currency, List<Transaction>> transactionsByCurrencies = new HashMap<>();

// 트랜잭션 리스트를 반복
for(Transaction transaction : transactions) {
  Currency currency = transaction.getCurrency();
  List<Transaction> transactionsForCurrency = transactionsByCurrencies.get(currency);
  
  //현재 통화를 그룹화하는 맵에 항목이 없으면 항목을 만든다
  if(transactionsForCurrency == null) { 
    transactionsForCurrency = new ArrayList<>();
    transactionsForCurrencies.put(currency, transactionsForCurrency);
  }
  
  // 같은 통화를 가진 트랜잭션 리스트에 현재 탐색 중인 트랜잭션을 추가한다.
  transactionsForCurrency.add(transaction);
}
```

**통화별 트랜잭션을 그룹화한 코드(함수형 버전)**

```java
Map<Currency, List<Transaction>> transactionsByCurrencies = transactions.stream().collect(groupingBy(TransactionL::getCurrency));
```



### 컬렉터란 무엇인가?

이전 예제에서는 `collect()` 메서드로 `Collector` 인터페이스 구현을 전달했다. `Collector` 인터페이스 구현은 스트림의 요소를 어떤 식으로 도출할지를 결정한다.

앞선 장에서는 `toList()`를 이용해서 각 요소를 리스트로 만들어 `Collector` 인터페이스의 구현으로 사용했으며, 이번 장에서는 `groupingBy()`를 사용해서 각 키 버킷 그리고 각 키 버킷에 대응하는 요소 리스트를 값으로 포함하는 맵을 만들 수 있었다.



### 고급 리듀싱 기능을 수행하는 컬렉터

스트림에 `collect()`를 호출하면 스트림의 요소에 리듀싱 연산이 수행된다. 아래 그림은 내부적으로 **리듀싱 연산**이 일어나는 모습을 보여준다. `collect()`에서는 리듀싱 연산을 이용해서 스트림의 각 요소를 방문하면서 컬렉터가 작업을 처리한다.

보통 함수를 요소로 변환(`toList()` 처럼 데이터 자체를 변환하는 것보다는 데이터 저장 구조를 변환할 때가 많다) 할 때는 컬렉터를 적용하며 최종 결과를 저장하는 자료구조에 값을 누적한다. 

![CamScanner 04-01-2022 15 38_1](https://user-images.githubusercontent.com/78407939/161209061-63b27e88-ccd1-4391-a010-b44878abc48e.jpg)

`Collector` 인터페이스의 메서드를 어떻게 구현하느냐에 따라 스트림에 어떤 리듀싱 연산을 수행할지 결정된다. 가장 많이 사용하는 메서드로는 스트림의 모든 요소를 리스트로 수집하는 `toList()` 메서드가 있다.

```java
List<Transaction> transactions = transactionStream.collect(Collectors.toList());
```

#### 미리 정의된 컬렉터

`Collectors`에서 제공하는 메서드의 기능은 크게 세 가지로 구분할 수 있다.

- 스트림 요소를 하나의 값으로 리듀스하고 요약
- 요소 그룹화
- 요소 분할



### 리듀싱과 요약

`Collector` 팩토리 클래스로 만든 컬렉터 인스턴스로 어떤 일을 할 수 있는지 살펴본다.

- **예제**

  `counting()` 이라는 팩토리 메서드가 반환하는 컬렉터로 메뉴에서 요리 수를 계산한다.

  ```java
  long howManyDishes = menu.stream().collect(Collectors.counting());
  ```

  다음처럼 불필요한 과정을 생략할 수 있다

  ```java
  long howManyDishes = menu.stream().count();
  ```

#### 스트림값에서 최댓값과 최솟값 검색

메뉴에서 칼로리가 가장 높은 요리를 찾는다고 가정하자. `Collectors.maxBy`, `Collectors.minBy` 두개의 메서드를 이용해서 스트림의 최댓값과 최솟값을 계산할 수 있다. 두 컬렉터는 스트림의 요소를 비교하는데 사용할 `Comparator` 인수를 받는다.

```java
Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories); // 비교를 위한 Comparator 선언

Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(dishCaloriesComparator)); // Collectors.maxBy 로 전달
```

또한 스트림에 있는 객체의 숫자 필드의 합계나 평균 등을 반환하는 연산에도 리듀싱 기능이 자주 사용되는데, 이러한 연산을 **요약** 연산이라고 부른다.

#### 요약 연산

`Collectors` 클래스는 `Collectors.summingInt`라는 요약 팩토리 메서드를 제공한다. `summingInt`는 객체를 int로 매핑하는 함수를 인수로 받고, `summingInt`의 인수로 전달된 함수는 객체를 int로 매핑한 컬렉터를 반환한다. 그리고 `summingInt`가 `collect` 메서드로 전달되면 요약 작업을 수행한다.

```java
int totalCalories = menu.stream().collect(summingInt(Dish::getCalories)); // 메뉴 리스트 총 칼로리 계산
```

![CamScanner 04-01-2022 22 56n_1](https://user-images.githubusercontent.com/78407939/161278098-47e9653f-ddea-4fbe-a2a7-20de6b5f6004.jpg)

이 외에도 평균값 계산 등도 요약 기능으로 제공된다. 종종 이들 중 두 개 이상의 연산을 한 번에 수행해야 할 때도 있다. 이런 상황에서는 팩토리 메서드 `summarizingInt`가 반환하는 컬렉터를 사용할 수 있다. 

아래 코드를 실행하면 `IntSummaryStatistics` 클래스로 모든 정보가 수집된다.

```java
IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories)); 
```

`menuStatistics`를 출력하면 다음과 같은 정보를 확인할 수 있다.

```
IntSummaryStatistics{count=9, sum=4300, min=120, average=477.777778, max=800}
```

이 외에도 long이나 double에 대응하는 클래스들도 존재한다.

#### 문자열 연결

`joining` 팩토리 메서드를 이용하면 스트림의 각 객체에 `toString` 메서드를 호출해서 문자열을 연결해 반환한다.

```java
String shortMenu = menu.stream().map(Dish::getName).collect(joining());
```

`joining` 메서드는 내부적으로 `StringBuilder`를 이용해서 문자열을 하나로 만드는데, `Dish` 클래스가 요리명을 반환하는 `toString` 메서드를 가지고 있다면 `map`을 생략할 수 있다.

```java
String shortMenu = menu.stream().collect(joining());
```

두 코드 모두 아래와 같은 결과를 출력하지만, 구분자가 없어 이해하기 힘들다.

```
porkbeefchickenfrench friesriceseason fruitpizzaprawnssalmon
```

오버로드 된 `joining` 메서드를 이용하여 구분자를 지정해줄 수 있다.

```java
String shortMenu = menu.stream().collect(joining(", "));
```

```
pork, beef, chicken, french fries, rice, season fruit, pizza, prawns, salmon
```

#### 범용 리듀싱 요약 연산

현재까지 소개된 모든 컬렉터는 `reducing` 과 같은 범용 팩토리 메서드로도 정의할 수 있다. 이전 예제에서 범용 팩토리 메서드 대신 특화 컬렉터를 사용한 이유는 프로그래밍적 편의성(가독성) 때문이다. 다음과 같이 범용 팩토리 메서드로도 합계를 계산할 수 있다.

```java
int totalCalories = menu.stream().collect(reducing(0, Dish::getCalories, (i, j) -> i + j));
```

`reducing`의 인수

- 첫 번째 : 리듕신 연산의 **시작값** or 스트림에 인수가 없을 때 **반환값**
- 두 번째 : 요리를 칼로리 정수로 변환할 때 사용한 **변환 함수**
- 세 번째 : 같은 종류의 두 항목을 하나의 값으로 더하는 `BinaryOperator`, 여기선 int가 사용되었다.

다음과 같이 `Optional<T>`를 반환하는 한개의 인수를 갖는 `reducing` 메서드도 사용할 수 있다. 한 개의 인수를 갖는 `reducing`의 경우 시작값이 설정되지 않으므로, 빈 스트림이 들어왔을때 Null이 발생할 수 있으므로 `Optional<T>`가 반환된다.

```java
Optional<Dish> mostCalorieDish = menu.stream().collect(reducing(d1, d2) -> d1.getCalories() > d2.getCalories ? d1 : d2));
```



**collect 와 reduce**

`collect` 메서드는 도출하려는 결과를 누적하는 컨테이너를 바꾸도록 설계된 메서드인 반면 `reduce`는 두 값을 하나로 도출하는 불변형 연산이라는 점에서 의미론적인 문제가 일어난다. 의미론적 문제는 곧 실용적 문제로도 이어지는데 여러 스레드가 동시에 같은 데이터 구조체를 고치면 리스트 자체가 망가져 버리므로 리듀싱 연산을 병렬로 수행할 수 없다는 문제가 생긴다.

**컬렉션 프레임워크 유연성 : 같은 연산도 다양한 방식으로 수행할 수 있다**

`reducing` 컬렉터를 사용한 이전 예제에서 람다 표현식 대신 `Integer` 클래스의 `sum` 메서드 참조를 이용하면 더 간단하게 표현할 수 있다

```java
int totalCalories = menu.stream().collect(reducing(0, Dish::getCalories, (i, j) -> i + j)); // 기존
int totalCalories = menu.stream().collect(reducing(0, Dish::getCalories, Integer::sum); // 단순화
```

![CamScanner 04-02-2022 00 32n_1](https://user-images.githubusercontent.com/78407939/161295418-73ba23c6-c6c5-4fc1-8409-b7c308091869.jpg)

위 그림은 리듀싱 연산 과정을 표현한 그림이다.

- 누적자 초깃값으로 초기화
- 합계 함수를 이용해서 각 요소에 변환 함수를 적용한 결과를 반복적으로 조합

> *제네릭 와일드카드(?)란? :* `?`는 컬렉터의 누적자 형식이 알려져 있지 않았음을, 즉 누적자의 형식이 자유로움을 의미한다.



### 그룹화

팩토리 메서드 `Collectors.groupingBy`를 이용해서 쉽게 메뉴를 그룹화할 수 있다.

```java
Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));
```

```
{FISH=[prawns, salmon], OTHER=[french fries, rice, season fruit, pizza], MEAT=[pork, beef, chicken]}
```

이 함수를 기준으로  스트림이 그룹화 되므로 이를 **분류 함수**라고 부른다. 아래 그림에서 보여주는 것처럼 그룹화 연산의 결과로 그룹화 함수가 반환하는 키 그리고 각 키에 대응하는 모든 항목 리스트를 값으로 갖는 맵이 반환된다.

![CamScanner 04-02-2022 13 23n_1](https://user-images.githubusercontent.com/78407939/161366149-53e6e23c-7c67-489f-b3b0-268411e0d6c2.jpg)

더 복잡한 분류 기준이 필요할 경우에는 `Dish` 클래스에 해당 분류 기준이 메서드로 정의 되어 있지 않으므로, 메서드 참조 대신 람다를 사용하는 것이 바람직하다.

지금까지 메뉴의 요리를 종류 또는 칼로리로 그룹화하는 방법을 살펴봤다. 그러면 요리 종류와 칼로리 두 가지 기준으로 동시에 그룹화 하려면 어떻게 해야 할까?

#### 그룹화된 요소 조작

요소를 그룹화 한 다음 각 결과 그룹의 요소를 조작하는 연산이 필요하다.

500칼로리가 넘는 요리만 필터한다고 가정하자. 

```java
Map<Dish.Type, List<Dish>> caloricDishesByType = menu.stream()
.filter(dish -> dish.getCalories() > 500)
.collect(groupingBy(Dish::getType));
```

하지만 단점이 존재한다. 이번 메뉴 요리에서는 다음처럼 맵 형태로 되어 있으므로 이 코드에 위 기능을 사용하려면 맵에 코드를 적용해야 한다.

```
{OTHER=[french fries, pizza], MEAT=[pork, beef]}
```

우리의 필터 프레디케이트를 만족하는  FISH 종류 요리는 없으므로 결과 맵에서 해당 키 자체가 사라지기 때문에 문제가 생긴다. 다음처럼 두 번째 `Collector` 안으로 필터 프레디케이트를 이동시켜 문제를 해결할 수 있다.

```java
Map<Dish.Type, List<Dish>> caloricDishesByType = menu.stream()
  .collect(groupingBy(Dish::getType, 
                      filtering(dish -> dish.getCalories() > 500, toist())));
```

다음을 실행하면 목록이 비어 있는 FISH도 항목으로 추가 된다.

- [ ] ```
  {OTHER=[french fries, pizza], MEAT=[pork, beef], FISH=[]}
  ```

그룹화된 항목을 조작하는 유용한 기능 중 하나로 맵핑 함수를 이용해 요소를 변환하는 작업이 있다. `filtering`과 같이 각 항목에 적용한 함수를 모으는 데 사용하는 또 다른 컬렉터를 인수로 받는 `mapping` 메서드를 제공한다.

```java
Map<Dish.Type, List<String>> dishNamesByType = menu.stream()
  .collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));
```

이저 예제와 다르게 각 그룹은 `Dish`가 아니라 `String` 리스트가 된다. 

`groupingBy`와 연계에서 세 번째 컬렉터를 사용해서 일반 맵이 아닌 `flatMap`변환을 수행할 수 있다. 이 경우에는 `flatMapping`을 이용하면 각 형식의 요리 태그를 간편하게 추출할 수 있다

```java
Map<Dish.Type, Set<String>> dishNamesByType = menu.stream()
  .collect(groupingBy(Dish::getType, flatMapping(dish -> dishTags.get(dish.getName()).stream(), toSet())));
```

#### 다수준 그룹화

`Collectors.groupingBy`는 일반적인 분류 함수와 컬렉터를 인수로 받는다. 바깥 `groupingBy` 메서드에 두 번째 기준을 정의하는 `groupingBy`를 정의해서 두 수준으로 스트림 항목을 그룹화할 수 있다.

```java
Map<Dish.Type, Map<CaloricLevel, Lish<Dish>>> dishesByTypeCaloricLevel = menu.stream().collect(groupingBy(Dish::getType, groupingBy(dish -> 
{
  if(dish.getCalories() <= 400) 
     return CaloricLevel.DIET;
  else if (dish.getCalories() <= 700)
     return CaloricLevel.NORMAL; 
  else return CaloricLevel.FAT;     
})));
                                                                                                          
                                                                                                          
```

이 결과로 아래와 같은 두 수준의 맵이 만들어진다.

```
{MEAT={DIET=[chicken], NORMAL=[beef], FAT=[pork]}, FISH={DIET=[prawns], NORMAL=[salmon]}, OTHER={DIET=[rice, seasonal fruit], NORMAL=[french fries, pizza]}}
```

첫 번째 분류 키값으로 `fish, meat, other`를 갖고, 두 번째 분류 키값으로는 `normal, diet, fat`을 갖는다. 위 예제와 같은 꼴로 n수준 맵을 만들 수 있다.

#### 서브그룹으로 데이터 수집

다음 코드처럼 `groupingBy` 컬렉터에 두 번째 인수로 `counting` 컬렉터를 전달해서 메뉴에서 요리의 수를 종류별로 계산할 수 있다.

```java
Map<Dish.Type, Long> typesCount = menu.stream().collect(groupingBy(Dish::getType, counting()));
```

```
{MEAT=3, FISH=2, OTHER=4}
```

> `groupingBy(f)`는 사실 `groupingBy(f, toList())`의 축약형이다

요리의 **종류**를 분류하는 컬렉터로 메뉴에서 가장 높은 칼로리를 가진 요리를 찾는 프로그램도 다시 구현할 수 있다.

```java
Map<Dish.Type, Optional<Dish>> mostCaloricByType = menu.stream().collect(groupingBy(Dish::getType,
                                                                                   maxBy(comparingInt(Dish::getCalories))));
```

```
{FISH=Optional[salmon], OTHER=Optional[pizza], MEAT=Optional[pork]}
```



### 컬렉터 결과를 다른 형식에 적용하기

마지막 그룹화 연산에서 맵의 모든 값을 `Optional`로 감쌀 필요가 없으므로 `Optional`을 삭제할 수 있다.

```java
Map<Dish.Type, Dish> mostCaloricByType = menu.stream()
  .collect(groupingBy(Dish::getType, // 분류 함수
                      collectingAndThen(maxBy(comparingInt(Dish::getCalories)), // 감싸인 컬렉터
                                        Optional::get))); // 변환 함수
```

`collectingAndThen`은 적용할 컬렉터와 변환 함수를 인수로 받아 다른 컬렉터를 반환한다. 반환되는 컬렉터는 기존 컬렉터의 래퍼 역할을 하며 `collect`의 마지막 과정에서 변환 함수로 자신이 반환하는 값을 매핑한다. 여기서는 `maxBy`로 마들어진 컬렉터가 감싸지는 컬렉터며 변환 함수  `Optional::get`으로 반환된 `Optional`에 포함된 값을 추출한다.

```
{Fish=salmon, OTHER=pizza, MEAT=pork}
```

![CamScanner 04-02-2022 15 08n_1](https://user-images.githubusercontent.com/78407939/161369340-9fe0faba-97ce-4dd0-a8e3-b875a6c5eb85.jpg)

중첩 컬렉터는 외부 계층에서 안쪽으로 다음과 같은 작업이 수행된다. 

- 컬렉터는 점선으로 표시되어 있으며 `groupingBy`는 가장 바깥쪽에 위치하면서 요리의 종류에 따라 메뉴 스트림을 세 개의 서브스트림으로 그룹화한다.
- `groupingBy` 컬렉터는 `collectingAndThen` 컬렉터를 감싼다. 따라서 두 번째 컬렉터는 그룹화된 세 개의 서브스트림에 적용된다.
- `collectingAndThen` 컬렉터는 세 번째 컬렉터 `maxBy`를 감싼다.
- 리듀싱 컬렉터가 서브스트림에 연산을 수행한 결과에 `collectingAndThen`의 `Optional::get` 변환 함수가 적용된다.
- `groupingBy` 컬렉터가 반환하는 맵의 분류 키에 대응하는 세 값이 각각의 요리 형식에서 가장 높은 칼로리다.

**groupingBy와 함께 사용하는 다른 컬렉터 예제**

- 메뉴에 있는 모든 요리의 칼로리 합계를 구하려고 만든 컬렉터 재사용

```java
Map<Dish.Type, Integer> totalCaloricByType = menu.stream()
  .collect(groupingBy(Dish::getType, summingInt(Dish::getCalories)));
```

- `groupingBy`와 `mapping` 메서드 사용

  각 요리 형식에 존재하는 모든 `CaloricLevel`값을 알고 싶을때

```java
Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType = menu.stream()
  .collect(groupingBy(Dish::getType, mapping(dish -> {
  if(dish.getCalories() <= 400 return CaloricLevel.DIET;
    else if(dish.getCalories() <= 700) return CaloricLevel.NORMAL;
    else return CaloricLevel.FAT;}, toSet() )));
```

`mapping`메서드에 전달한 변환 함수는 `Dish`를 `CaloricLevel`로 매핑한다. 그리고 `CaloricLevel` 결과 스트림은 (`toList`와 비슷한) `toSet` 컬렉터로 전달되면서 리스트가 아닌 집합으로 스트림의 요소가 누적된다. 이전과 마찬가지로 그룹화 함수로 생성된 서브스트림에 `mapping`함수를 적용하면서 다음과 같은 결과가 나온다.

```
{OTHER=[DIET, NORMAL], MEAT=[DIET, NORMAL, FAT], FISH=[DIET, NORMAL]}
```

이전 예제에서는 `Set`의 형식이 정해져 있지 않았는데, `toCollection`을 이용하면 형식을 제어할 수 있다.

```java
menu.stream().collect(groupingBy(Dish::getType, mapping(dish -> {
  if(dish.getCalories() <= 400 return CaloricLevel.DIET;
    else if(dish.getCalories() <= 400) return CaloricLevel.NORMAL;
    else return CaloricLevel.FAT;
}, toCollection(HashSet::new) )));
```

