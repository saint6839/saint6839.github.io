---
layout: post
title:  "모던 자바 인 액션 스터디 - chapter8"
date:   2022-05-09T00:00:00-00:00
author: sangyeop
categories: Sproutt-2nd


---



# 새싹 개발 서적 스터디 - 모던 자바 인 액션 Chapter8

## 컬렉션 API 개선

이 장의 내용

- 컬렉션 팩토리 사용하기
- 리스트 및 집합과 사용할 새로운 관용 패턴 배우기
- 맵과 사용할 새로운 관용 패턴 배우기



### 컬렉션 팩토리

자바에서 적은 요소를 포함하는 리스트를 만드는 경우를 살펴보자. 다음은 휴가를 함께 보내려는 친구 이름을 포함하는 그룹을 만드는 예제이다.

```java
List<String> friends = new ArrayList<>();
friends.add("Raphael");
friends.add("Olivia");
friends.add("Thibaut");
```

위의 코드는 `Arrays.asList()` 팩토리 메서드를 사용하여 아래와 같이 줄일 수 있다.

```java
List<String> friends = Arrays.asList("Raphael", "Olivia", "Thibaut");
```

이는 고정된 크기의 리스트를 생성할때는 문제가 되지 않지만, 만약 여기에 추가적으로 `add()` 메서드를 수행하려 하면 `UnsupportedOperationException`이 발생한다.

**UnsupportedOperationException 예외 발생**

내부적으로 고정된 크기의 변환할 수 있는 배여로 구현되었기 때문에 이와 같은 일이 발생한다.

그렇다면 집합을 생성해보자. 다음과 같이 리스트를 인수로 받는 `HashSet` 생성자를 사용할 수 있다.

```java
Set<String> friends = new HashSet<>(Arrays.asList("Raphael", "Olivia", "Thibaut"));
```

또는 다음과 같이 스트림을 이용해 생성할 수 있다.

```java
Set<String> friends = Stream.of("Raphael", "Olivia", "Thibaut").collect(Collectors.toSet());
```

이 두 방법은 모두 불필요한 객체 할당을 필요로 한다.

#### 리스트 팩토리

`List.of()` 팩토리 메서드를 이용해 간단하게 리스트를 만들 수 있다.  (`List.of()`로 만든 컬렉션은 바꿀 수 없다.)

```java
List<String> friends = List.of("Raphael", "Olivia", "Thibaut");
friends.add("Chih-Chun");
```

그러나 위 코드를 실행하면 `add()`에서 `UnsupportedOperationException`이 발생한다. 사실 변경할 수 없는 리스트가 만들어졌기 때문이다. 이것이 꼭 나쁜것만을 의미하지는 않는다. 컬렉션이 의도치 않게 변하는 것을 막을 수 있기 때문이다.

> **오버로딩 vs 가변 인수**
> List 인터페이스에는  `List.of()` 의 다양한 오버로드 버전이 있음을 알 수 있다.
>
> ```java
> static <E> List<E> of();
> static <E> List<E> of(E e1);
> static <E> List<E> of(E e1, E e2);
> ...
> ```
>
> 왜 자바는 다음과 같이 다중 요소를 받을 수 있도록만 구현하지 않았을까?
>
> ```java
> static <E> List<E> of(E... elements);
> ```
>
> 이러한 가변 인수 버전은 추가 배열을 할당해서 리스트로 감싸는데, 이 과정에서 추가 비용이 발생하기 떄문에, 자바에서는 고정된 숫자 요소를 API로 정의하여 사용한다.  단,`List.of()`로 열 개 이상의 요소를 가진 리스트를 만들때는 가변 인수를 이용하는 메서드가 사용된다. 

#### 집합 팩토리

`List.of()`와 비슷한 방법으로 바꿀 수 없는 집합을 만들 수 있다.

```java
Set<String> friends = Set.of("Raphael", "Olivia", "Thibaut");
System.out.println(friends); // [Raphael, Olivia, Thibaut]
```

#### 맵 팩토리

