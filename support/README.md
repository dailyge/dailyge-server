## ⛺️ Support 모듈

로깅, 모니터링 등 Api 모듈을 지원하는 역할을 담당합니다.

<br/><br/><br/>

## 👪 패키지 간 의존관계

Support 모듈은 다른 모듈에 의존하지 않습니다.

| Admin-Api | Dailyge-Api | Storage | Support |
|:---------:|:-----------:|:-------:|:-------:|
|     X     |      X      |    X    |    -    |

&nbsp;&nbsp; - Admin-Api: 관리자 Api, 스케줄링 모듈 <br/>
&nbsp;&nbsp; - Dailyge-Api: 사용자 서비스 Api 모듈 <br/>
&nbsp;&nbsp; - Storage: 저장소 모듈 <br/>
&nbsp;&nbsp; - Support: Api 지원 모듈 <br/>

<br/>
