<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/othneildrew/Best-README-Template">
<img width="394" alt="스크린샷 2022-11-21 오후 3 48 47" src="https://user-images.githubusercontent.com/72961728/202983517-9f6f63fd-f17e-43dd-a12e-4fc1d6498baa.png">

  </a>

  <h3 align="center">ConCoder</h3>

  <p align="center">
    실시간 화상 코딩 교육 웹 플랫폼의 다중 접속 안정화 및 교육 특화 서비스 개발
    <br />
    : WebRTC 연결 구조 변경 및 CRDT를 이용한 동시 편집 화이트보드 기능 도입
    <br />
    <br />
     <a href="https://www.youtube.com/watch?v=9AfpqQOJ6YI">Watch Demo</a> ·
    <a href="">Check out Presentation Matrials</a> ·
    <a href="https://github.com/Charmull/ConCoder-FrontEnd">FRONTEND PROJECT</a>
  </p>
</div>

ConCoder는 ConCurrency + Coder의 합성어로, "동시에 코딩하는 사람들" 이라는 의미를 가지고 있다.

경희대학교 캡스톤디자인1 수업 프로젝트의 일환으로 **화상통화와 동시 프로그래밍 서비스를 결합**하여 편리하게 하나의 웹 사이트에서 이용할 수 있는 서비스를 개발하였다.

본 프로젝트는 기존 프로젝트에 **유동적인 WebRTC 연결 구조를 적용**하여 참가인원 변화에 따라 최적의 실시간성을 보장하고 다중 접속에 있어서 안정화하고자 하였다. <br />
또한 CRDT 동시 편집 알고리즘을 적용하여 기존 **코드 에디터에 동시성을 극대화**하고자 하였다. <br />
더해서 **동시 편집 화이트보드 기능**을 추가 제공함으로써 더욱 효율적인 코딩 교육이 가능하도록 해, <br />
기존 프로젝트와 비교하여 사용자 경험 향상을 목표로 하였다.

## Built With

높은 품질과 안정성을 보장하는 알고리즘 화상 교육 웹 서비스를 구현하기 위해 기본적인 웹 프레임워크에 WebRTC , Web Socket 통신, Multi-Thread 처리 등 여러 기술을 더하여 본 서비스를 개발하였다. <br />
더해서 다중 접속 안정화를 위해 WebRTC에 SFU 구조를 적용하였고, 동시 편집 기능들에 CRDT 알고리즘을 적용하였다.

##### FRONTEND

[![React][react.js]][react-url]

##### BACKEND

[![Spring][spring]][spring-url]

## 기능

#### 메인 페이지

✨ 가장 왼쪽의 섹터에서 백준에 있는 문제들의 정보를 열람할 수 있다. **번호 검색**을 지원한다. <br/>

✨ 가운데에는 **코드 에디터**가 위치해있고, 아래에 컴파일 버튼과 스냅샷 버튼, 드로잉 버튼으로 해당 기능들을 이용할 수 있다. 코드 에디터에는 충돌에 자유로운 동시 편집 알고리즘인 CRDT를 적용하여 동시에 여러 명의 참여자가 편집할 수 있도록 하였다.
<br/>

✨ **화이트보드**를 ON하면 작성한 코드 위에 그림을 그리거나 하이라이팅을 할 수 있다. 해당 기능 또한 CRDT를 적용하여 동시에 여러 명의 참여자가 필기를 할 수 있도록 구현하였다.
<br/>

✨ 우측 중앙에 **테스트케이스**를 등록하면 컴파일시 각 테스트케이스의 소요 시간과 성공 여부도 확인할 수 있다.
<br/>

✨ 가장 우측 상단에는 **실시간 스트리밍 캠**을 볼 수 있다. 최상단에는 본인의 캠을 확인할 수 있고, 마이크와 캠에 대한 ON/OFF 설정을 할 수 있다. 2인 참가 시 Mesh, 3인 참가 시 SFU 구조를 적용하여 참가인원 변화에 따라 최적의 실시간성과 안정성을 보장하고자 하였다.
<br/>

✨ 가장 우측 하단에는 **채팅** 기능을 제공한다. 다수의 참여자 접속에 대비하여 채팅 라벨을 클릭하면 Fold/UnFold 할 수 있도록 하였다.

<img width="1396" alt="스크린샷 2023-05-25 오후 3 52 29" src="https://github.com/Charmull/ConCoder-FrontEnd/assets/56704985/8afa5e11-a42d-4f5d-a177-bc649808534a">

<br/>

⚠️ 위에서 설명한 본 프로젝트의 메인 기능들은 데모 영상을 통해 확인 가능합니다. 대부분의 기능들이 실시간성/동시성이 부여된 기능인 만큼 영상을 통해 확인해주시길 바랍니다.

<br/>

#### 코드 스냅샷 및 불러오기 기능

<img width="1250" alt="KakaoTalk_Photo_2022-11-21-15-28-06 001" src="https://user-images.githubusercontent.com/72961728/202980416-86e45dba-4f7b-426b-b866-4ded989c008c.png">

<br/>

#### 타이머

<img width="1250" alt="KakaoTalk_Photo_2022-11-21-15-28-07 003" src="https://user-images.githubusercontent.com/72961728/202980441-6ac4ae5f-2c8c-414e-a337-a64d34a75659.png">

## Contacts

##### ❣️ FRONTEND

최지민 ✉️ 527wlals@khu.ac.kr

##### ❣️ BACKEND

최지민 ✉️ 527wlals@khu.ac.kr

<!-- MARKDOWN LINKS & IMAGES -->

[react.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[react-url]: https://reactjs.org/
[spring]: https://img.shields.io/badge/Spring-20232A?style=for-the-badge&logo=spring&logoColor=6DB53D
[spring-url]: https://reactjs.org/