맵을 만들려면 key와 value가 있어야하기 때문에 인자를 번갈아가며 작성하는 방법으로 만들 수 있다.

- 열 개 이하의 쌍을 만들 경우

```java
Map<String, Integer> ageOfFriends = Map.of("Raphael", 30, "Olivia", 25, "Thibaut", 26);
System.out.println(ageOfFriends); // {Olivia=25, Raphael=30, Thibaut=26}
```

- 그 이상일 경우

```java
Map<String, Integer> ageOfFriends = Map.ofEntries(entry("Raphael", 30),
                                                 entry("Olivia", 25),
                                                 entry("Thibaut", 26));
System.out.println(ageOfFriends); // {Olivia=25, Raphael=30, Thibaut=26}
```



### 리스트와 집합 처리

- `removeIf()`

  프레디케이트를 만족하는 요소를 제거한다.

- `replaceAll()`

  `UnaryOperator` 함수를 이용해 요소를 바꾼다.

- `sort()`

  리스트를 정렬한다.

새로운 결과를 만드는 스트림 동작과는 다르게 위 메서드들은 기존의 컬렉션을 바꾼다. 

#### removeIf 메서드

다음은 숫자로 시작되는 참조 코드를 가진 트랜잭션을 삭제하는 코드이다.

```java
for(Transaction transaction : transactions) {
  if(Character.isDigit(transaction.getReferenceCode().charAt(0))) {
    transactions.remove(transaction);
  }
}
```

위 코드는  `ConcurrentModificationException`을 일으킨다. `for-each`는 `Iterator` 객체를 사용하므로 아래와 같다.

```java
for(Iterator<Transaction> iterator = transactions.iterator();
    iterator.hasNext(); ) {
  Transaction transaction = iterator.next();
  if(Character.isDigit(transaction.getReferenceCode().charAt(0))) {
    transactions.remove(transaction); // 반복하면서 별도의 두 객체를 통해 컬렉션을 바꾸고 있으므로 문제가 된다.
  }
}
```

여기서 별도의 두 객체란 다음을 의미한다.

- `Iterator` 객체, `next()`, `hasNext()`를 이용해 소스를 질의한다.
- `Collection` 객체, `remove()`를 호출해 요소를 삭제한다.

이는 반복자의 상태가 컬렉션의 상태와 서로 동기화되지 않음을 의미한다. 이는 `Iterator`객체를 명시적으로 선언하고, 해당 객체의 `remove()`를 호출함으로써 해결할 수 있다.

```java
for(Iterator<Transaction> iterator = transactions.iterator();
   iterator.hasNext(); ) {
  Transaction transaction = iterator.next();
  if(Character.isDigit(transaction.getReferenceCode().charAt(0))) {
   iterator.remove(); 
  }
}
```

이를  `removeIf()` 메서드로 개선할 수 있다.

```java
transactions.removeIf(transaction -> Character.isDigit(transaction.getReferenceCode().charAt(0)));
```

#### replaceAll 메서드

리스트의 각 요소를 새로운 요소로 모두 바꿀 수 있는 메서드이다.

```java
referenceCodes.stream().map(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1))
  .collect(Collectors.toList())
  .forEach(System.out::println);
```

그러나 위 코드는 새 문자열 컬렉션을 만든다. 아래와 같이 기존 컬렉션의 요소를 바꾸도록 할 수 있다.

```java
for(ListIterator<String> iterator = referenceCodes.listIterator();
   iterator.hasNext(); ) {
  String code = iterator.next();
  iterator.set(Character.toUpperCase(code.charAt(0)) + code.subString(1));
}
```

이를 자바 8의 기능을 이용하면 다음과 같이 간단하게 바꿀 수 있다.

```java
referenceCodes.replaceAll(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1));
```



### 맵 처리

#### forEach 메서드

```java
for(Map.Entry<String, Integer> entry: ageOfFriends.entrySet()) {
  String friend = entry.getKey();
  Integer age = entry.getValue();
  System.out.println(friend + " is " + age + " years old");
}
```

맵은 위와 같이 반복자를 이용해 맵의 요소를 반복 출력할 수 있다. 이는 자바8 에서 아래와 같이 개선이 가능하다.

