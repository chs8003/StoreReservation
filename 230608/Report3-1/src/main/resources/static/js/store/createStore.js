const nameStore = document.querySelector(".name");
const storeIntro = document.querySelector(".storeIntro");
const storePlace = document.querySelector(".storePlace");
const addr = document.querySelector(".AddressInfo");
const Menu2 = document.querySelector(".Menu");
const menuName1 = document.querySelector(".MenuName1");
const menuName2 = document.querySelector(".MenuName2");
const menuPrice1 = document.querySelector(".MenuPrice1");
const menuPrice2 = document.querySelector(".MenuPrice2");
const call = document.querySelector(".CallInfo");
const week = document.querySelector(".Week");
const openTime = document.querySelector(".OpenTime");
const closeTime = document.querySelector(".CloseTime");
const modal = document.querySelector(".modal");
const CostNum = document.querySelector(".CostNum");
const NameText = document.querySelector(".NameText");
const OrderText = document.querySelector(".OrderText");
const PhoneText = document.querySelector(".PhoneText");
const NumText = document.querySelector(".NumText");
const modalbtn = document.querySelector(".modal-foot");
const peopleBox = document.querySelector(".people");
const ReviewZone = document.querySelector(".ReviewZone");
const PhotoNum = document.querySelector(".PhotoNum");
const ReviewCount = document.querySelector(".ReviewCount");
const Imgmodal = document.querySelector(".Imgmodal");
const modalImg = document.querySelector(".Img-modal-content");
const Imgclose = document.querySelector(".Imgclose");
const clientNum = document.querySelector(".clientNum");
const ImgLeft = document.querySelector(".Img_Left");
const ImgRight = document.querySelector(".Img_Right");
const heart = document.querySelector(".fa-heart");
const rating = document.querySelector(".rating");
const staravg = document.querySelector(".staravg");
const heart_text = document.querySelector(".hart p")
const star =document.querySelector(".star");

let currentPhotoIndex = 0; // 현재 보여지고 있는 사진의 인덱스
let photos = [];

const uni = sessionStorage.getItem("selectedValue");
const url = `/api/store?adminNo=${uni}`;
fetch(url)
  .then((res) => {
    return res.json();
  })
  .then((data) => {
	console.log(data.reservable);
    openingDay(data);
    renderPage(data);
    addr.innerHTML=`${data.addressName}`;
    call.innerHTML = `${data.storePhone}`;
    openTime.innerHTML = `${data.openingTime}`;
    closeTime.innerHTML = `${data.closingTime}`;
    nameStore.innerHTML = `${data.adminCafe}`;
    storePlace.innerHTML = `${data.guName}`;
    storeIntro.innerHTML = `${data.storeIntroduce}`;
    heart_text.innerHTML = `${data.favoriteCount}`;
   	staravg.textContent = data.avgRating;
    console.log(data.favoriteCount);
     if (data.reservable === false) {
      peopleBox.innerHTML = `<div class="ClosingBox">
      <span class="ClosingText"> 예약이 불가능 한 매장입니다.</span>
      </div>`
    }``
  })
  .catch((err) => console.log(err));
  
  
  const uni2 = sessionStorage.getItem("selectedValue");
const url2 = `/findReviews?adminNo=${uni2}`;
fetch(url2)
  .then((res) => {
    return res.json();
  })
  .then((data) => {
    ReviewBoxes(data);
  })
  .catch((err) => console.log(err));
  
  
  
  
fetch("/userinformation")
  .then((res) => res.json())
  .then((data) => {
    OrderText.innerHTML = data.userName;
    PhoneText.innerHTML = data.userPhone;
    adminNo = sessionStorage.getItem("selectedValue");
    userNo = data.userNo;
    console.log(userNo);

    updateHeartButton(data,adminNo, userNo);
  });

fetch("/userinformation1")
  .then((res) => res.json())
  .then((data) => {
    OrderText.innerHTML = data.name;
    PhoneText.innerHTML = data.phoneNumber;
    adminNo = sessionStorage.getItem("selectedValue");
    userNo = data.id;
    data.userName = data.name;
    console.log(userNo);

    updateHeartButton(data ,adminNo, userNo);
  });

