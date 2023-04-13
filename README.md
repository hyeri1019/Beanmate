## Beanmate
개인 프로젝트 2022. 02. 03 ~ 현재 진행 중


RESTful-API 기반의 Single Page Application 입니다.

## 개발 환경
- Spring Boot
- React app
- Kakao API
- Spring Security
- JPA
- Mysql

## 개발 목적
- 클라이언트와 서버가 분리되어 의존성을 줄이고, 독립적으로 각자의 역할만 수행하는 구조로 설계
- 클라이언트 사이드 렌더링 방식으로, 서버는 view 에 전혀 관여하지 않고 데이터만 응답하는 형태
- 모든 페이지는 비동기식으로, 실제 변경된 부분만 재렌더링 되어 사용자 경험을 저해하지 않는 화면을 제공

## 주요 기능
- 카카오 API 를 이용한 카카오 로그인/회원가입 서비스
- Json Web Token 방식의 권한 및 인증 처리 진행
- 비동기 게시판 페이지네이션, 게시물 검색 기능, 이미지 첨부 기능, CRUD
- Spring Security 를 이용하여 여러 사용자의 여러 권한을 관리
- 후원 level 에 따른 게시물 공개 설정 기능