```java
ageOfFriends.forEach((friend, age) -> System.out.println(friend + " is " + age + " years old"));
```

#### 정렬 메서드

- `Entry.comparingByValue`

  값을 기준으로 정렬

- `Entry.comparingByKey`

  키를 기준으로 정렬

> **HashMap 성능**
>
> 기존에는 맵의 항목은 키로 생성한 해시코드로 접근할 수 있는 버켓에 저장했다. 많은 키가 같은 해시코드를 반환하는 상황이 되면 O(n) 시간이 걸리는 `LinkedList`로 버킷을 반환해야 하므로 성능이 저하된다. 
>
> 최근에는 버켓이 커질 경우 O(log(n)) 시간이 소요되는 정렬된 트리를 이용해 동적으로 치환해 충돌이 일어나는 요소 반환 성능을 개선했다.(단, `Comparable` 형태에만 해당)

#### getOrDefault 메서드

기존에는 찾으려는 키가 존재하지 않으면 `NPE`가 반환되므로, 요청 결과가 `null` 인지 확인해야만 했다. 이를 요청 값이 `null`일 경우 default value를 반환하도록 하여 문제를 해결할 수 있다. 이 메서드는 첫 번째 인수로 key를 두 번째 인수로 value를 받는데, 만약 key가 존재하지 않으면 value에 지정한 값을 반환하도록 한다.

```java
Map<String, String> favouriteMovies = Map.ofEntries(entry("Raphael", "Star Wars"),
                                                   entry("Olivia", "James Bond"));

System.out.println(favouriteMovies.getOrDefault("Thibaut", "Matrix")); // Thibaut 이 없으므로 Matrix를 반환한다.
```

#### 계산 패턴

맵에 키가 존재하는지 여부에 따라 어떤 동작을 실행하고, 결과를 저장해야 하는 상황이 필요한 때가 있다. 다음 세 가지 메서드가 이럴 경우 도움이 된다.

- `computeIfAbsent`

  제공된 키에 해당하는 값이 없으면, 키를 이용해 새 값을 계산하고 맵에 추가한다.

- `computeIfPresent`

  제공된 키가 존재하면 새 값을 계산하고 맵에 추가한다.

- `compute`

  제공된 키로 새 값을 계산하고 맵에 저장한다.

정보를 캐시할 경우 `computeIfAbsent`를 활용할 수 있다. 기존에 이미 데이터를 처리했다면 같은 값을 처리할 필요가 없기 때문이다.

그렇다면 여러 값을 저장하는 맵을 처리할 경우에는 어떤식으로 사용할 수 있을까? 다음은 Raphael 에게 줄 영화 목록을 만드는 예제이다.

```java
String friend = "Raphael";
List<String> movies = friendsToMovies.get(friend);
if(movies == null) { // 리스트 초기화 확인
  movies = new ArrayList<>();
  friendsToMovies.put(friend, movies);
}
movies.add("Star Wars"); // 영화 추가
System.out.println(friendsToMovies);
```

`computeAbsent()`는 키가 존재하지 않으면 값을 계산해 맵을 추가하고 키가 존재하면 기존 값을 반환한다. 이를 이용해 위 예제를 다음과 같이 구현할 수 있다.

```java
friendsToMovies.computeIfAbsent("Raphael", name -> new ArrayList<>()).add("Star Wars");
```

#### 삭제 패턴

다음과 같이 간결하게 맵에서의 삭제를 구현할 수 있다.

```java
favouriteMovies.remove(key, value);
```

#### 교체 패턴

- `replaceAll()` 

  `BiFunction`을 적용한 결과로 각 항목의 값을 교체한다. `List`의 `replaceAll()`과 유사하다.

- `replace()`

  키가 존재하면 맵의 값을 바꾼다.

```java
Map<String, String> favouriteMovies = new HashMap<>();
favouriteMovies.put("Raphael", "Star Wars");
favouriteMovies.put("Olivia", "james bond");
favouriteMovies.replaceAll((friend, movie) -> movie.toUpperCase());
System.out.println(favouriteMovies); // {Oliva=JAMES BOND, Raphael=STAR WARS}
```

