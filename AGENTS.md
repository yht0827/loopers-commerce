# Repository Guidelines

## 프로젝트 구조 및 모듈 구성
- `apps/` — 실행 애플리케이션: `commerce-api`(REST API), `commerce-streamer`(스트림 처리), `pg-simulator`(결제 게이트웨이 시뮬레이터)
- `modules/` — 공용 라이브러리: `jpa`, `redis`, `cache`, `feign`, `resilience`, `kafka`
- `supports/` — 공용 설정 모듈: `jackson`, `logging`, `monitoring`
- `http/` — IntelliJ HTTP 요청 샘플, `k6/` — 부하/스트레스 테스트, `docker/infra-compose.yml` — 로컬 MySQL/Redis/Kafka

## 빌드 · 테스트 · 로컬 실행
- `./gradlew clean build` — 전체 모듈 컴파일 및 단위/통합 테스트 실행
- `./gradlew test` / `./gradlew :apps:commerce-api:test` — 전체 또는 모듈별 테스트
- `./gradlew jacocoTestReport` — 커버리지 리포트 생성(`build/reports/jacoco/test`)
- 앱 실행: `./gradlew :apps:commerce-api:bootRun`, `:apps:commerce-streamer:bootRun`, `:apps:pg-simulator:bootRun`
- 로컬 인프라: `docker compose -f docker/infra-compose.yml up -d` (MySQL, Redis, Kafka, Kafka‑UI)

## 코딩 스타일 · 네이밍 규칙
- Java 21(일부 Kotlin), 4‑space 들여쓰기. `.editorconfig`로 최대 130자(테스트 제외), 최종 개행 강제
- 패키지 레이어: `domain/`, `application/`, `infrastructure/`, `interfaces/`, `support/`
- API 컨트롤러: `*V1Controller`, API 스펙: `*V1ApiSpec`, DTO: `*Dto`, `*Request`, `*Response`

## 테스트 가이드
- JUnit 5, Spring Boot Test, Testcontainers(MySQL/Kafka) 사용
- 통합/E2E 테스트는 `modules:jpa`의 `DatabaseCleanUp`으로 테이블 정리
- 명명: 단위 `*Test`, 통합 `*IntegrationTest`, E2E `*E2ETest` (예: `apps/commerce-api/src/test/...`)
- JaCoCo로 커버리지 확인, 신규 코드에는 테스트 필수

## 커밋 · PR 가이드
- 권장 커밋(이모지 선택):
  - `:sparkles: feat: like aggregation consumer`
  - `fix: correct payment status update`
  - `refactor: extract event handler`
- PR: 변경 요약, 범위, 관련 이슈, 브레이킹 체인지/엔드포인트 변경 명시. API 변경 시 `http/` 샘플·문서 갱신, 필요 시 k6 결과/로그 첨부

## 보안 · 설정 팁
- 기본 프로필은 `local`. `application.yml`에서 `jpa.yml`, `redis.yml`, `kafka.yml`, `logging.yml`, `monitoring.yml`를 import
- 비밀정보 커밋 금지. 로컬은 Docker 인프라 기동 후 `bootRun`으로 앱 실행

## 에이전트/기여자 유의사항
- 실행 앱만 `BootJar` 생성, 라이브러리는 `java-library` 유지
- 새 코드/테스트는 올바른 모듈·레이어·패키지 경로에 추가하고 현행 네이밍을 따릅니다

## 에이전트 작업 원칙(실용 · 과공학 지양)
- 과도한 완벽주의 지양: 당장 요구사항을 충족하는 실용적 최소 변경(MVP) 우선.
- 불필요한 추상화/범용화/대규모 리팩터링 금지: 요청 범위 밖 변경이나 아키텍처 재설계는 하지 않음.
- 단순성 우선: 간단한 로직, 적은 파일/라인 변경, 현재 컨텍스트에 맞춘 해결을 선택.
- 조기 최적화 금지: 성능 이슈가 확인된 경우에만 최적화. 필요 시 `TODO`나 제안으로 남김.
- 테스트는 필수이나 과도한 커버리지 집착 금지: 핵심 경로와 회귀 방지에 집중.
- 모호하면 질문/가정 명시: 스펙이 불명확하면 가정을 짧게 기록하고 최소 구현.
- 단계적 개선: 확장 포인트나 대안은 문서/`TODO`로 남기고, 구현은 현재 범위에 집중.
