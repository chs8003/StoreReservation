const HomeName = document.querySelector(".HomeName");
const logout2 = document.querySelector(".logout2");

fetch("/userinformation")
  .then((res) => {
    return res.json();
  })
  .then((data) => {
    console.log(data.userCafe);
    HomeName.innerHTML = data.userCafe;
  })
  .catch((error) => {
    console.log(error);
  });
  
  
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
logout2.addEventListener("click",admin_logout);