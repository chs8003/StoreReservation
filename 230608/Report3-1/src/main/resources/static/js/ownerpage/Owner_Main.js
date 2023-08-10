const Btn = document.querySelector('.Left');
const ON = document.querySelector('.OnText');
const OFF = document.querySelector('.OffText');
const logout = document.querySelector(".logout");
const HomeName = document.querySelector(".HomeName");

fetch("/userinformation")
  .then((res) => {
    return res.json();
  })
  .then((data) => {
    console.log(data.reservable);
    HomeName.innerHTML = data.userCafe;
    let adminNo_id = data.admin_id;
    Btn.addEventListener("click", () => {
	alert("예약상태가 변경되었습니다.")
	window.location.reload();
	
	 fetch('/toggleIsRevable', {
    method: 'POST'
  })
  });
  
	  fetch(`/api/store?adminNo=${adminNo_id}`)
  		.then((res) => {
    		return res.json();
  			})
  		.then((data2) => {
			 console.log(data2);
    	if (data2.reservable === true) {
       	 	ON.style.display = 'block';
        	OFF.style.display = 'none';
        	ON.innerHTML = "예약 가능"
        		
    	}
    	else if (data2.reservable === false) {
        	ON.style.display = 'none';
        	OFF.style.display = 'block';
            OFF.innerHTML = "예약 불가능"
    	}
 	 })
  })
  .catch(error => {
    console.log(error);
  });


// 로그아웃 버튼 이벤트
function admin_logout(){
	
 fetch('/logout', {
    method: 'POST'
  })
  .then(res => {
    if (res.redirected) {
      window.location.href = res.url;
    } else {
      console.log('Error logging out');
    }
  })
  .catch(error => console.log(error));
  }


// 로그아웃 버튼 이벤트
logout.addEventListener("click",admin_logout);