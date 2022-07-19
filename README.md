
# Android_DrawIt
> 저연령 대상 창의력 강화 미니게임 어플리케이션 (2021.04 ~ 2021.06) <br/> 
> Android Studio, Java, XML, SQLite

<img src="https://user-images.githubusercontent.com/87380790/126425351-6a231c07-5aca-4a20-8779-d4b850e53939.png" width="40%">


<br/>
<br/>
<br/>

## 1. 열매 받기 게임
> 떨어지는 열매를 받아야 하는 게임

<img src="https://user-images.githubusercontent.com/87380790/126425810-6df946d2-b2dc-4eb1-a5cf-c843a10dbcc0.png" width="32%"><img src="https://user-images.githubusercontent.com/87380790/175809913-dffab2ba-eb5a-47b9-8ffe-60c0e441b75c.gif" width="55%">
- 각각의 열매 객체는 랜덤 좌표와 랜덤 시간을 소유, 정해진 랜덤 시간 이후에 땅으로 낙하 <br/> 
- 드래그로 캐릭터를 움직이고, 낙하하는 열매와 바구니가 충돌시 점수 획득 <br/>
- 열매를 받지 못할 시 게임 오버

<br/>
<br/>
<br/>
 
## 2. 비행기 슈팅 게임
> 다가오는 적 비행기를 소탕하는 게임

<img src="https://user-images.githubusercontent.com/87380790/126426307-7d85bdcb-af89-472c-9d60-971461331d92.png" width="32%"><img src="https://user-images.githubusercontent.com/87380790/175809970-c114e086-0bdb-451c-a23b-e4f58836043a.gif" width="55%">
- 각각의 적 비행기 객체는 랜덤 좌표와 랜덤 속도를 소유, 정해진 랜덤 속도로 플레이어 쪽으로 비행 <br/> 
- 드래그로 비행기를 움직이고, 자동 발사되는 총알을 적 비행기에게 2번 맞출 시 점수 획득<br/>
- 적 비행기와 충돌 시 게임 오버

<br/>
<br/>
<br/>
 
## 3. 점프 게임
> 구름을 밟고 하늘로 점프하는 게임
> 
<img src="https://user-images.githubusercontent.com/87380790/126426437-0b3b6579-5384-4f35-b35e-f3c4efb697b7.png" width="32%"><img src="https://user-images.githubusercontent.com/87380790/175810002-09ca63b7-28cd-4735-8169-6ffa24017bc7.gif" width="55%">
- 각각의 구름 객체와 새는 랜덤 좌표를 소유 <br/> 
- 드래그로 캐릭터를 움직이고, 구름을 밟아 점프하여 점수 획득<br/>
- 새와 충돌 및 플레이어가 화면 밖으로 떨어질 시 게임 오버

<br/>
<br/>
<br/>
 
## 4. 모험 게임
> 동물과 장애물을 피해 사바나를 지나가는 게임
> 
<img src="https://user-images.githubusercontent.com/87380790/126426355-933552bf-46ab-47ba-b8fb-44c390ee427c.png" width="32%"><img src="https://user-images.githubusercontent.com/87380790/175809998-19e8192d-1d40-4b79-b597-2343a42dac42.gif" width="55%">
- 각각의 동물과 장애물은 고유한 좌표를 소유, 정해진 이동 경로와 속도에 따라 움직임 <br/> 
- 터치로 캐릭터를 움직이고, 이동한 거리에 따라 점수 획득<br/>
- 동물과 충돌하거나 장애물을 피하지 못할 시 게임 오버

<br/>
<br/>
<br/>
 
  
## 5. 회원 데이터베이스 저장
> 회원 가입, 로그인 기능, SQLite 를 활용하여 회원 데이터를 기기 내에 저장
  
<br/>
<br/>
<br/>

## 6. 최고 점수 랭킹 시스템
> SQLite를 이용하여 회원 데이터베이스로 같은 기기 내의 랭킹 시스템
<img src="https://user-images.githubusercontent.com/87380790/126427744-2160bc91-b82a-4a18-9260-89fe7c54e4da.png" width="30%">
  
<br/>
<br/>
<br/>
  
## 7. 오브젝트 간 충돌처리
> 객체마다 Rect 크기를 설정하고 Rect.intersects()를 이용하여 충돌 처리

<br/>
<br/>
<br/>
 
  
## 8. 터치 이벤트 설계
> 터치하는 곳으로 캐릭터를 이동시키는 게 아닌, 손가락을 움직이는 만큼만 캐릭터를 움직일 수 있게끔 onTouchEvent 설계

<br/>
<br/>
<br/>
 
  
## 9. 메인 오브젝트를 직접 그려 플레이
> Paint class를 활용한 그리기, 그림 저장 및 불러오기   by. 주세현(팀원)
> 
<img src="https://user-images.githubusercontent.com/87380790/126425601-fa6754c6-8b9d-4c76-a143-b58266fa50bf.png" width="30%"><img src="https://user-images.githubusercontent.com/87380790/126425595-b024d730-a6f8-498c-bbc7-f73242d07c65.png" width="30%">

<br/>
<br/>
<br/>
  
<br/>
