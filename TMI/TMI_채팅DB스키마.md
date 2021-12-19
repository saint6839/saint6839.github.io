####  채팅방 정보

|     필드     | 타입(크기) |                             설명                             |
| :----------: | :--------: | :----------------------------------------------------------: |
|    roomId    | string[30] |                 각 대화방 정의하는 고유한 ID                 |
| chatUpdateAt |   Bigint   |                   채팅메시지가 보내진 시간                   |
|    userId    |  array[]   | 채팅에 참여하는 유저들의 고유 ID( 본인 userId[0], 상대방 userId[1] ) |
| userNickname |  array[]   | 채팅에 참여중인 유저들의 닉네임<br />( 본인 userNickname[0], 상대방 userNickname[1] ) |
| userProfile  |  array[]   | 채팅에 참여중인 유저들의 프로필사진<br />( 본인 userProfile[0], 상대방 userProfile[1] ) |
|   userMBTI   |  array[]   | 채팅에 참여중인 유저들의 MBTI 정보 <br />( 본인 userMBTI[0], 상대방 userMBTI[1] ) |
| chatMessage  |  string[]  |       텍스트로 구성된 채팅내용을 저장하는 데이터베이스       |
|   isAlert    |  boolean   |                 해당 채팅방 알림 on/off 여부                 |

####  마지막 채팅 정보

|      필드       | 타입(크기) |                            설명                             |
| :-------------: | :--------: | :---------------------------------------------------------: |
|   lastChatId    | string[30] |             가장 마지막에 사용된 채팅 ID를 찾음             |
| lastChatContent | string[20] | 가장 마지막에 보내진 채팅내용을 각 채팅 항목에 한 줄로 표시 |

####  찜 목록

|       필드       | 타입(크기) |                     설명                     |
| :--------------: | :--------: | :------------------------------------------: |
|    likeUserId    | string[30] |   찜 목록에 추가하고자 하는 상대의 고유 ID   |
| likeUserNickname | string[8]  |   찜 목록에 추가하고자 하는 상대의 닉네임    |
| likeUserProfile  |  string[]  | 찜 목록에 추가하고자 하는 상대의 프로필 사진 |
|   likeUserMBTI   | string[5]  |    찜 목록에 추가하고자 하는 상대의 MBTI     |

