
## 음식점 대기열 예약 웹 서비스 
<img src="https://github.com/chs8003/StoreReservation/assets/84433133/70430106-f08b-4a62-8b8b-862d1ec08ac4" alt="캡처" width="600px">

## ✅ 프로젝트 소개
매장을 방문하지 않아도 방문하고자 하는 매장의 현재 **대기자 수**를 확인하고

방문 전에 미리 본인을 **대기인 명부에 올릴 수** 있도록 하여 기존의 사용자가 매장까지 이동 후 대기자 명부를 작성하고 입장 전까지 

대기하는 이중적으로 낭비되는 시간을 단축할 수 있도록 도와주는 시스템


## ✅ 프로젝트 구상

1. 회원가입(로컬 로그인, 소셜 로그인)을 통한 개인정보 등록 기능 구현
2. 사용자의 위치에 따른 주변 식당 필터 기능 구현
3. 페이지 상단의 내비게이션 바를 통해 자신이 예약한 식당의 이름, 전화번호, 인원 수, 대기 번호를 알 수 있으며 대기열 취소 버튼을 누르면 대기열이 삭제되게끔       구현
4. 각 식당마다 대기열 번호를 나타내서 사용자가 대기열을 볼 수 있게끔 구현
5. 현재 식당이 예약 가능/불가능 상태를 구분지어 예약 불가능 상태일 때는 어둡게 나타내어 사용자에게 쉽게 판별할 수 있게 구현
6. 예약 가능한 식당일 경우 예약 방법 매뉴얼과 함께 인원과 메뉴를 골라 예약을 진행 한다. (단, 한 사용자마다 한 개 식당으로 제한)
7. 식당 예약을 했을 때, 해당 식당 이름과 대기열 번호, 대기열 삭제 페이지를 제공한다.

## 👩‍🚀👨‍🚀 팀 구성
|이름|담당|역할|
|:---:|:---:|:---:|
|최현식|Back-end|Spring을 통한 서비스 제작 <br>Postman을 활용한 가상 DB 데이터 통신 테스트 <br> MySQL을 통한 실제 데이터 입·출력|
|조진형|Front-end|html, CSS, 자바스크립트(js)로 페이지 구축 <br> Postman을 활용한 가상 DB 데이터 통신 테스트|
|조승준|Front-end|html, CSS, 자바스크립트(js)로 페이지 구축 <br> Postman을 활용한 가상 DB 데이터 통신 테스트|
|최용혁|Front-end||


## 📅 프로젝트 기간
2023.02.01 ~ 2023.11.31 


## 📚 기술스택 (Back-end)

<img src="https://img.shields.io/badge/Java-536DFE?style=flat-square&logo=Java&logoColor=white"/> 
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/> 
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=Spring Security&logoColor=white"/>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/>

## ⚙ 주요 기능(클릭)
<details>
<summary>회원가입 / 로그인</summary>
<div markdown="1">

![image](https://github.com/chs8003/StoreReservation/assets/84433133/3d66901e-4794-4cd0-8329-9f0699564d4f)
![image](https://github.com/chs8003/StoreReservation/assets/84433133/9361b9d2-7840-4c9e-a97b-0baed9e8930b)
<br>
**(1) 목적:** 자체 도메인 로그인과 회원가입을 번거로워 하는 유저들을 위해 소셜 로그인을 구현하였습니다.
<br>
**(2) 특징:** 로그인 페이지에서는 로그인 외에 카카오 로그인 같은 SNS 로그인이나 회원가입, 아이디/비밀번호 찾기가 가능합니다.

</div>
</details>

<details>
<summary>메인 페이지</summary>
<div markdown="1">

 ![image](https://github.com/chs8003/StoreReservation/assets/84433133/96cd5610-376c-445f-a6df-1f045e2c55a7) ![image](https://github.com/chs8003/StoreReservation/assets/84433133/471f796b-4c0b-4080-aaa2-9c7e17f14557)
 <br>
**(1) 특징**

1. 상단 부분엔 현재 대기 중인 식당의 정보를 확인할 수 있도록 창을 <br>
2. 중단 부분에는 필터를 이용하여 고객이 원하는 식당 출력할 수 있도록  <br>
3. 하단 부분에는 식당 데이터를 출력[**가게 이름, 리뷰 평점, 즐겨찾기 수 , 현재 대기열 번호 등**] 하였습니다.

-----------------------------------------------------------------------------------------------------------------------------------------------------------------
**● 메인 페이지 – 필터**

![image](https://github.com/chs8003/StoreReservation/assets/84433133/a405d02e-2211-40aa-983c-9aa7b85c17db)

1. [**내위치**] 버튼을 누르면 카카오 API를 사용해 현재 사용자에 위도와 경도를 받아와 그에 맞는 행정구역을 표시해줍니다.

![image](https://github.com/chs8003/StoreReservation/assets/84433133/3d4195eb-8a38-4f36-9803-a16b1acec3ae)

2. DB안에 있는 데이터와 일치하는 행정 구역에 맞는 식당이 출력되게 구현하였습니다.


</div>
</details>

<details>
<summary>식당 페이지</summary>
<div markdown="1">

 ![image](https://github.com/chs8003/StoreReservation/assets/84433133/bbef4a24-a22e-425d-a86c-f1607471f108)
 ![image](https://github.com/chs8003/StoreReservation/assets/84433133/7b9021b1-6985-4d1e-aac1-3c7c9a798ecf)
 ![image](https://github.com/chs8003/StoreReservation/assets/84433133/50241b60-a2bb-4d32-bfd3-9a544b879352)



**(1) 특징:**  방문자 수를 선택 후 하단에 있는 메뉴탭으로 가서 원하는 메뉴를 고르면 오른쪽에 플로팅 바가 선택한 정보를 보여주게 변하면서<br>
[**주문하기**] 버튼이 활성화가 되고 대기열 추가에 성공하게 되면 아래와 같이 창을 띄우게 됩니다.

![image](https://github.com/chs8003/StoreReservation/assets/84433133/6fefe771-b8d1-443b-aaef-42aae7cf1bc2)


**(2) 특징:**  처음 회원가입 시 입력했던 휴대포 번호로 성공했음을 알리는 창이 뜨면 문자를 통해 매장 이름과 매장의 대기 번호를 알리는 문자를 받을 수 있습니다.

</div>
</details>

<details>
<summary>마이페이지</summary>
<div markdown="1">

![image](https://github.com/chs8003/StoreReservation/assets/84433133/e34c33d8-3950-40ff-aa1b-6cb5312d3eb4)


**(1) 목적:** 마이페이지에서는 회원들의 정보를 수정할 수 있도록 설계 해보았습니다.

**(2) 특징:** <br>
1.  프로필 사진 변경 가능
2.  전화번호, 이메일 등 기본정보 변경 가능
3.  비밀번호 변경 가능

</div>
</details>

## ⚡ 트러블슈팅(해결해야할 점)

**1. 서버 부하 및 성능 문제:**

1. 캐싱 활용: 자주 업데이트되지 않는 정보는 캐싱을 통해 성능을 향상시킬 수 있습니다.
2. 스케일 아웃: 트래픽이 증가할 경우 서버를 수평으로 확장하여 부하를 분산시킬 수 있습니다.

<br>

**2. 데이터 관리 :**

1. 트랜잭션 관리: 예약과 관련된 데이터베이스 트랜잭션을 적절히 관리하여 일관성을 유지합니다. <br>
2. 데이터베이스 백업: 주기적으로 데이터베이스를 백업하여 데이터 손실을 방지합니다.
