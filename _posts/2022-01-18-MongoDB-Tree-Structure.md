---
layout: post
title:  "How to build MonogDB Tree Structure "
date:   2022-01-18T00:00:00-00:00
author: sangyeop
categories: TIL

---

###  

### MongoDB 계층구조 만드는법

------

이번 TMI팀 HoneyBe 어플리케이션 프로젝트를 진행하면서, 각 사용자별로 별도의 데이터를 관리해야하기 때문에 사용자의 id 별로 데이터를 나누어주는 구조가 필요했다. 그렇기 때문에 파이어베이스 리얼타임 데이터베이스와 같은 parent-child로 이루어지는 tree 구조를 MongoDB에서 구현하고 싶었다.

파이어베이스의 경우에는 정말 단순하게 `push()`, `child()`, `parent()`등의 메소드를 이용해서 원하는 구조를 간단하게 만들 수 있었지만, MongoDB의 경우에는 관련내용을 구글링 하는 것이 쉽지 않았다( 검색 키워드를 잘못 생각했을지도 모른다 ).

이렇게 고민하던 중에 **카카오 i 개발문서**의 api를 참고해보라는 팀원분의 말을 듣고 해당 문서를 참고해 보았다.

[카카오 i 개발문서]: https://docs.kakaoi.ai/kakao_i_account/api/

해당 문서에서는 하나의 값 밑에 하위 값을 넣기 위해서 Object 타입을 사용해서 계층으로 구조를 표현하고 있었다. 여기까지만 해도 사실 이해가 잘 가지 않았는데, 팀원이 우리가 사용할 데이터베이스 구조라며 보내준 json 파일에서 비로소 이해를 할 수 있었다.

```json
 {
	"_id": "ObjectID", // mongoDB에서 생성되는 id

	// 가입 정보
	"u_id": "string",  // 사용자의 id
	"password": "string",  //사용자의 비밀번호
	"nickname": "string",  // 사용자의 닉네임
	// 기본 정보
	"age" : "integer", // 나이
	"gender" : "string", // 사용자의 성별
	"studentNumber" : "string", // 학번
	"department": "string", // 학과
	"mbti": "string", // MBTI
	"religion" : "string",  // 종교
	"location" : "Point",   // 위치
	"smoking" : "string", // 흡연 여부
	"dringking" : "string", // 음주
	"height": "integer" // 가입자의 키
	"user_image" : "string[]", // 자신의 프로필 이미지
	// 나의 관심사
	"interest":{
		"exercise": "integer", 
		"hiking": "integer",
		"sports": "integer",
		"art": "integer",
		"museums": "integer",
		"reading": "integer",
		"shopping": "integer",
		"dining": "integer",
		"theater": "integer",
		"concerts": "integer",
		"movies": "integer",
		"tv": "integer", 
		"music": "integer"
	},
	"charming": "string[]",
	"personalitiy": "string[]",
	"introduce": "string",
	// 이상형
	"ideal_type":{
		"attractiveness": "integer",
		"sincerity": "integer",
		"intelligence": "integer",
		"fun": "integer",
		"ambition": "integer",
		"shared_interest": "integer"
	},
	// 어플 가입 목적
	"app_join":"string",
	// 관심있는 이성
	"pick_person": "string[]"
}
```

이 json 에서 interest와 ideal_type 을 보고, 하위 항목에 넣고 싶은 필드를 가진 클래스를 객체 필드로 넣으면 되겠다는 생각이 들어서 바로 SpringBoot의 Test 코드를 시험해 보았다.

**TmiData.class** - 메인 데이터 클래스

```java
@Data
@Document("tmiData")
public class TmiData {
    @Id
    String _id;
    String u_id;
    String password;
    String nickname;
    Integer age;
    String gender;
    String studentNumber;
    String department;
    String mbti;
    String religion;
    Point location;
    String smoking;
    String dringking;
    Integer height;
    String[] user_image;

    InterestData interestData;      // 관심사

    String[] interest;
    String[] personality;
    String introduce;

    IdealTypeData idealTypeData;    // 이상형

    String app_join;                // 어플 가입 목적
    String[] pick_person;           // string[]
		
  	// ... (빌더패턴 생성자 생략)
}

```



**InterestData.class** -관심사들에 대한 값을 담고있는 데이터 클래스

```java
@Data
@Document("interestData")
public class InterestData {
    Integer exercise;
    Integer hiking;
    Integer sports;
    Integer art;
    Integer museums;
    Integer reading;
    Integer shopping;
    Integer dining;
    Integer theater;
    Integer concerts;
    Integer movies;
    Integer tv;
    Integer music;
  
 		// ... (빌더패턴 생성자 생략)
}
```



**IdealTypeData.class** - 이상형 정보에 대한 값을 담고있는 데이터 클래스

```java 
@Data
@Document("idealTypeData")
public class IdealTypeData {
    Integer attractiveness;
    Integer sincerity;
    Integer intelligence;
    Integer fun;
    Integer ambition;
    Integer shared_interest;
  
   	// ... (빌더패턴 생성자 생략)
}
```

이제 위 클래스를 테스트코드를 이용해서 MongoDB에 어떤식으로 들어가는지를 테스트 해보았다.

```java
@SpringBootTest
class TmiDataRepositoryTest {
    @Autowired
    private TmiDataRepository tmiDataRepository;

    @Test
    void save() {
        //given
        InterestData interestData = InterestData.builder()
                .art(0).concerts(0).dining(0).exercise(2)
                .hiking(3).movies(10).museums(5).reading(7)
                .music(8).shopping(12).sports(2).theater(7).tv(3)
                .build();

        IdealTypeData idealTypeData = IdealTypeData.builder()
                .ambition(10).fun(4).attractiveness(3).intelligence(5)
                .shared_interest(5).sincerity(3)
                .build();

        TmiData tmiData = TmiData.builder()
                .interestData(interestData).idealTypeData(idealTypeData)
                .age(20).app_join("fun").department("ie").dringking("drink")
                .gender("man").height(180).interest(new String[]{"1", "2", "3"})
                .introduce("자기소개").location(new Point(30, 30)).mbti("ISTP")
                .nickname("queque").password("passwd").personality(new String[]{"test1", "test2"})
                .pick_person(new String[]{"aPerson", "bPerson"}).religion("christ")
                .smoking("smoking").studentNumber("164761").u_id("userId")
                .user_image(new String[]{"img1", "img2", "img3"}).build();

        //when
        tmiDataRepository.save(tmiData);
        List<TmiData> all = tmiDataRepository.findAll();
        //then
        Assertions.assertThat(all.get(0).getInterestData().getHiking()).isEqualTo(3);
    }
```

다음과 같이 실행하면 검증을 통과하고 MongoDB에 성공적으로 저장된다. 

![mongodb-tree](/Users/chaesang-yeob/Desktop/blog/saint6839.github.io/assets/mongodb-tree.png)

