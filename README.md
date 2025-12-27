# MSA Spring Cloud 실습 프로젝트

## 1. 프로젝트 개요

이 프로젝트는 **Spring Cloud 기반 MSA(Microservice Architecture)** 구조를 학습하고 검증하기 위한 실습용 레포지토리이다.

* Eureka Server / Client를 직접 구성하여 서비스 디스커버리 동작 확인
* API Gateway(Spring Cloud Gateway)에서 **MVC 기반 서비스와 WebFlux 기반 서비스**를 동시에 라우팅
* Gateway Filter를 **YAML 설정 방식**과 **Java 코드 방식** 두 가지로 구현하여 동작 차이 및 제어 범위 확인

### 실습할 프로젝트 내용 
- 상품 조회 , 사용자 조회 , 상품 주문을 각각 msa 환경을 통해 설계.
<img width="873" height="457" alt="image" src="https://github.com/user-attachments/assets/1f39e2a9-0465-4a37-92fc-c9250bb7b514" />



---

## 2. 프로젝트 구조

루트 프로젝트(`msa-root`)에서 **Gradle 멀티 모듈** 형태로 관리한다.

```
msa-root
 ├─ settings.gradle
 ├─ apigateway-service
 ├─ eureka-server
 ├─ eureka-client
 ├─ service_01
 └─ service_02
```

### settings.gradle

```gradle
rootProject.name = 'msa-root'

include (
  'eureka-server',
  'eureka-client',
  'service_01',
  'service_02',
  'apigateway-service'
)
```

---

## 3. 모듈별 역할

### 3.1 Eureka Server

* 서비스 레지스트리 역할
* 각 마이크로서비스 및 API Gateway가 등록
* 기본 포트: **8761**

### 3.2 Eureka Client

* Eureka Server에 등록되는 기본 Client 예제
* 서비스 등록 및 헬스체크 동작 확인용

### 3.3 API Gateway (apigateway-service)

* Spring Cloud Gateway 기반
* WebFlux 기반 비동기 라우팅 처리
* 요청/응답 Header를 필터로 가공
* YAML 설정 방식과 Java 코드 방식 모두 구현

### 3.4 service_01

* Spring MVC 기반 서비스
* Gateway를 통한 요청 수신

### 3.5 service_02

* Spring WebFlux 기반 서비스
* Gateway를 통한 요청 수신

---

## 4. Eureka 연동 설정

```yaml
server:
  port: 8000

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://localhost:8761/eureka

spring:
  application:
    name: apigateway-service
```

* 모든 서비스는 Eureka Server에 등록
* Gateway 역시 Client로 등록되어 서비스 디스커버리 가능

---

## 5. API Gateway 라우팅 및 필터 처리

### 5.1 YAML 기반 Route & Filter 설정

```yaml
spring:
  cloud:
    gateway:
      server:
        webflux:
          routes:
            - id: first-service
              uri: http://localhost:8081/
              predicates:
                - Path=/first-service/**
              filters:
                - AddRequestHeader=f-request, 1st-request-header-by-yaml
                - AddResponseHeader=f-response, 1st-response-header-from-yaml
            - id: second-service
              uri: http://localhost:8082/
              predicates:
                - Path=/second-service/**
              filters:
                - AddRequestHeader=f-request, 2st-request-header-by-yaml
                - AddResponseHeader=f-response, 2st-response-header-from-yaml
```

#### 특징

* 선언적 방식
* 설정 변경 시 코드 수정 불필요
* 운영 환경에서 빠른 수정 가능

---

### 5.2 Java 코드 기반 Route & Filter 설정

```java
public RouteLocator getRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
        .route(r -> r.path("/first-service/**")
            .filters(f -> f.addRequestHeader("f-request", "1st-request-header-by-java")
                .addResponseHeader("f-response", "1st-response-header-from-java"))
            .uri("http://localhost:8081"))
        .route(r -> r.path("/second-service/**")
            .filters(f -> f.addRequestHeader("s-request", "2nd-request-header-by-java")
                .addResponseHeader("s-response", "2nd-response-header-from-java"))
            .uri("http://localhost:8082"))
        .build();
}
```

#### 특징

* 코드 기반 제어로 조건 분기 및 커스터마이징 용이
* 공통 Filter, 복잡한 로직 처리에 적합

---

## 6. 학습 포인트 정리

1. Eureka Server / Client 동작 원리 이해
2. Spring Cloud Gateway의 WebFlux 기반 라우팅 구조
3. MVC 서비스와 WebFlux 서비스의 공존 구조
4. Gateway Filter의 적용 시점(Request / Response)
5. YAML 설정 방식 vs Java 코드 방식 비교

---

## 7. 실행 순서

1. Eureka Server 실행
2. service_01, service_02 실행
3. apigateway-service 실행
4. Gateway 엔드포인트로 요청

```text
http://localhost:8000/first-service/**
http://localhost:8000/second-service/**
```

---

## 8. 참고

* 본 프로젝트는 학습 및 검증 목적의 예제 프로젝트이다.
* 설정 및 구조는 실무 MSA 구성의 축소 버전이다.
