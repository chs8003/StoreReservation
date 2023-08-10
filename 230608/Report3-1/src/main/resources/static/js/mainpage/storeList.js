const Store_Zone = document.querySelector(".Store_Zone");

// db의 데이터를 가져와서 가게 리스트들을 나열
fetch("/api/mainpage")
  .then((res) => {
    return res.json();
  })
  .then(data => {
	  console.log(data);
      storeList(data);
      menuClick(data);
    }) 
  .catch((error) => {
    console.log(error);
  });

function storeList(data) {
  for (let i = 0; i < data.length; i++) {
    let adminCafe = data[i].adminCafe;
    let intro = data[i].storeIntroduce;
    let addr = data[i].addressName;
    let adminNo = data[i].adminNo;
    let reservable = data[i].reservable;
    let currentqueueNumber = data[i].currentqueueNumber
    let foodType = data[i].foodType;
    let review = data[i].reviewCount;  
    let avgRating = data[i].avgRating;

    let Openstores = document.createElement("div");
    let Closestores = document.createElement("div");
   
    //stores.setAttribute("value",i+1);
    if (reservable=== true) {
		    Openstores.setAttribute("value", foodType);
    Openstores.setAttribute("class", "store");

      Openstores.innerHTML = `<div class="Sign">
      <div class="Store_Sign">
        <div class="Store_Image"></div>
        <div class="Store_Name">
          <h4 class="Store_Title">${adminCafe}</h4>
          <p class="detail">${intro}</p>
          <div class="rating">
            <span class="Star">⭐</span>
            <span class="Star_Rating">${avgRating}</span>
            <span class="Review_Rating">(${review})</span>
          </div>
     <span class="tags">${foodType}</span><br>
          <span class="address">${addr}</span>
        </div>
      </div>
      <div class="WaitingBox">
       <p class="WaitingNum">${currentqueueNumber}</p>
      <p>팀</p>
    </div>
    </div>`;
    } else if (reservable === false) {
		 Closestores.setAttribute("value", foodType);

    Closestores.setAttribute("class", "store");

      Closestores.innerHTML = `<div class="CloseSign">
      <div class="Store_Sign">
        <div class="Store_Image"></div>
        <div class="Store_Name">
          <h4 class="Store_Title">${adminCafe}</h4>
          <p class="detail">${intro}</p>
          <div class="rating">
            <span class="Star">⭐</span>
            <span class="Star_Rating">${avgRating}</span>
            <span class="Review_Rating">(${review})</span>
          </div>
          <span class="tags">${foodType}</span><br>
          <span class="address">${addr}</span>
        </div>
      </div>
      <span class="bad">
      <p>대기</p>
      <p>불가</p>
      </span>
    </div>`;
    }
    Openstores.onclick = function (event) {
      sessionStorage.setItem("selectedValue", adminNo); //sessionStorage에 가게고유의 adminNo값 저장
        window.location.href = "/store?adminNo=" + adminNo;
    };
    Closestores.onclick = function (event) {
      sessionStorage.setItem("selectedValue", adminNo); //sessionStorage에 가게고유의 adminNo값 저장
      window.location.href = "/store?adminNo=" + adminNo;
    };
    Store_Zone.appendChild(Openstores);
    Store_Zone.appendChild(Closestores);
  }

}

// 음식 종류에 따른 가게 리스트 출력
function menuClick(data) {
  const sortedMenu = document.querySelectorAll("#sortedMenu");
  sortedMenu.forEach((el) => {
    el.addEventListener("click", function () {
      Store_Zone.innerHTML = "";

      let menus = el.parentElement.lastElementChild.innerHTML; //한식
      data.forEach((item) => {
        if (item.foodType === menus) {
          let adminCafe = item.adminCafe;
          let intro = item.storeIntroduce;
          let addr = item.addressName;
          let adminNo = item.adminNo;
          let open = item.open;
          let waitingNum = item.waitingNum;
          let foodType = item.foodType;
          let review = item.review_length; 
          let avgRating = item.avgRating;

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
                  <h4 class="Store_Title">${adminCafe}</h4>
                  <p class="detail">${intro}</p>
                  <div class="rating">
                    <span class="Star">⭐</span>
                     <span class="Star_Rating">${avgRating}</span>
           			 <span class="Review_Rating">(${review})</span>
         			 </div>
          			<span class="tags">${foodType}</span><br>
                  <span class="address">${addr}</span>
                </div>
              </div>
              <div class="WaitingBox">
              <p class="WaitingNum">${waitingNum}</p>
              <p>팀</p>
            </div>
            </div>`;
          } else if (reservable === false) {
			  Closestores.setAttribute("value", foodType);

          Closestores.setAttribute("class", "store");
            Closestores.innerHTML = `<div class="CloseSign">
              <div class="Store_Sign">
                <div class="Store_Image"></div>
                <div class="Store_Name">
                  <h4 class="Store_Title">${adminCafe}</h4>
                  <p class="detail">${intro}</p>
                  <div class="rating">
                    <span class="Star">⭐</span>
                     <span class="Star_Rating">${avgRating}</span>
          		 		 <span class="Review_Rating">(${review})</span>
        			  </div>
         		 <span class="tags">${foodType}</span><br>
                  <span class="address">${addr}</span>
                </div>
              </div>
              <span class="bad">
              <p>대기</p>
              <p>불가</p>
              </span>
            </div>`;
          }
          Openstores.onclick = function (event) {
            sessionStorage.setItem("selectedValue", adminNo); //sessionStorage에 가게고유의 adminNo값 저장
            window.location.href = "/store?adminNo=" + adminNo;
          };
          Closestores.onclick = function (event) {
            sessionStorage.setItem("selectedValue", adminNo); //sessionStorage에 가게고유의 adminNo값 저장
              window.location.href = "/store?adminNo=" + adminNo;
          };
          Store_Zone.appendChild(Openstores);
          Store_Zone.appendChild(Closestores);
        }
      });
    });
  });
}