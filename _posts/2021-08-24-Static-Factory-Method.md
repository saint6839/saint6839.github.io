---
layout: post
title:  "Static Factory Method"
date:   2021-08-24T14:25:52-05:00
author: sangyeop
categories: Android


---





# 정적 팩토리 메소드

- #### **장점**

  1. **이름을 가질 수 있다.**

     다음 예시는 게임 캐릭터 생성을 담당하는 클래스의 코드이다.

     ```java
     class Character {
         int strength, intelligence, agility;
         
         public Character(int strength, int intelligence, agility){
             this.strength = strength;			// 힘
             this.intelligence = intelligence;	// 지능
             this.agility = agility;				// 민첩성
         }
         
         // 정적 팩토리 메소드
         public static Character newWarrior(){
             return new Character(20,5,5);		// 전사는 힘이 높다.
         }
         
         public static Character newThief(){
             return new Character(5,5,20);		// 도적은 민첩성이 높다.
         }
     }
     ```

     - 만약 위와 같은 클래스를 생성자를 사용하여 전사나 도적을 생성한다면 다음과 같다.

       ```java
       Character warrior = new Character(20,5,5);
       Character thief = new Character(5,5,20);
       ```

       한 눈에 봤을때 변수명이 없었다면 어떤 캐릭터를 생성하는지 알아보기가 어렵다.

       

     - 만약 정적 팩토리 메소드를 사용한다면 다음과 같다

       ```java
       Character warrior = Character.newWarrior();
       Character thief = Character.newThief();
       ```

       한 눈에 봤을때 변수명이 없더라도 어떤 캐릭터를 생성하는지 메소드 명만으로도 알아보기 쉬워진다.

       따라서 이름을 가질 수 있는 정적 팩토리 메소드에는 의미를 이해하지 못하는 경우 등의 제약이 없기 때문에, 한 클래스에 시그니처가 같은 생성자가 여러 개 필요할 것 같으면, 생성자를 정적 팩토리 메소드로 바꾸고 각각의 차이를 잘 드러내는 이름을 지어주자.
       
       

  2. **호출될 때마다 인스턴스를 새로 생성하지는 않아도 된다.**

     하지만 위와 같이 전사와 도적을 만드는 코드는 정적 팩토리 메소드를 호출할 때마다 `new Character(...)` 를 호출하게 된다. 그러나 immutable(불변) 객체를 조회(캐시)하여 쓰고 있다면 굳이 일일이 비싼 `new` 같은 비싼 연산을 사용할 필요가 없다.

     다음 예시를 보자

     ```java
     private static final BigInteger ZERO = new BigInteger(new int[0], 0);
     
     private final static int MAX_CONSTANT = 16;
     private static BigInteger posConst[] = new BigInteger[MAX_CONSTANT+1];
     private static BigInteger negConst[] = new BigInteger[MAX_CONSTANT+1];
     
     static {
         // posConst에 1~16까지의 BigInteger 값을 담는다.
         // negConst에 -1~-16까지의 BigInteger 값을 담는다.
     }
     
     public static BigInteger valueOf(long val){
             if (val == 0)
                 return ZERO;
             if (val > 0 && val <= MAX_CONSTANT)
                 return posConst[(int)val];
         	else if (val < 0 && val >= -MAX_CONSTANT)
                 return negConst[(int)-val];
         
         	return new BigInteger(val);
         }
     ```

     다음과 같이 정적 팩토리 메소드를 선언 해주면 val==0 일 경우에 이미 생성되어 있는 ZERO 객체를 반환하므로, 일일이 객체를 생성하는 일을 피할 수 있다.

     

  3. **반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.**

     이 능력은 반환할 객체의 클래스를 자유롭게 선택할 수 있게 하는 '엄청난 유연성'을 선물한다.

     다음은 어느 가상의 인터넷 쇼핑몰에서 할인 코드를 처리하는 정적 팩토리 메소드이다.

     ```java
     class OrderUtil{
         public static Discount createDiscountItem(String discountCode) throws Exception{
             if (!isValidCode(discountCode)){
                 throw new Exception("잘못된 할인 코드");
             }
             // 쿠폰 코드인가? 포인트 코드인가?
             if(isUsableCoupon(discountCode)){
                 return new Coupon(1000);
             }else if(isUsablePoint(discountCode)){
                 return new Point(500);
             }
         }
     }
     
     class Coupon extends Discount{}
     class Point extends Discount{}
     ```

     할인 코드의 규칙에 따라 `Coupon`과 `Point` 객체를 선택적으로 리턴하고 있다.

     이를 위해서는 두 하위 클래스가 같은 인터페이스를 구현하거나, 같은 부모 클래스를 갖도록 하면 된다.

     만약 파일을 분리하기 애매한 작은 클래스가 있다면 private class를 활용할 수도 있다.

     다음은 `java.util.Collections`에서 `EMPTY_MAP` 부분만 발췌한 것이다.

     ```java
     @SuppressWarnings("rawtypes")
     public static final Map EMPTY_MAP = new EmptyMap<>();
     
     /**
      * Returns an empty map (immutable).  This map is serializable.
      */
     @SuppressWarnings("unchecked")
     public static final <K,V> Map<K,V> emptyMap() {
         return (Map<K,V>) EMPTY_MAP;
     }
     
     private static class EmptyMap<K,V> extends AbstractMap<K,V> implements Serializable {
         /* 생략 */
     }
     ```
     
     `EmptyMap` 클래스는 `java.util.Collections` 내에 private static으로 선언되었으며, `emptyMap`이라는 정적 팩토리 메서드를 통해 캐스팅된 인스턴스를 얻을 수 있다.

     

  4. **입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다**

     3번과 비슷한 의미를 갖는다. 예를 들면 같은 이름의 메서드지만 매개변수의 개수에 따라 리턴받는 클래스를 아무런 하위타입 클래스로 리턴 받을 수 있다는 것을 의미한다.

     ```java
     public abstract class StaticFactoryMethodType{
         public abstract void getName();
         
         static public StaticFactoryMethodType getNewInstance(String one){
             return new OneClass();
         }
         static public StaticFactoryMethodType getNewInstance(String one, String two) {
             return new TwoClass();
         }
     }
     
     class OneClass extends StaticFactoryMethodType{
         public void getName(){
             System.out.println("쿠폰을 발행합니다.");
         }
     }
     
     class TwoClass extends StaticFactoryMethodType{
         public void getName(){
             System.out.println("포인트 1000점을 적립합니다.");
         }
     }
     ```

     이 코드를 사용해보면

     ```java
     public static void main(String args[]){
         StaticFactoryMethodType isOneObj = StaticFactoryMethodType.getNewInstance("one");
         StaticFactoryMethodType isTwoObj = StaticFactoryMethodType.getNewInstance("one","two");
         isOneObj.getName();
         isTwoObj.getName();
     }
     ```

     결과는

     ```java
     // 출력
     쿠폰을 발행합니다.
     포인트 1000점을 적립합니다.
     ```

  5.  **정적 팩토리 메소드를 작성하는 시점에서 반환할 객체의 클래스가 존재하지 않아도 된다**

     3,4과 같은 개념의 장점이다. 어떤 인스턴스를 만들어야할 지 모를 때 정적 팩토리 메소드를 사용하면 좀 더 유연하게 인스턴스를 생성할 수 있다. 예를 들면 JDBC에서 getConnection()을 할 때 어떤 DB를 쓰느냐에 따라서 드라이버가 달라지고 인스턴스가 달라지는데 이때 이 인스턴스를 유연하게 생성할 수 있다.

     하지만 항상 정적 팩토리 메소드가 장점만 있는 것은 아니다.

     

