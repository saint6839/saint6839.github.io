---
layout: post
title:  "모던 자바 인 액션 스터디 - Week1"
date:   2022-02-13T00:00:00-00:00
author: sangyeop
categories: Sproutt 2nd





---

# 새싹 개발 서적 스터디 - 모던 자바 인 액션 Week1



에코노베이션 새싹 2기 `모던 자바 인 액션` 스터디

`37page ~ 52page`



> ***Stream 이란?*** 
>
> - 자바 8 부터 추가된 기능으로 "컬렉션, 배열등의 저장 요소를 하나씩 참조하며 함수형 인터페이스(람다식)을 적용하여 반복적으로 처리할 수 있도록 해주는 기능" 
> - 한 번에 한 개씩 만들어지는 연속적인 데이터 항목들의 모임이다.

스트림에 대한 자세한 내용은 뒷 부분에서 등장하기 때문에 이 정도로만 이해하고 넘어가기로 한다.



#### 자바 8의 등장 배경

- 자바8이 등장하고 자연어에 가까운 방식으로 코딩이 가능해졌음

- 자바8 등장 이전에는 CPU의 나머지 코어를 활용하려면 스레드를 사용해야했음
  - 관리가 어렵고 많은 문제가 발생한다.
  
- **간결한 코드**, **멀티코어 프로세서의 쉬운 활용**
  
  - **스트림 API**
    - 데이터베이스의 질의 언어에서 표현식을 처리하는 것처럼 병렬 연산을 지원하는 API
    
  - 메서드에 코드를 전달하는 기법
  
  - 인터페이스의 디폴트 메서드
  
    

#### 자바 8 설계의 밑 바탕을 이루는 세 가지 개념

1. 스트림 처리
2. 동작파라미터화 ( 메소드를 다른 메소드의 파라미터로 전달하는 기능 )
3. 병렬성과 공유 가변 데이터



#### 자바가 변화하는 이유

프로그래밍 언어는 생태계와 매우 닮아있다. 그렇기 때문에 새로운 언어가 등장할때마다 진화하지 않은 기존 언어들은 사라지기 마련이다. 완벽한 언어란 존재하지 않으며, 각각의 장단점을 가지고 있다. 그리고 이러한 특정 분야에서 장점을 가진 언어는 다른 경쟁언어들을 도태시킨다. 자바는 1995년 첫 베타 버전이 공개된 이후로 여러 경쟁언어들을 대신하며 성공적으로 살아남았다. 

기후(프로그래밍 문제)가 변화하면 재배가능한 작물(프로그래밍 언어)이 달라지는 것 처럼, 기후 변화에 대응하기 위해서 자바는 점점 변화하고 대중화가 되고 있다는 것이다.



#### 프로그래밍 생태계에서 자바의 위치

자바는 시작부터 많은 유용한 라이브러리들을 포함하는 잘 설계된 객체지향언어로 시작했다. 코드를 JVM 바이트 코드로 컴파일하는 특징때문에 자바는 인터넷 애플릿 프로그램의 주요 언어가 되었다. 

- 자바가 대중적인 프로그래밍 언어로 성장하게된 계기
  - 캡슐화
  - '모든 것은 객체다'
    - 마우스 클릭 이벤트 객체를 만들어 두면, 모든 곳에서 사용할 수 있었다.



### 스트림 API

------

#### 스트림 처리

기존에는 한 번에 한 항목을 처리했지만 이제 자바 8에서는 일련의 스트림으로 만들어서 처리할 수 있다. 이는 스레드라는 복잡한 작업을 하지 않으면서 공짜로 병렬성을 얻을 수 있다는 의미이기도 하다.



####  **동작파라미터화**

코드 일부를 API로 전달하는 기능. 즉, 메소드를 다른 메소드의 파라미터로 넘겨주는 기능을 의미한다.



#### 병렬성과 공유 가변 데이터

안전하게 병렬성을 얻기 위해서는 **공유 가변 데이터**에 접근해서는 안된다. 즉 두 프로세서가 공유된 변수를 동시에 바꾸려하면 문제가 생긴다. 기존에는 synchronized 성능에 악 영향을 미치는 기능을 사용해서 데이터를 보호하는 규칙을 만들 수 있었다. 하지만 자바 8에서부터는 이 보다 쉽게 병렬성을 활용할 수 있게 되었다.



#### **함수형 프로그래밍**의 핵심적인 사항

- 공유되지 않은 가변 데이터
  - 가변 공유 상태가 없는 병렬 실행을 이용해서 효율적이고 안전하게 함수나 메소드를 호출할 수 있음
- 메소드, 함수 코드를 다른 메소드로 전달하는 두 가지 기능