#### 합침

다음은 두그룹의 연락처를 포함하는 두 맵을  `putAll()` 을 이용하여 합치는 예제이다. 

```java
Map<String, String> family = Map.ofEntries(entry("Teo", "Star Wars")
                                          ,entry("Cristina", "James Bond"));
Map<String, String> friendns = Map.ofEntries(entry("Raphel", "Star Wars"));
Map<String, String> everyone = new HashMap<>(family);
everyone.putAll(friends); // friends의 모든 항목을 everyone에 복사
System.out.println(everyone); // {Cristina=James Bond, Raphael=Star Wars, Teo=Star Wars}
```

중복 키가 없는 경우에는 충돌없이 잘 작동한다. 그러나 만약 같은 키로 다른 값을 가지고 있는 맵이 두개가 합쳐질 경우 문제가 발생한다. 이를  `forEach`와 `merge`멕서드를 이용하여 해결할 수 있다.

```java
Map<String, String> everyone = new HashMap<>(family);
friends.forEach((k,v) -> everyone.merge(k, v, (movie1, movie2) -> movie1 + " & " + movie2)); // 중복된 키 있을 경우 두 값을 연결한다.
System.out.println(everyone);
```

> 키와 연관된 값이 null일 경우 `merge`는 키가 널이 아닌 값과 연결한다. 또는 연결된 값을 주어진 매핑 함수의 결과 값으로 대치하거나, 결과가 null이면 항목을 제거한다.



### 개선된  ConcurrentHashMap

`ConcurrentHashMap` 클래스는 동시성 친화적이며 최신 기술을 반영한 `HashMap`이다. 연산 성능이 뛰어나다는 특징이 있다.

#### 리듀스와 검색

`ConcurrentHashMap`은 세 가지 연산을 지원한다.

- `forEach`

  각 쌍에 주어진 액션을 실행

- `reduce`

  모든 쌍을 제공된 리듀스 함수를 이용해 결과로 합침

- `search`

  널이 아닌 값을 반환할 때까지 쌍에 함수를 적용

연산 형태로는 다음과 같은 네 가지 연산 형태를 지원한다.

- 키, 값으로 연산
  - `forEach`
  - `reduce`
  - `search`
- 키로 연산
  - `forEachKey`
  - `reduceKeys`
  - `searchKeys`
- 값으로 연산
  - `forEachValue`
  - `reduceValues`
  - `searchValues`
- `Map.Entry` 객체로 연산
  - `forEachEntry`
  - `reduceEntries`
  - `searchEntries`

이들 연산은 `ConcurrentHashMap`의 상태를 잠그지 않고 연산을 수행한다. 따라서 연산에 제공할 함수들은 계산이 진행하는 동안 바뀔 수 있는 객체, 값, 순서 등에 의존하지 않아야 한다.

또한 이들 연산에는 병렬성 기준값(threshold)를 지정해야 한다. 다음 예제는 `reduceValues` 메서드를 이용해 맵의 최댓값을 찾는 예제이다.

```java
ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
long parallelismThreshold = 1;
Optional<Integer> maxValue = Optional.ofNullable(map.reduceValues(parallelismThreshold, Long::max));
```

위와 같이 기준 값을 1로 설정하면 공통 스레드 풀을 이용한 병렬성을 극대화 할 수 있다.

#### 개수

`mappingCount()` 메서드를 이용해서 맵의 매핑 개수를 구할 수 있다. 매핑의 개수가 int의 범위를 넘어설 수 있으므로 `size()` 대신 `mappingCount()`를 사용하는 것이 바람직하다.

#### 집합뷰

`ketSet()` 메서드는 `ConcurrentHashMap`을 집합뷰로 반환하는 메서드이다. 맵을 바꾸면 집합도 바뀌고, 집합을 바꾸면 맵도 영향을 받는데, `newKeySet()` 메서드를 이용하면 `ConcurrentHashMap`으로 유지되는 집합을 만들 수도 있다.





