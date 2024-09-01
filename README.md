<br/>

<div align="center">
  <img width="400" src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FCl3Zr%2FbtsH9tFrQxe%2F4Wd1nL8FuHANpzi9flDwBK%2Fimg.png"><br/><br/>
</div>

<div align="center">

Dailyge로 일정 관리를 간편하게! <br>
성실한 하루의 연속이 큰 변화를 가져옵니다. 🏃

[![Release](https://img.shields.io/badge/-📆_Web_Service-blue)](https://www.dailyge.com/) [![Release](https://img.shields.io/badge/-📚_API_Docs-brightgreen)]() <br/>
[![Release](https://img.shields.io/badge/%E2%9C%A8%20release-v0.0.0-brightgreen)](https://www.dailyge.com/)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=dailyge_dailyge-server&metric=coverage)](https://sonarcloud.io/summary/new_code?id=dailyge_dailyge-server)

</div>

<br/><br/><br/><br/><br/><br/>

# Table of Contents.

1. 서비스 소개
2. Contents
3. Skills
4. CICD
5. Architecture
6. Monitoring
7. Moduels
8. Package

<br/><br/><br/><br/><br/><br/><br/>

# 1. Service.

Dailyge는 일정을 체계적으로 관리하고 하루를 효과적으로 계획하여 중요한 일에 집중할 수 있도록 도와주는 서비스입니다.

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbGAESd%2FbtsJnrmbCzr%2FI3OIR1VM5LFYxuMnXrP9X1%2Fimg.png)

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FEVA0Y%2FbtsJnhxeyvw%2FxEpotIGhrNVG8Gb9VWlPxk%2Fimg.png)

<br/><br/><br/><br/><br/><br/><br/>

# 2. Contents.

- [JWT 토큰을 발행할 때, 어떤 점을 고려해야 할까?](https://github.com/dailyge/dailyge-server/discussions/107)
- [JWT 토큰을 파싱할 때, 어떤 예외가 발생할 수 있을까?](https://github.com/dailyge/dailyge-server/discussions/35)
- [JWT 토큰이 탈취 당한경우, 어떻게 해야 할까?](https://github.com/dailyge/dailyge-server/pull/33)
- [도메인 간, 쿠키가 공유되지 않을 때, 어떻게 해야 할까?](https://github.com/dailyge/dailyge-server/discussions/105)
- [JWT 토큰의 필드를 암호화하면 어떤 이점이 있고, 어떤 알고리즘이 사용될까?](https://github.com/dailyge/dailyge-server/discussions/122)
- [도메인 간, 쿠키가 공유되지 않을 때, 어떻게 해야 할까?](https://github.com/dailyge/dailyge-server/discussions/105)
- [데이터 압축하면 어떤 이점이 있을까? (feat.레디스)](https://github.com/dailyge/dailyge-server/discussions/86)
- [부하 테스트를 할 때, 톰캣의 어떤 설정들을 고려해야 할까?](https://github.com/dailyge/dailyge-server/discussions/84)
- [부하 테스트 과정에서 어떤 지표들을 모니터링 해야 할까?](https://github.com/dailyge/dailyge-server/discussions/82)

<br/><br/><br/><br/><br/><br/><br/>

# 3. Skills.

Backend, Infra, 협업에 사용된 기술 스택/툴은 다음과 같습니다.

- Backend

- Infra

- Collaboration

<br/><br/><br/>

## 🖥️ Backend.

Java/SpringBoot를 사용해 애플리케이션을 구축했습니다. Liquibase로 데이터베이스 스키마를 추적하고 있으며, 공개 API 문서는 RestDocs를, 개발 서버에는 Swagger + RestDocs를 조합해 사용하고 있습니다.

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FG9Nmi%2FbtsH9FZOYqq%2FmoUcUwmJZ4Mz9Lxz4LVKc1%2Fimg.png)

<br/><br/><br/><br/><br/><br/>

## ☁️ Infra.

서비스 구축을 위해 AWS를 활용했으며, 모니터링은 Prometheus와 Grafana를 사용하고 있습니다. 운영 로그는 Grafana Loki, 시스템 로그는 AWS CloudWatch로 관리하고 있으며, 운영 과정에서 발생하는 이슈는 AWS Lambda로 보고받고 있습니다. 비용 절감을 위해 일부 서버는 Google Cloud를 활용하고 있습니다.

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FRUR1K%2FbtsJebYQ9S7%2Fpr8dSua2YHDtpnNlQ6bdR1%2Fimg.png)

> 인프라에 대한 상세한 내용은 [해당 링크](https://github.com/dailyge/dailyge-infra)를 참조해주세요.

<br/><br/><br/><br/><br/><br/>

## 👬 Collaboration.

협업 툴은 이슈 트래킹을 위해 Jira/Confluencer를, 자동화 툴은 Zapier와 AWS EventBridge, Lambda, SNS/SQS를 사용하고 있습니다. 팀원 간 커뮤니케이션은 Slack으로 이루어지며, 또한 CICD 과정에서 발생한 리포트, AWS 비용 결과도 Slack으로 보고 받고 있습니다. 팀원 간 코드 컨벤션 관리 및 코드 스멜 제거를 위해 CheckStlye, PMD, SonarCloud와 같은 정적 코드 분석 툴을 사용하고 있습니다.

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fb7h7Xw%2FbtsJniCWIXE%2FhKO3sUXR9X4Oq4E30LXu00%2Fimg.png)

<br/><br/><br/><br/><br/><br/><br/>

# 4. CICD

PR이 생성되면 자동으로 정적 분석을 시작하며, Slack으로 결과를 보고받습니다. 팀원 간 코드 리뷰를 거친 후, dev 브랜치로 병합이 되면, 개발 서버로 배포가 되며, 인수 테스트가 시작됩니다. 이후 QA를 진행하며 기능의 동작 유무, 버그 리포팅을 합니다. 마지막으로 main 브랜치로 병합이 되면 상용 서버로 배포가 되고 최종 결과를 보고받습니다.

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FN1QMI%2FbtsJm6v06aM%2FuoKarw39V591Ii8FFUTk2k%2Fimg.png)

> 테스트 코드를 작성하지 않거나 테스트가 실패하는 경우, 또는 테스트 커버리지를 충족하지 못하면 빌드가 실패하게 됩니다. 개발서버에서 QA를 진행해 코드 레벨에서 발견할 수 없는 버그를 한 번 더 검증합니다.

<br/><br/><br/><br/><br/><br/>

# 5. Architecture

정적 자원은 S3와 CloudFront를, 서버 오케스트레이션은 AWS ECS를 사용했습니다. 각 리소스는 VPC 내부 별도의 서브넷(Public/Private)에 존재하며, ALB와 NAT를 통해 외부와 통신합니다. 부하 테스트를 할 때는 terraform을 통해 서버를 동적으로 확장하고 있으며, 평상시에는 최소 인스턴스만 사용하고 있습니다.

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FRUR1K%2FbtsJebYQ9S7%2Fpr8dSua2YHDtpnNlQ6bdR1%2Fimg.png)

<br/><br/><br/><br/><br/><br/><br/>

# 6. Monitoring

모니터링은 Prometheus와 Grafana를 CloudWatch와 연동해 사용하고 있으며, 이를 통해 알림을 받고 있습니다. 모니터링 중인 리소스는 EC2 서버, 애플리케이션 지표, RDS, Redis, MongoDB 이며, CPU/메모리 사용률, Slow Query 등을 체크하고 있습니다.

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FeEeYh0%2FbtsJfPz2TdW%2FDD2Zu0zqkZfkljdEFs7960%2Fimg.png)

<br/><br/><br/><br/><br/><br/><br/>

# 7. Module

프로젝트에 사용된 모듈은 **`admin-api`**, **`dailyge-api`**, **`storage`**, **`support`** 모듈 입니다. 각 모듈의 기능은 다음과 같습니다.

1. admin-api: 관리자 API 모듈 입니다.
2. dailyge-api: 스케줄 관리 서비스 API 모듈 입니다.
4. storage: 데이터베이스 모듈 입니다.
5. support 로깅, 모니터링 등 API 모듈을 지원하는 모듈 입니다.

<br/><br/><br/><br/>

도메인 모델과 영속 모델은 별도로 구분하지 않았으며, 영속 모델을 도메인 모델로 사용하고 있습니다. 도메인 모델로 인해 늘어나는 코드량, 이로 인한 유지보수 비용이 더 크다고 판단했기 때문입니다.

```shell
├─ 📁storage
│    ......
│       └📁 dailyge
│             └📁 entity   # 영속 모델

```

<br/><br/><br/><br/>

모듈간 의존관계는 다음과 같으며, 불필요한 의존성 제거, 빌드 시간 단축을 위해 위해 대체로 runtimeOnly 또는 imeimplementation [구성(Configuration)](https://docs.gradle.org/current/userguide/declaring_dependencies.html)을 사용하고 있습니다.

<br/>

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FeqqS6s%2FbtsJoC8aCoQ%2F8sbzZjmkmY6QH1FDLfHJ21%2Fimg.png)

<br/><br/><br/><br/><br/><br/><br/>

# 8. Package

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
각 계층의 의존성은 다음과 같습니다.
> Controller  -->  Facade  -->  Application, External  -->  Persistence
```

<br/><br/><br/><br/><br/><br/><br/>

# Contributors

<div align="center">

| [beatmeJy](https://github.com/beatmeJY) | [devjun10](https://github.com/devjun10) | [kmularise](https://github.com/kmularise) |
| :---: | :---: | :---: |
|<img width="150" src="https://avatars.githubusercontent.com/u/54700818?v=4">|<img width="150" src="https://avatars.githubusercontent.com/u/92818747?v=4">|<img width="150" src="https://avatars.githubusercontent.com/u/106499310?v=4">|
|**Backend, Frontend**|**Backend, Infra**|**Backend**|

</div>
