## ⛺️ Storage 모듈

저장소에 대한 모듈 입니다. Document, Memory, RDB 모듈을 포함하고 있습니다.

1. Document: 문서화 데이터베이스(MongoDB)
2. Memory: 메모리 데이터베이스(Redis)
3. RDB: 관계형 데이터베이스(MySQL)

<br/><br/><br/>

## 👪 패키지 간 의존관계

Storage 모듈은 다른 모듈에 의존하지 않습니다.

| Admin-Api | Dailyge-Api | Storage | Support |
|:---------:|:-----------:|:-------:|:-------:|
|     X     |      X      |    -    |    X    |

&nbsp;&nbsp; - Admin-Api: 관리자 Api, 스케줄링 모듈 <br/>
&nbsp;&nbsp; - Dailyge-Api: 사용자 서비스 Api 모듈 <br/>
&nbsp;&nbsp; - Storage: 저장소 모듈 <br/>
&nbsp;&nbsp; - Support: Api 지원 모듈 <br/>

<br/>
