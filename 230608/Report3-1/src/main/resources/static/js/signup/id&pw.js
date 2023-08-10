const mainlogin = document.querySelector(".confirm");
const idInput = document.querySelector(".main_login .id");
const pwInput = document.querySelector(".main_login .password");
const id_remove = document.querySelector("#id_remove");
const pw_remove = document.querySelector("#pw_remove");
const error_login = document.querySelector(".error_login");




function LoginSubmit(event){
 
}

 console.log(window.location.search);
let urlParams =decodeURI(window.location.search);
 console.log(urlParams);
 const hangulOnly = urlParams.replace(
  /[^\uAC00-\uD7AF\u1100-\u11FF\u3130-\u318F\uA960-\uA97F\uAC00-\uD7A3]/g,
  ""
);
if (urlParams) {
	
  error_login.innerHTML = hangulOnly;
} else {
  error_login.style.height = "0px";
}

mainlogin.addEventListener("submit",LoginSubmit);// submit했을때 해당 함수 실행


pwInput.addEventListener('keyup',()=>{
    pw_remove.style.visibility = "visible";
});
idInput.addEventListener('keyup',()=>{
    id_remove.style.visibility = "visible";
});
// 각각의 input에 글이 써지면 X박스 보이게 함

/******************************************************* */

id_remove.addEventListener("click",()=>{
    idInput.value = "";
    id_remove.style.visibility = "hidden";
})

pw_remove.addEventListener("click",()=>{
    pwInput.value = "";
    pw_remove.style.visibility = "hidden";

})
// x박스를 클릭하면 input에 있는 글 지우는 함수
// + 글이 지워지면 x박스는 다시 안보이게 됨

document.querySelector('form').addEventListener('submit', function(event) {
  event.preventDefault(); // 기본 폼 제출 동작 방지

  const usernameInput = document.querySelector('input[name="username"]');
  const passwordInput = document.querySelector('input[name="password"]');

  // 입력값 검증
  if (usernameInput.value.trim() === '') {
    alert('아이디 또는 이메일을 입력해주세요.');
    return;
  }

  if (passwordInput.value.trim() === '') {
    alert('비밀번호를 입력해주세요.');
    return;
  }
});



