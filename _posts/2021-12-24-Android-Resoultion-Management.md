---
layout: post
title:  "Android Resolution Management"
date:   2021-12-24T00:00:00-00:00
author: sangyeop
categories: Android

---

###  기기별 dpi 차이 대응 하는 법

------

```
res
ㄴvalues-hdpi
ㄴvalues-mdpi
ㄴvalues-xhdpi
ㄴvalues-xxhdpi
ㄴvalues-xxxhdpi
```

해상도에 따른 별도의 res 파일을 만들어서 따로 관리한다.



###  dp란?

------

Pixel을 dpi에 상대적으로 계산한 값이 dp이다. 



###  레이아웃 동적 대응

------

```xml
<androidx.constraintlayout.widget.GuideLine
		android:id="@+id/vertical_guideline1"
    android:layout_width="wrap_content"
    android:layout_height="1dp"      
    android:orientation="vertical"
    app:layout_constraintGuide_percent="0.2"/>                                        
```

와 같이 constraintlayout에서 제공하는 가이드라인을 사용하여 두개의 가이드 라인 사이에 컴포넌트를 위치시키는 방법으로 균일한 레이아웃 유지가 가능하다.