# Dailyge



<br/><br/><br/><br/>

# Module

프로젝트에 사용된 모듈은 **`admin-api`**, **`dailyge-api`**, **`storage`**, **`support`** 모듈 입니다. 각 모듈의 기능은 다음과 같습니다.

1. **`admin-api`**: 관리자 API, 스케줄링 관련 모듈입니다.
2. **`dailyge-api`**: 사용자 서비스 API 모듈로 입니다.
3. **`storage`**: 데이터베이스 모듈입니다.
4. **`support`**: 로깅, 모니터링 등 API 모듈을 지원하는 모듈입니다.

<br/><br/><br/><br/>

도메인 모델과 영속 모델은 별도로 구분하지 않았으며, 영속 모델을 도메인 모델로 사용하고 있습니다. 도메인 모델로 인해 늘어나는 코드량, 이로 인한 유지보수 비용이 더 크다고 판단했기 때문입니다.

```shell
├─ 📁storage
│    ......
│       └─📁 dailyge
│             └─📁 entity   # 영속 모델
```

<br/><br/><br/><br/>

모듈간 의존관계는 다음과 같습니다. 불필요한 의존성 제거, 빌드 시간 단축을 위해 위해 대체로 runtimeOnly 또는 imeimplementation [구성(Configuration)](https://docs.gradle.org/current/userguide/declaring_dependencies.html)을 사용하고 있습니다.

<br/>

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcHD02o%2FbtsH8G6bEze%2FgkOXvPS5h9ZNeIdKRDl9VK%2Fimg.png)

<br/><br/><br/><br/><br/><br/><br/>

# Package

core 패키지는 서비스에 관한 기능을, common 패키지는 프로젝트에서 공통으로 사용되는 클래스 또는 설정을 포함하고 있습니다. 상위 계층은 하위 계층에 의존하지 않으며, 하위 계층의 존재를 알지 못합니다.

```shell
├─📁 dailyge-api
│     ......
│       └─📁 app
│           ├─📁 common                       # 프로젝트 공통 패키지
│           └─📁 core       
│             └─📁 user                       # 도메인
│                ├─📁 external                # 외부 시스템 호출 계층         - Optional
│                ├─📁 presentation            # 표면 계층  
│                ├─📁 facade                  # 퍼사드 계층                 - Optional
│                ├─📁 application             # 서비스 계층
│                └─📁 persistence             # 영속 계층
└── 

......

```

```text
각 계층의 의존성은 다음과 같습니다. 숫자가 작을수록 하위 계층입니다.
> Controller(1)  -->  Facade(2)  -->  Application(3), External(3)  -->  Persistence(4)
```

<br/><br/><br/><br/><br/><br/>

패키지 순환 참조를 막기 위해 주기적으로 패키지 사이클을 관리하고 있습니다.

![image]()