- ### 단점

  > 1.  public이나 protected 생성자 없이 정적 팩토리 메서드만 제공하면 하위 클래스를 만들 수 없다.
  > 2. 정적 팩터리 메서드는 프로그래머가 찾기 어렵다.

  1. **public이나 protected 생성자 없이 정적 팩토리 메서드만 제공하면 하위 클래스를 만들 수 없다**

     :  당연하게도 생성자가 존재하지 않으면 클래스를 상속받을 수 없다. 따라서 정적 팩토리 메서드만 존재하는 클래스는 하위 클래스를 구현할 수 없습니다. 이러한 제약사항은 단점이기도 하지만 상속이 아닌 컴포지션을 강제해 클래스간 결합도를 낮추기 떄문에 장점으로 받아들일 수도 있다.

  2. **정적 팩터리 메서드는 프로그래머가 찾기 어렵다.**

     : 자바 Docs를 보면 생성자는 따로 정의하여 설명하지만 정적 팩토리 메서드는 설명이 명확하게 들어나지 않아 개발자가 인스턴스화하는 방법을 알아야한다. 따라서 API 문서를 잘 써놓거나 이미 알려진 정적 팩토리 메서드 규약을 따라 짓는 방법으로 이러한 문제를 완화시켜줘야한다..

  > 핵심 : 정적 팩터리 메서드는 항상 장점만 있는 것이 아니기 때문에 public 생성자와 정적 팩터리 메서드의 쓰임새와 장단점을 잘 이해하고 사용하는 것이 좋다. 

------

참고자료 : 

- *Effective Java*

* https://velog.io/@sunwook317/Item1-%EC%83%9D%E%84%B1%EC%9E%90-%EB%8C%80%EC%8B%A0-%EC%A0%95%EC%A0%81-%ED%8C%A9%ED%84%B0%EB%A6%AC-%EB%A9%94%EC%84%9C%EB%93%9C%EB%A5%BC-%EA%B3%A0%EB%A0%A4%ED%95%98%EB%9D%BC*

- https://johngrib.github.io/wiki/static-factory-method-pattern/
