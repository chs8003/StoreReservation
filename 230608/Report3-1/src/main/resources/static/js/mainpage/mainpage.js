var slideIndex = 0; //slide index
const delete2 = document.querySelector(".button2");
const utilList_login = document.querySelector(".utilList_login");

const utilList = document.querySelector(".utilList");
const formps = document.querySelector(".formps");
fetch("/userinformation")
  .then((res) => res.json())
  .then((data) => {
    console.log(data);
     Users(data);
    if (data.userName) {
      let btn = document.createElement("button");
      utilList_login.addEventListener("click", function () {
        location.href = "/Mypage_Account";
      });
      btn.innerHTML = "로그아웃";
      formps.appendChild(btn);
    }
  })
  .catch((err) => console.log(err));
  
 fetch("/userinformation1")
  .then((res) => res.json())
  .then((data) => {
    console.log(data);
    Users(data);
    if (data.name) {
      utilList_login.addEventListener("click", function () {
        location.href = "/Mypage_Account";
      });

      let btn = document.createElement("button");
      let btn_Moblie = document.createElement("div");
      let btn_Myinfo = document.createElement("div");
      btn_Moblie.setAttribute("class", "logout");
      btn_Myinfo.setAttribute("class", "Myinfo");
      btn_Moblie.innerHTML = "로그아웃";
      btn_Myinfo.innerHTML = "내정보";
      btn.innerHTML = "로그아웃";
      formps.appendChild(btn);
      toggleOn.appendChild(btn_Myinfo);
      toggleOn.appendChild(btn_Moblie);
      btn_Myinfo.addEventListener("click", function () {
        location.href = "/Mypage_Account";
      });
    } else {
      let login = document.createElement("div");
      login.setAttribute("class", "login");
      login.innerHTML = "회원가입";
      toggleOn.appendChild(login);
      toggleOn.style.Height = "65px";
      login.addEventListener("click", function () {
        location.href = "/login";
      });
    }
  })
  .catch((err) => console.log(err));
  
 delete2.addEventListener("click", function () {
     fetch('/waitlist' ,{
		method:'GET'
        }).then(response => {
            if (response.status === 401) {
				alert("로그인 후 삭제해주세요");
                location.href = "/login"
            } else {
				alert("취소되었습니다.");
                return response.json();
            }
        })
        .then(data => {
			console.log(data[0].adminNo);
            const adminNo = data[0].adminNo;
            const userNo = data[0].userNo;
   
			
			if(!data[0].adminCafe.length) {
				alert("식당을 예약해주세요");
			}
			else {
            fetch('/delete/' + adminNo + '/' + userNo, {
                method: 'DELETE'
            }).then(response => {
                if (response.status === 401) {
                        location.href = "/login"
                } 
            }).catch(error => {
                console.log(error);
                
            });
            }
        })
        
        .catch(error => {
            console.log(error);
        });
      
});
 
 

// HTML 로드가 끝난 후 동작
window.onload=function(){
  showSlides(slideIndex);
  var sec = 3000;
  setInterval(function(){
    slideIndex++;
    showSlides(slideIndex);
  }, sec);
}

function moveSlides(n) {
  slideIndex = slideIndex + n
  showSlides(slideIndex);
}

function currentSlide(n) {
  slideIndex = n;
  showSlides(slideIndex);
}

function showSlides(n) {

  var slides = document.getElementsByClassName("mySlides");
  var dots = document.getElementsByClassName("dot");
  var size = slides.length;

  if ((n+1) > size) {
    slideIndex = 0; n = 0;    // 슬라이드가 다돌면 다시 처음으로 
  }else if (n < 0) {
    slideIndex = (size-1);    // 슬라이드가 첫번째에서 뒤로더가면 끝으로 이동
    n = (size-1);
  }

  for (i = 0; i < slides.length; i++) {
      slides[i].style.display = "none";   // 해당 
  }
  for (i = 0; i < dots.length; i++) {
      dots[i].className = dots[i].className.replace(" active", "");
  }
  slides[n].style.display = "block";
  dots[n].className += " active";
  
 }
 
 const toggleBar = document.querySelector(".headerUtil_toggleBar");
const subBar = document.querySelector(".toggleOn");
const header = document.querySelector(".header");

let toggle_info_Bar = true;
let toggle_search_Bar = true;

// 토클 버튼누르면 유틸바 내려옴
function showList() {
  if (toggle_info_Bar) {
    subBar.style.height = "120px";
    toggle_info_Bar = !toggle_info_Bar;
  } else {
    subBar.style.height = "0px";
    toggle_info_Bar = !toggle_info_Bar;
  }
}

//반응형일 떄 검색 버튼 함수
const search_box_moblie = document.querySelector(".search_box_moblie");
function showSearch() {

  if (toggle_search_Bar) {
    search_box_moblie.style.height = "120px";
    toggle_search_Bar = !toggle_search_Bar;
  } else {
    search_box_moblie.style.height = "0px";
    toggle_search_Bar = !toggle_search_Bar;
  }
}