function updateHeartButton(data,adminNo, userNo) {
  heart.addEventListener("click", function () {
	 
	 if(data.userName) {
    location.reload();
    if (heart.classList.contains("fa-regular")) {
      fetch("/favorites", {
        method: "post",
        body: JSON.stringify({
          adminNo: adminNo,
          userNo: userNo,
        }),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((response) => {
          response.json();
        })
        .catch((error) => {});

      heart.classList.remove("fa-regular");
      heart.classList.add("fa-solid");
      heart.style.color = "#ff0000";
    } else {
      fetch("/delete2/" + adminNo + "/" + userNo, {
        method: "delete",
      });

      heart.classList.remove("fa-solid");
      heart.classList.add("fa-regular");
      heart.style.color = "";
    }
  }
  
  
  else {
	  alert("로그인 후 이용해주세요");
  }
  });
}

 
  fetch("/favorite")
  .then((res) => res.json())
  .then((data) => {
    console.log(data);
    console.log(typeof uni, typeof data[0].restaurant.adminNo);
    console.log(typeof userNo,typeof data[0].userNo);
    if (uni === String(data[0].restaurant.adminNo) && userNo===data[0].userNo) {
      heart.classList.remove("fa-regular");
        heart.classList.add("fa-solid");
        heart.style.color = "#ff0000";
    } 
    else if(uni === String(data[0].restaurant.adminNo) && id===data[0].id){
		  heart.classList.remove("fa-regular");
          heart.classList.add("fa-solid");
          heart.style.color = "#ff0000";
	}
    else {
      heart.classList.remove("fa-solid");
        heart.classList.add("fa-regular");
        heart.style.color = "";
    }
  });

  


function renderPage(data) {
  for (let i=0; i<data.categories.length; i++) {
    // 카테고리별로 박스 만들어서 appendChild로 추가
    const menubox = document.createElement("div");
    menubox.innerHTML = `<a class="MenuItem">
        <h1 class="Menu${i+1}">${data.categories[i].categoryName}</h1>
        </a>`
    Menu2.appendChild(menubox);

    for (let j = 0; j < data.categories[i].menuList.length; j++) {
      const menu = data.categories[i].menuList[j];

      const menulist = document.createElement("div");
      menulist.innerHTML = `<a class="SubItem">
      <div class="MenuInfo">
      <div class="MenuName">
        <h4>
          <strong class="MenuName1">${data.categories[i].menuList[j].menuName}</strong>
        </h4>
      </div>
       <span class="MenuPrice1">${data.categories[i].menuList[j].menuPrice}</span>
      <span>원</span>
    </div>
    <div class="menuImg" style="background-image: url(${menu.menusavedNm})"></div>
    </a>`;
      Menu2.appendChild(menulist);

      PhotoTab.addEventListener("click", function () {
        Move1(
          window.pageYOffset +
            document.querySelector(".storePhoto").getBoundingClientRect().top
        );
      });
      ReviewTab.addEventListener("click", function () {
        Move1(
          window.pageYOffset +
            document.querySelector(".storeReview").getBoundingClientRect().top
        );
      });

      // ------ 메뉴추가
      const menu3 = document.querySelectorAll(".SubItem");

      menu3.forEach((item) => {
        item.addEventListener("click", function (e) {
          const line = document.querySelector(".bottomline");
          const menuBoxOuter = document.querySelector(".menuBoxOuter"); // 총합DIV을 맨 아래 넣기 위해 각각의 메뉴태그들을 감싸는 부모 태그를 만들었다.
          if (
            cart.every(
              (menu3) =>
                menu3.name !== item.childNodes[1].childNodes[1].innerText
            )
          ) {
            menuBoxOuter.innerHTML += `
                <div class="menuBox">
                      <div class="menuName">${item.childNodes[1].childNodes[1].innerText}</div>
                      <div class="menuCost">
                        <div class="menuCost_left">
                          <button class="delete" onclick="del(this)">X</button>
                          <div class="cost" value="${item.childNodes[1].childNodes[3].innerText}">${item.childNodes[1].childNodes[3].innerText}</div>
                          <div>원</div>
                        </div>
                        <div class="menuCost_right">
                          <button class="minus" onclick="minus(this)">-</button>
                          <div class="su">1</div>
                          <button class="plus" onclick="plus(this)">+</button>
                        </div>
                </div>`;
            const product = {
              name: `${item.childNodes[1].childNodes[1].innerText}`,
              quantity: 1,
              price: Number(item.childNodes[1].childNodes[3].innerText),
            };
            // 새로 추가될때마다 총합 태그는 가장 아래 위치됨
            cart.push(product);

            line.style.visibility = "visible";
            const costhap = document.querySelector(".costHap");
            const count = document.querySelector(".count");
            costhap.style.display = "flex"; // 합계 DIV 보이게 하기
            count.style.display = "block";
            costhap.innerHTML = `
                <div class="hapDiv">
                  <div class="hapname">합계: </div>
                  <div class="hap"></div>
                  <div>원</div>
                </div>
                `
                    count.innerHTML = `
                <button class="order">주문하기</button>`
                
                //모달창
                let orderBTN = document.querySelector(".order");
                let close = document.querySelector(".close");
				
				function button_ON_OFF(data){
					if (data.userName) {
                  orderBTN.addEventListener("click", function (event) {
                    event.preventDefault();
                    modal.style.display = "flex";
                  });
                  close.addEventListener("click", function (event) {
                    closeModal(modal);
                  });
                  hap();
                } else {
                  orderBTN.addEventListener("click", function (event) {
                    alert("로그인 후 사용해주세요");
                    orderBTN.disabled = true;
                  });
                }
				}
                fetch("/userinformation")
              .then((res) => res.json())
              .then((data) => {
                // 로그인 정보가 있을때
                button_ON_OFF(data);
              });
               fetch("/userinformation1")
              .then((res) => res.json())
              .then((data) => {
                 data.userName = data.name;
                button_ON_OFF(data);
              });
                  }
                });
              })
        }        
        const bar = document.createElement("hr");
        bar.className = "line";
        Menu2.append(bar)
		NameText.innerHTML = data.adminCafe;
     

        //예약하기 버튼 눌렀을 때 데이터 sent()함수
        function handleClick1() {
          sent()
          
        }
        //예약하기 버튼 눌렀을 때 성공했다는 메시지 출력 함수
       

        modalbtn.onclick = function() {
			
          handleClick1();
      
        };
    }
    
}

function ReviewBoxes(data) {
  for (let i = 0; i < data.length; i++) {
    const Reviewbox = document.createElement("div");
    let imageNamed = data[i].imageNamed;
    let name = data[i].name;
    let content = data[i].content;
    let date = data[i].date;
    
   


    ReviewCount.textContent = data.length;
    clientNum.textContent = data.length;

    Reviewbox.innerHTML = `<div class="ReviewOne">
    <div class="UserZone">
      <span class="UserProfile"></span>
      <h4 class="NickName">${name}</h4>
      <h4 class="rating"></h4>
    </div>
    <li class="Review">
      <div class="ReviewPhoto" style="background-image: url('/image/${imageNamed}')"></div>
      <div class="ReviewContent">${content}</div>
    </li>
    <div class="DateInfo">
      <span class="UserDate">${date}</span>
    </div>
  </div>`;
    ReviewZone.appendChild(Reviewbox);

    let photoItem = document.createElement("li");
    let image = document.createElement("img");
    photoItem.className = "Photo";
    image.src = '/image/' + imageNamed;
    photoItem.appendChild(image);
    PhotoNum.appendChild(photoItem);

    photos = document.querySelectorAll('.Photo');

    for (let j = 0; j < photos.length; j++) {
      const photo = photos[j];
      const imageSrc = photo.childNodes[0].src;

      photo.addEventListener('click', function () {
        ImgModal(imageSrc);
      });

    }
    let rating = document.querySelectorAll(".rating")    
    const Star = String(data[i].rating);
    let ratinglist = "";

    for (let i = 0; i < Star.length; i++) {
      if (Star[i] === "1") ratinglist += "⭐";
      else if (Star[i] === "2") ratinglist += "⭐⭐";
      else if (Star[i] === "3") ratinglist += "⭐⭐⭐";
      else if (Star[i] === "4") ratinglist += "⭐⭐⭐⭐";
      else if (Star[i] === "5") ratinglist += "⭐⭐⭐⭐⭐";
    }
    rating[i].innerHTML = ratinglist;
  }


Imgclose.addEventListener('click', function () {
  closeModal(Imgmodal); // 모달창 닫기 함수 호출
});
}


function Move1(element) {
  window.scroll({ top: element - 95, behavior: "smooth" });
}

// 운영요일 구하는 함수
function openingDay(data) {
  const day = String(data.weekday);
  let daylist = "";
  for (let i = 0; i < day.length; i++) {
    if (day[i] === "0") daylist += "일 ";
    else if (day[i] === "1") daylist += "월 ";
    else if (day[i] === "2") daylist += "화 ";
    else if (day[i] === "3") daylist += "수 ";
    else if (day[i] === "4") daylist += "목 ";
    else if (day[i] === "5") daylist += "금 ";
    else if (day[i] === "6") daylist += "토 ";
  }
  week.innerHTML = daylist;
}

function ImgModal(src) {
  Imgmodal.style.display = 'flex'; // 모달창 열기
  modalImg.src = src; // 모달창 이미지 변경
}

// 모달창 닫기 함수
function closeModal(e) {
  e.style.display = 'none';
}

function showPhoto(index) {
  const photo = photos[index];
  const imageSrc = photo.childNodes[0].src;
  ImgModal(imageSrc);
}

function showNextPhoto() {
  currentPhotoIndex++;
  if (currentPhotoIndex >= photos.length) {
    currentPhotoIndex = 0;
  }
  showPhoto(currentPhotoIndex);
}

function showPreviousPhoto() {
  currentPhotoIndex--;
  if (currentPhotoIndex < 0) {
    currentPhotoIndex = photos.length - 1;
  }
  showPhoto(currentPhotoIndex);
}

ImgRight.addEventListener('click', showNextPhoto);
ImgLeft.addEventListener('click', showPreviousPhoto);