### 자바 함수

------

자바에서 `함수 = 메소드`를 의미한다. 특히 `정적 메소드`와 같은 의미로 사용된다.

자바에서는 원시형 타입(int, double .. 등등), 객체(new String("abc"), new Integer(1111) ... 등등) 모두 값이다. 심지어 배열도 객체인데 왜 함수가 필요할까?

프로그래밍 언어의 핵심은 **값을 바꾸는 것** 이다. 자바에서 위에서 언급한 타입과 같이 자유롭게 값을 바꿀 수 있는 값들을 **1급 시민** 이라고 한다. 그리고 메소드, 클래스 등은 **2급 시민**에 해당한다. 즉 메서드, 클래스 등과 같은 2급 시민들을 1급 시민처럼 런타임에 메소드를 전달하기 위해서 함수가 필요한 것이다( ex 자바 스크립트 ). 이렇게 되면 메소드를 값 처럼 취급 할 수 있게 된다.



#### 메서드와 람다를 일급 시민으로

1. **메소드 참조**

   - 메소드 참조 사용 전

   ```java
   File[] hiddenFiles = new File(".").listFiles(new FileFilter() {
     	pubic boolean accept(File file) {
         	return file.isHidden();					// 숨겨진 파일 필터링
       }
   });
   ```

   - 메소드 참조 사용 후

   ```java
   File[] hiddenFiles = new File(".").listFiles(File::isHidden);
   ```

   전자에서는 `File` 클래스안에 `isHidden()`이라는 메소드가 이미 들어있음에도 불구하고, `FileFilter`를 인스턴스화 해서 사용해야 했다. 그러나 자바 8이 등장 한 이후로는 후자 처럼 이미 준비되어 있는 isHidden()이라는 **함수**를 **메소드 참조**를 이용해서 직접 `listFiles`에 전달할 수 있다.

   기존에는 **객체 참조(new)**를 이용해서 객체를 이리저리 주고 받았던 것처럼, 자바 8에서는 **메서드 참조(File::isHidden)*을 이용해서 메소드를 이리저리 주고 받을 수 있게 된 것이다. 메소드는 코드를 포함하고 있으므로 즉, 코드를 마음대로 전달할 수 있게 된 것이다. 복사 붙여넣기 필요없이.

   

2. **람다 : 익명 함수**

   자바 8에서는 기명(named) 메소드를 1급 시민으로 취급할 뿐아니라, **람다**(또는 익명 함수) 를 포함하여 함수도 값으로 취급할 수 있다. 

   - 어떤 경우에 사용할까?

     ```java
     static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p) {
       	List<Apple> result = new ArrayList<>();
       	for (Apple apple : inventory) {
           	if (p.test(apple)) {				// test() 메소드는 boolean 반환
               	result.add(apple);
             }
         }
       	return result;
     }
     ```

     > Predicate<T> 
     >
     > 수학에서는 값을 인수로 받아서 true나 false로 반환해주는 함수를 프레디케이트라고 한다.

     이를 다음과 같이 호출 한다

     ```java
     filterApples(inventory, Apple::isGreenApple);
     filterApples(inventory, Apple::isHeavyApple);
     ```

     다음과 같이 메소드를 전달하는 것은 정말 유용한 기능이지만, 이처럼 메서드 참조를 사용하기 위해서 한 번씩만 사용되는 `isGreenApple`과 `isHeavyApple` 메소드를 매번 정의하는 것은 귀찮은 일이다. 이러한 경우에 람다를 사용하면 간단하게 별도의 메서드 정의 없이 간편하게 사용할 수 있다.

     ```java
     filterApples(inventory, (Apple a) -> GREEN.equals(a.getColor()));
     filterApples(inventory, (Apple a) -> a.getWeight() > 150);
     
     // 이러한 형태도 가능하다!
     filterApples(inventroy, (Apple a) -> a.getWeight() < 80 || RED.equals(a.getColor()));
     ```

     하지만 람다가 몇 줄 이상으로 길어진다면 오히려 가독성을 해칠 수 있으므로, 이름이 분명한 메소드를 정의하고 메소드 참조를 활용하여 코드의 명확성을 챙기는 것이 바람직하다.



### 스트림

------

스트림에 대해서 자세한 내용은 책의 뒷 부분에서 설명될 것이므로 얼마나 유용한지 간단하게 표현하자면, 스트림API를 이용하면 중첩된 제어 흐름이 많은 문장을 한눈에 그 의미가 파악될 수 있도록 바꿔줄 수 있다.

컬렉션에서는 반복문장을 직접 처리하는 **외부 반복** 을 사용했으나, 스트림 API에서는 라이브러리 내부에서 모든 데이터 처리가 이루어지는 **내부 반복**을 사용한다. 

