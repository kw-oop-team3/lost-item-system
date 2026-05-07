# 🔍 KW Lost and Found
> 광운대학교 캠퍼스 분실물 관리 시스템

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![OOP](https://img.shields.io/badge/OOP-Project-blue?style=flat-square)
![Status](https://img.shields.io/badge/Status-In%20Progress-yellow?style=flat-square)

---

## 📌 프로젝트 소개

광운대학교 캠퍼스 내에서 발생하는 분실물을 효율적으로 관리하기 위한 시스템입니다.  
관리자는 분실물을 등록하고 상태를 관리할 수 있으며,  
학생은 물건 이름, 발견 장소, 카테고리를 기준으로 분실물을 검색할 수 있습니다.

---

## 👥 팀원

| 이름 | 역할 | 담당 기능 |
|------|------|-----------|
| 전주용 | 사용자 관리 | `User`, `Student`, `Admin`, 로그인 로직 |
| 백지윤 | 분실물 도메인 | `LostItem`, `ItemService`, `ReturnService`, enum 클래스 |
| 황서영 | 검색 및 데이터 | `SearchStrategy`, `LostItemRepository` |

---

## 🛠️ 기술 스택

- **Language** : Java 17
- **IDE** : IntelliJ IDEA / Eclipse
- **협업** : GitHub

---

## 📂 프로젝트 구조

```
src/
└── com/kw/lostfound/
    ├── user/
    │   ├── User.java
    │   ├── Student.java
    │   └── Admin.java
    ├── item/
    │   ├── LostItem.java
    │   ├── Category.java
    │   ├── ItemStatus.java
    │   └── Location.java
    ├── repository/
    │   └── LostItemRepository.java
    ├── search/
    │   ├── SearchStrategy.java
    │   ├── NameSearchStrategy.java
    │   ├── LocationSearchStrategy.java
    │   └── CategorySearchStrategy.java
    ├── service/
    │   ├── ItemService.java
    │   └── ReturnService.java
    └── Main.java
```

---

## ⚙️ 주요 기능

### 👤 사용자 관리
- 학번 / 이름으로 로그인
- 학생과 관리자 권한 구분
  - 학생 : 분실물 조회 및 검색
  - 관리자 : 분실물 등록, 상태 변경, 반환 처리

### 📦 분실물 등록 및 관리
- 물건 이름, 카테고리, 발견 장소, 보관 장소, 발견 날짜 입력
- 분실물 상태 관리 (보관 중 / 반환 완료 / 폐기 예정)

### 🔍 검색 시스템
- 물건 이름으로 검색
- 발견 장소로 검색
- 카테고리별 검색

### 🔄 반환 처리
- 주인이 찾아간 물건의 상태를 반환 완료로 변경

---

## 💡 객체지향 설계 원칙 적용

| OOP 개념 | 적용 위치 | 설명 |
|----------|-----------|------|
| 추상화 | `User` 추상 클래스 | 학생/관리자 공통 속성 분리 |
| 캡슐화 | 모든 클래스 private 필드 | getter/setter로만 접근 |
| 상속 | `Student`, `Admin` | 역할별 권한 구현 |
| 다형성 | `SearchStrategy` 인터페이스 | 검색 방식을 런타임에 교체 가능 |
| Strategy 패턴 | 검색 시스템 | 검색 기준 추가 시 기존 코드 수정 불필요 |

---

## 🚀 실행 방법

1. 레포지토리 클론
```bash
git clone https://github.com/{팀장_깃허브_아이디}/kw-lost-and-found.git
```

2. 프로젝트 열기
```
IntelliJ IDEA 또는 Eclipse에서 프로젝트 열기
```

3. Main.java 실행
```
src/com/kw/lostfound/Main.java 실행
```

---

## 📅 개발 일정

| 기간 | 내용 |
|------|------|
| 5월 초 | 주제 선정 및 역할 분담 |
| 5월 초 ~ 중순 | 기능별 구현 |
| 5월 14일 | 통합 및 테스트 |
| 5월 17일 | 보고서 제출 |
| 5월 19일 or 21일 | 최종 발표 |

---

## 📁 제출물

- 프로젝트 보고서 (5페이지 이내)
- 프로젝트 소스코드

---

> 광운대학교 객체지향프로그래밍 수업 팀 프로젝트
