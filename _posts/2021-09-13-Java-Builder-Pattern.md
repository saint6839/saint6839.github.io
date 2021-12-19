---
layout: post
title:  "Java Builder Pattern"
date:   2021-09-13T14:25:52-05:00
author: sangyeop
categories: Java


---





# 자바 생성자 빌더 패턴

일반적으로 우리는 데이터 클래스를 아래와 같이 정의한다.

```java
class UserData{
    private String name;
    private int age;
    private String email;
    
    UserData(String name, int age, String email){
        this.name = name;
        this.age = age;
        this.email = email;
    }    
    public void setName(String name){
        this.name = name;
    }    
    public void setAge(int age){
        this.age = age;
    }
    public void setEmail(String email){
        this.email = email;
    }   
    public String getName(){
      	return this.name;
    }
    public int getAge(){
        return this.age;
    }
    public String getEmail(){
        return this.email;
    }       
}
```

대부분 다음과 같이 생성자를 정의하고, setter / getter 메소드를 정의할 것이다. 이처럼 작성하는것이 익숙하기도하고 간편하기 때문일 것이라고 생각한다. 그러나 이러한 구조에는 몇 가지 단점이 존재한다. 만약 이 클래스 객체를 생성하고자 한다고 가정하자. 

```java
UserData data = new UserData("홍길동","24","saint6839@gmail.com");
```

다음과 같이 정의가 될 것이다. 언뜻 보면 첫 번째 인자는 사람 이름, 두 번째 인자는 나이, 세 번째 인자는 이메일 이라고 추측할 수 있을 것이다. 그러나 만약 생성자 인자가 다음과 같이 추측이 불가능한 경우를 예를 들어보자. 

```java
 UserData(int korean, int math, int eng){
        this.korean = korean;
        this.math = math;
        this.eng = eng;
    }    
```

만약 위 클래스가 담은 데이터가 name, age, email이 아니라 다음과 같이 국어, 수학, 영어 과목의 점수를 정수로 담는 클래스라고 가정하자. 이번에는 생성자의 모양이 다음과 같을 것이다.

```java
UserData data = new UserData(90,85,100);
```

위 코드와 같은 객체 생성일 경우 본인은 UserData 클래스를 열어보지 않고 세 숫자가 어떤 것을 의미하는지 알 수 있겠는가? 아마도 불가능 할 것이다. 자바에는 여러가지 생성자 패턴이 존재한다. 자바 빈즈 패턴, 팩토리 패턴 등등과 빌더 패턴이 존재한다. 빌더 패턴은 자바 빈즈 패턴과 팩토리 패턴의 단점들을 보완했다고 볼 수 있다.

그렇다면 빌더 패턴의 장점이 무엇인지는 코드를 보면서 이해해보도록 하자.

```java
class UserData{
	private final int korean;
	private final int math;
	private final int eng;
	
	public static class Builder{
		private int korean;
		private int math;
		private int eng;
		
		public Builder(){
			// 만약 필수 인자를 지정하고 싶다면 Builder 클래스 내부에서
			// private final int로 선언을 하고 Builder 생성자의 인자로 받으면 된다.
		}
		public Builder korean(int val){
			korean = val;
			return this;
		}
		public Builder math(int val){
			math = val;
			return this;
		}
        public Builder eng(int val){
            eng = val;
            return this;
        }
        public UserData build(){
            return new UserData(this);
        }
	}
    private UserData(Builder builder){
        korean = builder.korean;
        math = builder.math;
        eng = builder.eng;
    }
}
```

다음과 같은 구조가 빌더 패턴의 기본적인 구조이다. 이 구조를 처음 접한다면 복잡해 보인다고 생각될 것이다. 그러나 익숙해진다면 유지보수에서 이전 구조보다 꽤 이득을 볼 수 있을 것이다. 이번에는 빌더 패턴을 적용한 클래스의 객체를 생성하는 코드이다.

```java
UserData data = new UserData.Builder().korean(90).math(85).eng(100).build();
```

이전 코드와 비교해보면 빌더 클래스 내에 정의한 명시적인 이름의 메소드로 각각의 인자 값들이 어떤 의미를 갖는지 한눈에 파악할 수 있게 되었다. 각 인자값이 별도의 메소드로 구현되기 때문에 클래스 필드에 변화가 생기더라도 제거하고 추가할때 간편하게 수정이 가능하다.

또한 기존의 setter와 달리 의미없는 호출을 남발할 가능성이 줄어들게 된다. 항상 빌더 패턴이 유리한건 아니지만 대부분의 경우에는 도움이 될 가능성이 크므로 잘 익혀두면 좋을 것이라고 생각된다.

