# 안드로이드 생명주기

안드로이드의 생명주기는 다음과 같다.

![activity_lifecycle](C:\Users\USER\Desktop\GitBlog\saint6839.github.io\image\activity_lifecycle.png)

- #### 주의할 점

  **불투명한 새로운 액티비티**가 최상단으로 올라오면 onPause()에 이어서 **onStop()**까지 바로 호출된다. 새로운 액티비티가 최상단으로 올라오면 기존 액티비티는 사용자와 더 이상 상호작용 할 수 없기 때문에, 포커스도 새로운 액티비티에 맞춰진다. 그렇기 때문에 onPause()가 호출되고, onStop()까지 호출된다.

  
