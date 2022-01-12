---
layout: post
title:  "MVC Pattern Change Process"
date:   2022-01-10T00:00:00-00:00
author: sangyeop
categories: Spring



---





### MVC패턴 전

------

비즈니스 로직과 뷰 로직이 한 군데에 묶여 있었음



### MVC패턴 1

------

비즈니스 로직과 뷰 로직이 Controller와 View로 두개로 나누어지고, Controller가 Model에 데이터를 넘기면 View가 Model의 데이터를 참조하여 사용 하는 방식으로 기능이 나누어짐



### MVC패턴 2

------

Controller는 컨트롤러 로직만 맡고, Service와 Repository가 데이터 접근과 비즈니스 로직을 맡음. 이후 마찬가지로 Controller가 Model에 데이터를 넘겨주면 View가 Model의 데이터를 참조하여 사용하게 됨

컨트롤러에 비즈니스 로직을 둘 수 도 있지만, 이렇게 되면 컨트롤러가 너무 많은 역할을 담당하게 된다. 그래서 일반적으로 **서비스**라는 계층을 별도로 만들어서 처리한다. 그리고 컨트롤러는 비즈니스 로직이 있는 서비스 호출을 담당한다.

