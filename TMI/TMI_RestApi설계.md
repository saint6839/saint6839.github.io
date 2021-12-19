#### [회원 가입 했을 때]

```
HTTP POST /users/age
HTTP POST /users/religion
HTTP POST /users/location
HTTP POST /users/smoking
HTTP POST /users/drinking
HTTP POST /users/college
HTTP POST /users/height
HTTP POST /users/mbti
HTTP POST /users/personality
```

#### [메인 화면 보여질 때]

```
HTTP GET /users/profile
HTTP GET /users/nickname
HTTP GET /users/age
HTTP GET /users/similarity
HTTP GET /users/interest (4개 정도)
HTTP GET /users/introduce
```

#### [상세 정보 화면 보여질 때]

```
HTTP GET /users/profile
HTTP GET /users/nickname
HTTP GET /users/age
HTTP GET /users/college
HTTP GET /users/location
HTTP GET /users/mbti
HTTP GET /users/interest (전부 출력)
HTTP GET /users/introduce
HTTP GET /users/smoking
HTTP GET /users/drinking
HTTP GET /users/height
```