이러한 방식을 사용함으로써 얻는 장점은 다음과 같다. 만약 거대한 컬렉션 리스트를 처리하려면 단일 CPU로는 이를 처리하기가 어려울 것이다. 그러나 스트림을 사용하면 8개의 코어를 가진 컴퓨터의 경우에는 8개로 분할하여 작업을 병렬적으로 처리할 수 있기 때문에 기존 단일 CPU보다 8배 빨리 작업을 처리할 수 있게 된다.



#### 멀티스레딩은 어렵다

스레드를 이용해서 **멀티스레딩** 코드를 이용해서 병렬성을 이용하는것은 쉽지 않다. 각각의 스레드가 공유된 데이터에 접근하고 데이터를 갱신할때 스레드를 잘 제어하지 못하면 원치 않은 결과가 도출되기 쉽다.

스트림은 '컬렉션을 처리하면서 발생하는 모호함과 반복적인 코드 문제' 그리고 '멀티코어 활용 어려움' 이라는 두 가지 문제를 모두 해결했다. 

- **필터링**
  - 자주 반복되는 패턴으로 조건에 따라 데이터를 필터링(무게 또는 색깔에 따라 사과 선택)
- **추출**
  - 데이터를 추출 (리스트에서 각 사과의 무게 필드 추출)
- **그룹화**
  - 데이터를 그룹화(숫자 리스트의 숫자를 홀수와 짝수로 그룹화)

또한 이러한 동작들을 쉽게 병렬화 할 수 있다는 점도 스트림 API로의 변화의 동기가 되었는데, 두 개 이상의 CPU를 가진 환경에서 리스트를 필터링할 때 **한 CPU는 리스트의 절반을 기준으로 앞 부분을 처리**하고, **다른 한 CPU는 리스트의 절반을 기준으로 뒷 부분을 처리**하도록 요청한다. 이를 **포킹 단계** 라고 한다. 그리고 각각의 CPU가 맡은 부분을 필터링 한 뒤, 마지막에 하나의 CPU가 두 결과를 정리한다.



### 디폴트 메소드와 자바 모듈

------

구현 클래스에서 구현하지 않아도 되는 메소드를 인터페이스에 추가할 수 있는 기능을 제공하는데, 메소드의 바디는 클래스 구현이 아니라 인터페이스의 일부로 포함이 된다. 그래서 이를 **디폴트 메소드** 라고 부른다.

```java
public interface List<E> extends Collection<E> {
	...
  default void sort(Comparator<? super E> c) {
          Object[] a = this.toArray();
          Arrays.sort(a, (Comparator) c);
          ListIterator<E> i = this.listIterator();
          for (Object e : a) {
              i.next();
              i.set((E) e);
          }
      }
	...
}
```

위 코드 예제와 같이 인터페이스 임에도 메소드의 본문을 작성할 수 있다. 그렇기 때문에 자바 8에서는 List에 직접 sort 메소드를 호출할 수 있다.



### 함수형 프로그래밍에서 가져온 다른 유용한 아이디어

------

- **Optional<T>**
  - NullPointer 예외를 피할 수 있도록 해주는 클래스
  - 값이 없는 상황에서 어떻게 처리할지 명시적으로 구현하는 메소드를 포함하고 있다

- **구조적 패턴 매칭 기법**
  - if-then-else가 아닌 케이스로 정의하는 수학과 함수형 프로그래밍의 기능을 의미
  - 함수형 언어는 보통 다양한 데이터 타입을 switch문에 사용할 수 있는데 자바는 문자열과 기본형만 사용할 수 있다.

### 

### 읽으면서 몰랐던 키워드들

------

- JVM
  - 자바 가상 머신
  - 자바 프로그램이 어느 기기, 또는 어떤 운영체제 상에서도 실행될 수 있게 하는 것
    - "한 번 작성해, 어디에서나 실행한다"
  - 프로그램 메모리를 관리하고 최적화 하는 것
    - 가비지 컬렉션

- 애플릿
  - 웹 브라우저 환경에서 동작하는 작은 프로그램
  - Ex) 인터넷 익스플로러 및 다른 플러그인을 지원하는 브라우저에 소속된 윈도우 미디어 플레이어, 브라우저 게임들



- WIMP (소프트웨어 번들)
  - 웹 서버를 운영하는데 자주 사용되는 소프트웨어 모음
    - Windows - 운영 체제
    - IIS - 웹 서버
    - MySQL 또는 MSSQL - DBMS
    - PHP 또는 Perl 또는 Python - 프로그래밍언어ㅈ