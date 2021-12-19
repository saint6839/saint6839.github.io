# 안드로이드 스터디 로드맵 영상 정리

### **안드로이드 스터디 로드맵
 *[https://tv.naver.com/v/9329737](https://tv.naver.com/v/9329737)***

> # <span style="color:red">Level 0 - 기본기</span>



### 기본기 - 알아야할 최소한의 전공 지식

---

- **DB + OS + Network - Algorithm + Data Structures**

![Untitled](../image/Untitled.png)

- 이를 각각이 아닌 하나의 프로세스로 연결하여 이해할 수 있어야함

### 기본기 - **네트워크 프로그래밍 추천 서적(심화)**

---

- 인프라를 보는 눈이 생긴다
- 리눅스, 윈도우의 IO 통지 모델의 메커니즘 이해
- UNIX Network Programming, NETWORK PROGRAMMING FOR MICROSOFT WINDOWS

### 기본기 - T

---

- 우선 T자형 인재
- 5대 과목 + git + 언어

![Untitled](../image/Untitled%201.png)

> # <span style="color:pink">Level 1 - 키워드</span>
>

### 키워드의 시작

---

- Android Developers 유튜브 채널
    - Google IO, Android Dev Summit
    - **안드로이드 모든 키워드의 발생지!**

### 키워드 수집 - 커뮤니티, 컨퍼런스

---

- 1년 마다 열리는 컨퍼런스
    - Naver Deview, if kakao, Airbnb Open, Facebook F8, 드로이드 나이츠...
- Facebook 페이지
    - 생활코딩, GDG, 9XD, 안드로이드 개발자 모임...
- 오픈소스
    - Pretty Awesome Lists, Android Arsenal, ExoPlayer...
- 모임 플랫폼에서 올라오는 네트워킹 행사
    - onoffmix, festa, event-us, meetup...
- Medium
    - AndroidPub, ProAndroidDev...
- 블로그
    - 커니의 안드로이드, 박상권의 삽질블로그, Pluu Dev, 아라비안 나이트, 돼지왕 왕돼지 놀이터, Realm...

> # <span style="color:purple">Level 2 - 안드로이드</span>
>

### 안드로이드 개발자 문서에 다 나와있어!

---

[안드로이드 개발자 문서에 다 나와있어!](https://www.notion.so/a0da0ce449a54b10a75c70a70c37ff28)

### 4대 컴포넌트

---

- Activities
- Services
- Broadcast receivers
- Content providers

### Jetpack

---

- Fragment
- ConstraintLayout
- ViewPager
- RecyclerView

### Inflater, View

---

- 정형화된 형태로만 쓴다
- **Fragment를 재활용 단위로 가져감**
- xml문제로 빌드가 안되는 걸 잘 파악하지 못함
- **Inflater, View에 대한 이해가 있으면 좋은 구조의 View를 만들 수 있다.**

### Inflater란 ?

---

- "부풀게 하다"
- **xml 파일을 읽어와서 파싱한 뒤 객체로 만드는 과정**
- IO와 탐색을 동반하기 때문에 비용이 있는 동작

### View 기반으로 이해하기

---

- Tools - Layout Inspector
- 내가 만든 화면은 어떻게 생겼을까?

### View란?

---

- View는 화면의 직사각형 영역을 차지하며 그리기 및 이벤트 처리를 담당
- View는 UI 구성 요소 (버튼, 텍스트 필드 등)를 만드는데 사용되는 위젯의 기본 클래스
- **ViewGroup은 View의 서브 클래스**로 다른 View 또는 다른 ViewGroup들을 보유하는 컨테이너 View
- **ViewGroup 구현체**
    - ConstraintLayout, RecyclerView, ViewPager
- **View 트리를 전위 순회하면서 처리해 나가는 구조**
    - 깊이 우선 탐색 (DFS)
    - View 계층을 최소화하는 이유
- **렌더링 - measure - layout - draw**

![Untitled](../image/Untitled%202.png)

- **렌더링 뿐만 아니라 터치 이벤트 전달도 유사한 메커니즘으로 전달**
    - return 값에 따라 전달할지 말지 결정

### 안드로이드 - Fragment vs View

---

- UI 기본 구성요소는 Fragment가 아니라 View
    - Fragment는 backstack 처리, activity와 상호작용 등을 위해 View를 래핑 → **결국엔 View**
- **View를 재활용 단위로 가져가는게 좋다**
    - xml에 넣거나 동적 생성해서 Fragment, DialogFragment, ViewHolder, PopupWindow 등으로 재활용 가능

    ![Untitled](../image/Untitled%203.png)

> # <span style="color:blue">Level 3 - 비동기</span>
>

### 비동기

---

- Thread
- Executor
- CompletableFuture(java 8, sdk≥24)
- AsyncTask
- Loader
- WorkManager
- **Rx**
- **Coroutine**

### Rx, Coroutine 그냥 쓰면 될까?

---

- **Docs - Core topics - Activities - Processes and app lifecycle**
    - foreground process
    - visible process
    - service process
    - cached process
- **Docs - Core topics - Background tasks - Sending operations to multiple threads - Communicate with the UI thread**
    - UI Thread의 정의와 통신하는 방법
    - Handler, Looper, MessageQueue
- **Docs - Best Practice - Processes and Threads Overview ( 참고해서 함께 보기 )**
    - Thread 기본 사용법

### Callback에 대한 이해는 필수!

---

- Runnable 왜 있을까?
- subroutines, lambda expressions, function pointers...
- concurrency vs parallelism
- synchronous vs asynchronous
- blocking vs non-blocking
- Java Concurrent
- #synchronized #volatile #atomic #semaphore #ReentrantLock #LockFree #FalseSharing #ForkJoinPoll #Executor #BlockingQueue #CountDownLatch #Future...

**비동기 추천 서적**

⇒ 자바 병렬 프로그래밍, Efficient Android Threading

### 비동기 - Thread, MessageQueue, Handler, Looper

---

- 이 그림을 이해하면 끝!
- ***#Consumer-ProducerPattern(생성자 소비자패턴) #MainThread #UiThread #MessageQueue #Handler #MainLooper #Looper #HandlerThread***
- <span style="color:red">**이 이후 Rx, Coroutine을 사용한다!**</span>

![Untitled](../image/Untitled%204.png)

### 비동기 - background processing

---

- 정확한 타이밍에 실행이 보장되길 원하면? Foreground Service
- 특정 조건을 걸고 실행이 보장되길 원한다면? JobScheduler, JobDispatcher, AlarmManager, BroadCastReceivers

![Untitled](../image/Untitled%205.png)

### 비동기 - WorkManager

---

- **Android Jetpack : easy background processing with WorkManager(Google I/O '18)**
    - GoogleIO 2018 해당 세션을 보도록 하자
    - **WorkManager**
        - 기존에 파편화된 백그라운드 처리를 하나로 모아서 아래 조건에 따라 실행

        ![Untitled](../image/Untitled%206.png)

> # <span style="color:green">Level 4 - 성능, 최적화</span>
>

### 안드로이드 성능 최적화 - 60fps

---

- 안드로이드는 초당 60프레임을 렌더링
- 1프레임을 표현하려면? 1/60 = 약 16ms

### 안드로이드 성능 최적화 - Frame Drop

---

- **Frame Drop**
    - 시스템이 화면을 새로 그리려는데 다음 프레임이 준비되지 않음
    - 유저는 같은 프레임을 다음 갱신까지 32ms 동안 보고 있게 됨

        ![Untitled](../image/Untitled%207.png)

### 안드로이드 성능 최적화 - Profile GPU rendering

---

- **Frame Drop을 발생시키는 요소 - 8가지**
    - 개발자 옵션 - Profile GPU rendering
- **선 안으로 모두 들어오는건 이상적인 일**
    - MinSDK 저사양 단말 기준 적당하게 들어오면 된다.

    ![Untitled](../image/Untitled%208.png)

### 안드로이드 성능 최적화

---

- **MainThread → Worker Thread**
- **UI업데이트가 순차적으로 진행될 수 있도록 조절**
- **CPU → GPU**
- **GPU에 안그려도 되는 부분을 알려준다.**
- #ViewHierarchy #DoubleLayoutTaxation #Async #Overdraw #AnimationProperty #Cache #Clipping #ObjectPool #HardwareAcceleration #VSYNC

### 안드로이드 성능 최적화 - Android Performace Patterns

---

- 2~5분 정도의 동영상으로 안드로이드의 전반적인 지식을 비약적으로 올려준다
    - 한글 자막도 있다.
    - Docs - User Guides - Best practices - Performance에서 다루는 영상

    ![Untitled](../image/Untitled%209.png)

### 안드로이드 성능 최적화 - 추천 서적

---

- 안드로이드 앱 성능 최적화

> # <span style="color:yellow">Level 5 - 프레임워크</span>
>

### 안드로이드 프레임워크

---

- **필요할까?**
    - NDK 개발 시에는 필수
    - 코드 레벨까지 보는 건 비추
    - 성능 최적화시에도 알아야할 내용들이 많기 때문에 어느 정도는 알아야한다..

### 안드로이드 프레임워크 - AOSP

---

- **Android Open Source Project**
    - Configure - RUNTIME
        - #.class #.dex #Dalvik #ART #D8 #R8 #AOT #JIT
        - 컴파일러 관련 내용
    - Develop - GRAPHICS
        - #SurfaceView #TextureView #BufferQueue #SurfaceFlinger #Surface #Canvas #SurfaceHolder
        - 화면 렌더링 관련 내용'
    - Develop - ARCHITECTURE
        - #HAL #Context #Parcel #BinderIPC #SystemServices #MediaServer #SystemServer
        - IPC관련 내용

### 안드로이드 프레임워크 - 추천 서적

---

- 인사이드 안드로이드

> # <span style="color:orange">Level 6 - 언어</span>
>

### 언어 - 무엇을 해야할까?

---

- **Java vs Kotlin**

### 언어 - Kotlin

---

- Google이 말하길...
    - **Kotlin First!**
    - 예제 코드 - **Kotlin Default**
    - **Kotlin 100%** 라이브러리들 등장 중...
    - 곧 Java 추월 예정
- Kotlin은 뿌리부터 새로운 언어가 아님
    - 각 언어들의 좋은 점을 차용
    - Java Collection을 그대로 사용, 현재는 Java로 만들어진 라이브러리를 사용해야하는 경우가 대부분
        - **Java도 알아야한다.**
- **함수가 1급 객체이며 함수 타입 존재**
    - 함수를 변수, 인자, 반환 값으로 전달할 수 있다.

### 언어 - Java

---

- **아직까지 많이 사용되는 언어**
    - 기존 Java 프로덕트가 상당히 많음
    - 객체지향을 잘 알고 쓰면 생산성에 크게 문제 없음
- **Android는 Java8은 SDK ≥ 24(Nougat) 부터 제대로 사용 가능**
    - Min SDK 24 정도 되려면...?
    - 혀재 Java 13까지... Java 8도 안되는데 Java 9은 언제?
    - 갈 길이 멀다
- **Java 8은 함수 타입이 없음**
    - interface로 흉내
- **RxJava 사용시 어느 정도 모던한 개발 가능**

### 언어 - 서적 고르는 팁

---

- Effective ~
- Modern ~
- ~ in Action
- O'REILLY

### 언어 - Java vs Kotlin

---

- **쓰고 싶은거 쓰자**
- **Java**
    - 내가 지금 자바를 알고 있고 안드로이드를 빨리 배우고 싶다면...
    - 가고 싶은 곳이 자바를 사용한다면
- **Kotlin**
    - 처음 입문한다면...
    - 좀 더 모던한 개발을 하고 싶다면...

### 언어 - Kotlin - 추천 서적

---

- Kotlin in Action
- Effective Kotlin

### 언어 - Kotlin - Kotlinconf

---

- 2017년부터 해마다 열리는 코틀린 컨퍼런스

### 언어 - JAVA - 추천 서적

---

- Thinking in JAVA
- JAVA in Action
- Effective Java

### 프로그래밍 패러다임

---

- wiki) **프로그래밍 패러다임은 기능에 따라 프로그래밍 언어를 분류하는 방법.** 언어는 여러 패러다임으로 분류 할 수 있다.
- **유행**
- **프로그래밍 관점을 갖게 해 주고, 결정하는 역할**
- **서로 다른 프로그래밍 언어는 서로 다른 프로그래밍 패러다임을 지원**
- **새로운 것을 가능하게도 하지만 어떤 기법을 금지 함**
    - 구조적 프로그래밍에서는 goto 문을 금지
- #StructuredProgramming #Object-OrientedProgramming #FunctionalProgramming #ReactiveProgramming #FunctionalReactiveProgramming #Modular Programming

### 패러다임

---

- **Kotlin, Java는**
    - 객체지향프로그래밍(Object-Oriented Programming)
    - 함수형프로그래밍(Functional Programming)

### 객체지향프로그래밍 - 추천 서적

---

- #OOP #클래스 #객체 #메시지 #추상화 #캡슐화 #상속 #구성 #다형성 #S.O.L.I.D
- 서적 이름 : **클린소프트웨어, 실용주의 디자인 패턴**

### 객체지향프로그래밍 - 5대 원칙 <span style="color:red">★</span><span style="color:red">★</span><span style="color:red">★</span>

---

- **OOP의 5대 원칙**
    1. **단일 책임 원칙(Single responsibility principle) - SRP**
    2. **개방 폐쇄 원칙(Open/Closed principle) - OCP**
    3. **리스코프 치환 원칙(Liskov substitution principle) - LSP**
    4. **인터페이스 분리 원칙(Interface segregation principle) - ISP**
    5. **의존관계 역전 원칙(Dependency inversion principle) - DIP**

### 객체지향프로그래밍 - 디자인 패턴

---

- **SOURCE MAKING**
    - 디자인패턴 **의도**가 잘 나와있음

### 함수형프로그래밍 - 추천 서적

---

- **클래스가 아닌 함수가 재활용 단위**
- 함수는 1급 객체
- #람다 #클로저 #순수함수 #고차함수 #lazy #커링 #재귀 #메모이제이션 #모나드
- 서적 이름 : **함수형 사고**

> # <span style="color:brown">Level 7 - Best Practice</span>
>

### 잘 변하지 않는 것들..

---

- **Best Practice**
    - 좋은 코드를 짜는 방법을 훈련
- 서적
    - 클린코드
    - 리팩토링
    - 클린아키텍쳐

    ### 좋은 코드

    ---

    - 높은 응집도 낮은 결합도
    - 캡슐화가 잘돼있다
    - 가독성이 좋다
    - 변경하기 쉽다
    - 테스트를 작성하기 쉽다
    - 낮은 의존성

    ### Best Practice

    ---

    - 경험이 없다면 봐도 이해도 안되고 공감도 안된다.
    - 알아야할 배경 지식도 많다.

    ### Best Practice - 우선 이것부터 해보자 <span style="color:red">★</span>

    ---

    - 함수는 한가지 일만 한다
    - SRP : 클래스는 한가지 책임만 가진다
    - 상속 대신 구성

    ### Best Practice - 함수는 한가지 일만 한다

    ---

    ![Untitled](../image/Untitled%2010.png)

    - **너무 포괄적인 의미**
    - **동작을 예상하기 어렵다**
    - **부수 효과**
        - Ex) 초기화만 할거라고 예상했는데 특정 flag 값을 바꿔버려서 View가 나타나지 않음
    - **점점 커진다**

    ![Untitled](../image/Untitled%2011.png)

    - **네이밍을 잘해서 각각 한가지 일을 하는 private 메소드로 분리**

    - 두 가지 일을 하고 싶다면 ?
        - 한 가지 일을 하는 함수들을 조합한다.

    ![Untitled](../image/Untitled%2012.png)

    - 그러나 분리하다보면 private메소드가 엄청나게 많아진다
        - **리팩토링 신호**
        - **SRP**

    ### Best Practice - SRP

    ---

    - **The Single Responsibility Principle**
    - **책임**? "어떤 변화에 의해 클래스를 변경해야 하는 이유는 오직 하나뿐이어야 함"
    - 코드가 많다면? 해당 클래스가 여러 책임을 가졌을 가능성이 높음
    - 나눌 수 있는 책임을 분배하여 각각 클래스로

    <span style="color:red">**xml에 여러 요소 들어갈 경우 커스텀뷰를 이용해보자! ( 프래그먼트보다 효율적 )**</span>

    ### Best Practice - 상속 대신 구성

    ---

    - 상속(Extension)? is-a
    - 구성(Composition)? has-a
    - 각자의 책임을 가진 클래스로 구성
    - 상속은 decorator 형태로...

    ### Best Practice - 우선 이것부터 해보자 <span style="color:red">★</span>

    ---

    - **함수는 한가지 일만 한다**
        - 함수형 프로그래밍의 기본
    - **SRP : 클래스는 한가지 책임만 가진다**
        - 나머지 원칙도 저절로 따라옴
    - **상속 대신 구성**
        - 잘못된 상속은 확장이 점점 더 어려워지므로 구성하자

> # <span style="color:gray">TODO</span>
>

### 고인물이 되지 말자

---

- 키워드 수집 꾸준히
- 좋은 코드를 짜기 위한 훈련을 해라

### 기록

---

- 블로그를 추천

### 테스트

---

- **한 가지 일만 하는 함수로 작성했다면 유닛테스트를 작성해보자**
- **좋은 구조로 가는 지름길이 되기도 한다.**
- **CI**
- #TDD #BDD #Junit #Dummy #Stub #Spy #Fake #Mock #Mockito #PowerMock #Robolectric #CI #Jenkins #TestCoverage #Jacoco

### 커뮤니케이션

---

- **개발을 잘한다 ≠ 일을 잘한다**
- 관련 서적 : 클린 코더