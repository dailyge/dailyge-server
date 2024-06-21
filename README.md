# Dailyge



<br/><br/><br/><br/>

## 모듈 연관관계

프로젝트에 사용된 모듈은 다음과 같습니다.

1. dailyge-api: 서비스 API 모듈입니다.
2. scheduler: 스케줄링 모듈입니다.
3. storage: 데이터베이스 모듈입니다.
4. support: 로깅, 모니터링 등 API 모듈을 지원하는 모듈입니다.

<br/><br/><br/><br/>

모듈간 연관관계는 다음과 같습니다.

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FVapYn%2FbtsH8x9vZig%2FHakoUuW7xkJ8mkrVunN851%2Fimg.png)

<br/><br/><br/><br/>

## 패키지 구조

```shell
.
├── dailyge-api
│     ......
│       └─ project
│           ......
│           └─ dailyge
│                └─ app
│                ├─ common
│                └─ core
│                    └─ user
│                       ├─ presentation     # Controller 계층
│                       ├─ facade           # 퍼사드 계층 (Optional)
│                       ├─ application      # Service 계층
│                       ├─ persistence      # Repository 계층
│                       └─ external         # 외부 호출 계층 (Optional)
├── scheduler     # 스케줄링 모듈
├── storage       # 데이터베이스 모듈
└── support       # 로깅, 모니터링 등 API 지원 모듈
```
