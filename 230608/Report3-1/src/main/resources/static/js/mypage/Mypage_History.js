const historyBox = document.querySelector(".historyBox");
const ReviewBoxes = document.querySelectorAll(".ReviewBox");
const modal = document.querySelector(".modal");
const close = document.querySelector(".close");
const Phone = document.querySelector(".PhoneNumText");
const Name = document.querySelector(".NameText");
const realUpload = document.querySelector('.Upload');
const PhotoContent = document.querySelector(".PhotoContent");
const PhotoText = document.querySelector(".PhotoContent p");
const PhotoImg = document.querySelector(".PhotoContent div");
const photo = document.querySelector(".Review_img");

// db의 데이터를 가져와서 가게 리스트들을 나열
fetch("/pagereview")
    .then(res => {
        return res.json();
    })
  .then((data) => {
	  storeList(data);
	  
   
    })
    .catch(error => {
        console.log(error);
    });

function storeList(data) {

    for (let i = 0; i < data.length; i++) {
        let phoneNumber = data[i].adminCafe;
        let name = data[i].name;
        let timeAdded = data[i].timeAdded;
         let adminNo = data[i].adminNo;

        let stores = document.createElement("div");

        stores.setAttribute("class", "store");

        stores.innerHTML = `<div class="Sign">
    <div class="Store_Sign">
        <div class="Store_Image"></div>
        <div class="Store_Name">
            <h4 class="Store_Title">"${phoneNumber}"</h4>
            <p class="detail">"${name}"</p>
            <div class="rating">
                <span class="Star">⭐</span>
                <span class="Star_Rating">4.5</span>
            </div>
            <span class="tags">연어 및 각종 일식</span><br>
            <span class="address">${timeAdded}</span>
        </div>
    </div>
    <div class="ReviewBox">
        <p class="ReviewContent">리뷰 작성</p>
    </div>
</div>`;

        stores.onclick = function (event) {
  		 sessionStorage.setItem("selectedValue", adminNo); //sessionStorage에 가게고유의 adminNo값 저장

         modal.style.display = "flex";
        };
        historyBox.appendChild(stores);

        close.addEventListener("click", function () {
            closeModal(modal)
        });

        Name.textContent = data.CustomerName;
    }

}

function change(){
    let selectFile = realUpload.files[0];
    const file = URL.createObjectURL(selectFile);

    photo.src = file;
    PhotoImg.style.display = "block"
    PhotoText.style.display = "none"
}

PhotoContent.addEventListener('click', () => realUpload.click());
realUpload.addEventListener('change', change);

// 모달창 닫기 함수
function closeModal(e) {
    e.style.display = 'none';
}



function submitForm() {
  // 폼 제출을 중단하여 페이지가 새로고침되는 것을 방지

  let form = document.querySelector(".reviewBox");
  let rating = form.querySelector('input[name="rating"]:checked').value;
  let fileInput = form.querySelector(".Upload");
  let file = fileInput.files[0];
  let content = form.querySelector(".content").value;
  let adminNo = sessionStorage.getItem("selectedValue");
 
  //console.log(rating, file, content);
  var formData = new FormData();
  formData.append("file", file);
  formData.append("rating", rating);
  formData.append("content", content);
  formData.append("adminNo", adminNo);

  // console.log(formData);
  // for (var key of formData.keys()) {
  //   console.log(key);
  // }

  // for (var value of formData.values()) {
  //   console.log(value);
  // }

  fetch("/reviews", {
    method: "POST",
    body: formData,
  })
    .then(function (response) {
      if (response.ok) {
        alert("리뷰가 성공적으로 등록되었습니다.");
      } else {
        alert("리뷰 등록에 실패했습니다. 다시 시도해주세요.");
      }
    })
    .catch(function (error) {
      console.log("네트워크 오류:", error);
    });
}

let form = document.querySelector(".reviewBox");
form.addEventListener("submit", submitForm);