---
layout: post
title:  "사다리 게임 미션 페어프로그래밍 - 회고(2)"
date:   2022-03-02T0:00:00-00:00
author: sangyeop
categories: Sproutt-2nd



---

# 사다리 게임 미션 페어프로그래밍 - 회고(2)

2월 초부터 진행중인 자바, 스프링부트, 알고리즘 스터디에서 세 번째 미션으로 지난 주에 구현했던 `사다리 게임` 코드를 서로 페어를 교체하여 **리팩토링**하는 미션을 부여받았다.

여기서 말하는 페어프로그래밍의 규칙은 다음과 같다.

- **네비게이터**와 **드라이버**

  - 네비게이터 : 전체적인 코드 구조와 설계에 대해서 방향을 잡아주는 역할
  - 드라이버 : 키보드를 잡고 코드를 네비게이터의 요구사항에 따라 코드를 작성함

  위 두 역할을 **10분 마다** 번갈아가며 진행하는 형식의 프로그래밍을 의미한다. 네비게이터가 드라이버에게 지시를 하지만 드라이버는 무조건 그 의견을 수용하는것이 아니라, 본인의 생각과 다르다면 어필을하고 같이 조율해 나갈 수 있다.



### 회고

------

#### 어려웠던 점

- 원래 진행하던 코드가 아닌 누군가가 작성하던 코드를 리팩토링을 시도한건 이번이 처음이었다. 이런 경험을 하면서 아직 다른 사람들의 코드를 읽는 능력이 많이 부족함을 느꼈다. 코드를 이해하기 위해서 원래 이 미션을 진행중이던 페어에게 설명을 한참을 듣고나서, 설명이 굉장히  명쾌했음에도 머릿속에서 정리되지 않는 느낌이 들었다. 

- 람다와 스트림을 적재적소에 잘 사용된 코드들이 작성되어 있었는데, 아직 람다와 스트림에대한 이해가 많이 부족한 상태였기 때문에 코드를 람다와 스트림을 이용해서 리팩토링하고 이해하는 과정에서 어려움이 있었다.
- 자바를 나보다 더 잘 다루고, 잘 이해하고있는 페어와 프로그래밍을 진행하면서. 아직 객체지향적으로 바라보는 능력이 많이 부족하다고 느꼈다.

#### 배웠던 점

- 다른 사람의 코드를 보고 이해하려고 노력한 경험이 이번이 거의 처음이었다. 이번 페어와 함께 프로그래밍을 진행하면서, 다른 사람에게 코드를 설명하는 능력이 뛰어나다고 느꼈다. 생각보다 복잡한 구조와 쉽지 않은 코드였음에도 불구하고, 단 한 두번의 설명으로 다른 사람을 이해시키는 것이 대단하다고 느꼈다. 이번 페어를 진행하면서 평소 나는 누군가에게 설명을 잘 못했기 때문에, 설명하는 방법을 페어를 통해 어느정도 배울 수 있었던 것 같다.
- 람다와 스트림에 대한 필요성과 유용성을 몸소 느낄 수 있었다. 물론 거의 잘 짜여진 코드를 보면서 람다와 스트림을 온전히 이해하는데는 어려움이 있었다. 그러나 페어의 설명을 들으면서 람다와 스트림이 적재적소에 잘 사용됐다는 느낌을 받았고, 또한 람다를 큰 고민을 들이지 않고 작성하는 페어를 보면서 람다와 스트림에 대한 공부와 연습이 더 필요하겠다고 느꼈다.
- 지난 주에 구현했던 사다리게임 미션과의 차이점은 지난 주에는 알고리즘스럽게 코드를 구현했다면, 이번 주에 마주친 코드는 지난 주의 코드보다 객체 지향적이라고 느꼈다. 정말 작은 기능 단위까지 객체로 쪼개려고 노력한 것이 코드에서 보였다. 이러한 부분에서 내 생각보다 기능들이 더 작은 단위로 나누어 질 수 있음을 느꼈고, 책으로 이론적으로 익힌 객체지향에 대한 내용도 중요하지만 이를 실제로 구현해 낼 수 있는 능력이 더 중요하다고 느껴졌다.

#### 느낀점

- 이번 페어를 진행하면서 페어와 나의 실력차이를 많이 느낄 수 있었다. 그래서 최대한 페어에게 많이 배우려고 했던 것 같다. 그러나 계속해서 따라가기만 하면 결국에는 얻는것이 없을 것 같아서. 이 과정에서 페어의 생각에서 배우되, 페어가 제안한 방법이 '좋지 않은' 이유에 대해서 고민하려고 노력했다. 결국에는 이 부분 또한 내가 부족했던 탓인지 그 이유를 찾는게 쉽지는 않았다. 때로는 내가 생각해도 논리적이지 않은 이유를 들어서 의견에 반할때에도, 페어가 항상 차분하게 좋은 설명을 해주어서 정말 많이 배웠던 것 같다.

