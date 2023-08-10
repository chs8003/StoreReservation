const nowplace = document.querySelector(".nowplace");
const place = document.querySelector(".place");
const find = document.querySelector(".find");
const store = document.querySelector(".store");


nowplace.addEventListener("click",function(){
    navigator.geolocation.getCurrentPosition(function(position){
        var geocoder = new kakao.maps.services.Geocoder();        
        var callback = function(result,status){
            if(status === kakao.maps.services.Status.OK){
                var location = "경기도 안양시 만안구 안양동";
                nowplace.style.display = "none";
                var div = document.createElement("div");
                var txt = document.createTextNode(`현재 위치는 ${location} 입니다.`);
                div.appendChild(txt);
                place.prepend(div);
                div.style.marginBottom = "30px";
                div.style.fontSize = "30px";
            }
        };
        geocoder.coord2RegionCode(position.coords.longitude,position.coords.latitude,callback);
    });
});
  
  
find.addEventListener("click",function(){
  navigator.geolocation.getCurrentPosition(function(position){
  let latitude = position.coords.latitude;
  let longitude = position.coords.longitude;
  fetch("/mainpage/location",{
  method:"POST",
  headers:{
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    latitude:latitude,
    longitude:longitude,
  }),
  })
  .then(res=>{
      return res.json();
  })
  .then(data=>{
      Store_Zone.innerHTML="";
        for(let i=0;i<data.length;i++){
            let adminCafe = data[i].adminCafe;
            let intro = data[i].storeIntroduce;
            let addr = data[i].addressName;
            let adminNo = data[i].adminNo;
            let foodType = data[i].foodType;
            let avgRating = data[i].avgRating;
            let review = data[i].reviewCount;
            
          let reservable = data[i].reservable;
    let currentqueueNumber = data[i].currentqueueNumber
    let Openstores = document.createElement("div");
    let Closestores = document.createElement("div");

    //stores.setAttribute("value",i+1);
    if (reservable === true) {
		Openstores.setAttribute("value", foodType);
            Openstores.setAttribute("class", "store");
      Openstores.innerHTML = `<div class="Sign">
      <div class="Store_Sign">
        <div class="Store_Image"></div>
        <div class="Store_Name">
          <h4 class="Store_Title">"${adminCafe}"</h4>
          <p class="detail">"${intro}"</p>
          <div class="rating">
            <span class="Star">⭐</span>
            <span class="Star_Rating">${avgRating}</span>
            <span class="Review_Rating">(${review})</span>
          </div>
     <span class="tags">${foodType}</span><br>
          <span class="address">"${addr}"</span>
        </div>
      </div>
      <div class="WaitingBox">
      <p class="WaitingNum">${currentqueueNumber}</p>
      <p>팀</p>
    </div>
    </div>`;
    }
    else if (reservable === false) {
		Closestores.setAttribute("value", foodType);
            Closestores.setAttribute("class", "store");
      Closestores.innerHTML = `<div class="CloseSign">
      <div class="Store_Sign">
        <div class="Store_Image"></div>
        <div class="Store_Name">
          <h4 class="Store_Title">"${adminCafe}"</h4>
          <p class="detail">"${intro}"</p>
          <div class="rating">
           <span class="Star">⭐</span>
            <span class="Star_Rating">${avgRating}</span>
            <span class="Review_Rating">(${review})</span>
          </div>
     <span class="tags">${foodType}</span><br>
          <span class="address">"${addr}"</span>
        </div>
      </div>
                <span class="bad">
              <p>대기</p>
              <p>불가</p>
              </span>
            </div>`;
    }
    Openstores.onclick = function (event) {
      sessionStorage.setItem("selectedValue", adminNo);   //sessionStorage에 가게고유의 adminNo값 저장
       location.href= "/store?adminNo="+adminNo;
    };
        Closestores.onclick = function (event) {
      sessionStorage.setItem("selectedValue", adminNo);   //sessionStorage에 가게고유의 adminNo값 저장
       location.href= "/store?adminNo="+adminNo;
    };
    Store_Zone.appendChild(Openstores);
    Store_Zone.appendChild(Closestores);

  }
  })
  .catch(error=>{
      console.log(error);
  });
    })
  })













/*
find.addEventListener("click",function(){       // 찾기 버튼 누를 시 가게 정보 보이게 함
    navigator.geolocation.getCurrentPosition(function(position){    // 현재 위치의 위도 경도 찾기
        var places = new kakao.maps.services.Places();
        let longitude = position.coords.longitude;
        let latitude = position.coords.latitude;
         $.ajax({
        type: 'POST',
        url: '/mainpage/location',
        data: {
			 latitude: latitude,
            longitude: longitude
          
        },
        success: function(data) {
            console.log(data);
        },
        error: function(error) {
            // 에러 처리 코드
        }
    });
});
});
*/