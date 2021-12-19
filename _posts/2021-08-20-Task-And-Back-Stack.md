# 태스크와 백스택 ( Intent Flag )

- <span style="color:red">**액티비티의 백스택 순서는 재정렬 될 수 없다**</span>
  - 안드로이드의 액티비티 스택 구조는 **LIFO**(Last In First Out)이다.
  - 새로운 액티비티가 나타나면 **push**되고, back button을 클릭하면 **pop**된다.

![image-20210820112147622](..\image\image-20210820112147622.png)

- **액티비티에서 다른 액티비티를 띄우거나 back button을 클릭하면?**

  - 현재 상태의 액티비티는 stopped 되고, resume을 대기하게 된다.

    

- **여러개의 스택이 쌓인 상태에서 Home버튼을 클릭한다면?**

  - 여러개의 스택이 모두 stopped되고, 최상단의 액티비티가 resume을 대기하게 된다.



- <span style="color:red">**태스크 관리**</span>

  - 기본적으로 안드로이드에서 작업과 백스택을 관리하는 방식은 LIFO(Last In First Out)이다.

  - 그러나 몇 가지 속성 설정을 통해서

    - 현재 태스크에 배치되지 않고 새 태스크가 시작되도록 할 수 있다.

    - 또는 작업에서 떠날때 루트 액티비티를 제외한 모든 액티비티가 백스택에서 제거되도록 할 수 있다.

      

- manifest의 **\<activity>  속성**

  - taskAffinity

  - launchMode

    - standard : 기본값. 액티비티는 여러번 인스턴스화 될 수 있고, 각 인스턴스는 서로 다른 작업에 속할 수 있으며, 한 작업에는 여러 인스턴스가 있을 수 있다.

    - singleTop :  인스턴스가 이미 작업의 최상단에 있으면, 액티비티의 새 인스턴스를 생성하지 않고 **onNewIntent()** 메소드를 호출하여 인텐트를 기존 인스턴스로 라우팅한다.

      ex) A-B-C-D (D가 최상단) 만약 D에 **standard** 실행 모드가 있으면 스택이 A-B-C-D-D가 된다.

      그러나 D의 실행모드가 **singleTop**이면 D의 기존 인스턴스는 **onNewIntent()**를 통해 돡한 인텐트를 받고, 기존의 인스턴스가 스택의 맨 위에 있기 때문에 스택은 A-B-C-D로 유지된다.

    - singleTask : 새 작업을 생성하고, 새 작업의 루트에 있는 활동을 인스턴스화 한다. 이미 별도의 작업이 있으면 새 인스턴스를 생성하지 않고 **onNewIntent()** 메소드를 호출하여 인텐트를 기존 인스턴스로 라우팅한다.

    - singleInstance : singleTask와 동일하지만 시스템이 인스턴스를 보유한 작업으로는 어떤 다른 활동도 실행하지 않는다는 점이 다르다. 

  - allowTaskReparenting

  - clearTaskOnLaunch

  - alwaysRetainTaskState

  - finishOnTaskLaunch

  > 더 자세한 내용은 [https://developer.android.com/guide/topics/manifest/activity-element?hl=ko]( https://developer.android.com/guide/topics/manifest/activity-element?hl=ko ) 

- **Intent Flag**

  - **FLAG_ACTIVITY_NEW_TASK**

    - 액티비티 새 작업에서 시작한다. 지금 싲가하고 있는 액티비티에 대해 이미 실행 중인 작업이 있으면 그 작업이 마지막 상태가 되어 포그라운드로 이동하고 액티비티는 **onNewIntent()**의 새 인텐트를 수신한다.

    - launch모드의 **singleTask**값과 동일한 동작이 발생한다.

    - 앱 A에 액티비티 A1, 앱 B에 액티비티 B1,  B2 존재한다고 가정

      **ex1)** 앱 A실행 => A1 호출 => B1 호출 (B1은 A1과 같은 태스크에서 관리 되어짐)
      
      <span style="color:green">**Task1 : A1-B1**</span>
      
      
      
      **ex2)** 앱 A실행 => A1 호출 => B1 호출 + FLAG_ACTIVITY_NEW_TASK (B1과 같은 affinity를 가지고 있는 태스크가 현재 존재하지 않기 때문에 태스크를 새로 생성하고 B1은 그 태스크의 RootActivity가 된다.)
      
      <span style="color:green">**Task1 : A1**</span> 

       <span style="color:green">**Task2 : B1**</span> 

      

      **ex3)** 앱 A,B 실행 => A1 호출 => B2 호출 + FLAG_ACTIVITY_NEW_TASK (앱B를 실행했으므로 B1은 Task2에 존재한다)

      B2와 동일한 affinity를 가진 태스크가 존재한다. B2는 B1과 같은 태스크에서 관리되고, B2에서 Back했을때 B1이 보여진다.

      <span style="color:green">**Task1 : A1**</span> 

       <span style="color:green">**Task2 : B1-B2**</span> 

      

      > **affinity란 ? :** 활동이 어느 태스크에 소속되기를 선호하는지를 나타낸다. 기본적으로 동일한 앱일 경우 동일한 affinity를 갖는다고 말한다. 이는 동일한 앱의 모든 액티비티들은 기본적으로 동일한 태스크안에 있는 것을 선호함을 의미한다. ( 그러나 \<activity>의 taskAffinity 속성을 이용해서 어피니티 수정이 가능하다. )
      
      

  - **FLAG_ACTIVITY_CLEAR_TOP**
  
    - 시작 중인 액티비티가 현재 액티비티(백스택의 맨위에 있는)이면 액티비티의 새 인스턴스가 생성되는 대신 기존 인스턴스가 **onNewIntent()** 호출을 수신한다.
    - launch모드의 **singleTop**값과 동일한 동작이 발생한다.

  - **FLAG_ACTIVITY_SINGLE_TOP**
  
    - 시작 중인 액티비티가 현재 작업에서 이미 실행중이면 액티비티의 새 인스턴스가 실행되는 대신 작업의 맨 위에 있는 다른 모든 액티비티가 제거되고 이 인텐트가 **onNewIntent()**를 통해 액티비티(이제 맨 위에 있음)의 다시 시작된 인스턴스로 전달된다.
    - 이와 동일한 launch 값은 없다.
  
    ![image-20210820140644720](../image/image-20210820140644720.png)

> 더 자세한 내용은 [https://developer.android.com/guide/components/activities/tasks-and-back-stack?hl=ko](https://developer.android.com/guide/components/activities/tasks-and-back-stack?hl=ko)

