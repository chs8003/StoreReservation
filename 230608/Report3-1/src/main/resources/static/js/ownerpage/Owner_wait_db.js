const wait_Zone = document.querySelector(".wait tbody");
const details = document.querySelector(".details");
const X = document.querySelector(".X-image");
const BlackBtn = document.querySelector(".BlackBtn");
const lockBtn = document.querySelector(".lockBtn");
const unlockBtn = document.querySelector(".unlockBtn");
const ReserveBtn = document.querySelector(".ReserveBtn");
const BlackPeople = document.querySelector(".BlackPeople");
const modal = document.querySelector(".modal");
const closeBtn = document.querySelector(".close");
const NameText = document.querySelector(".NameText");
const PhoneNumText = document.querySelector(".PhoneNumText");
const PeopleNumText = document.querySelector(".PeopleNumText");


fetch("/OwnerWait")
  .then((res) => {
    return res.json();
  })
  .then((data) => {
    waitingList(data);
  })
  .catch((error) => {
    console.log(error);
  });

  //adminNo,userNo에 데이터를 보낸는 함수
  function sendData1(adminNo, userNo) {
  const content = document.querySelector('textarea[name="content"]').value;
  const data = {
    userNo: userNo,
    adminNo: adminNo,
    content: content
  };

  fetch('/add/blacklist', {
    method: 'POST',
    body: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json'
    }
  })
    .then(response => {
      if (response.ok) {
        console.log('데이터 전송 성공');
      } else {
        console.log('데이터 전송 실패');
      }
    })
    .catch(error => {
      console.log('데이터 전송 중 오류 발생:', error);
    });
}

 function sendData2(adminNo, userNo, phoneNumber) {

     fetch('/admindelete/' + adminNo + '/' + userNo+`?phoneNumber=${phoneNumber}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json'
    }
  })
    .then(response => {
      if (response.ok) {
        console.log('데이터 전송 성공');
      } else {
        console.log('데이터 전송 실패');
      }
    })
    .catch(error => {
      console.log('데이터 전송 중 오류 발생:', error);
    });
}


 function sendData3(adminNo, userNo) {

     fetch('/remove/' + adminNo + '/' + userNo, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json'
    }
  })
    .then(response => {
      if (response.ok) {
        console.log('데이터 전송 성공');
      } else {
        console.log('데이터 전송 실패');
      }
    })
    .catch(error => {
      console.log('데이터 전송 중 오류 발생:', error);
    });
}

function waitingList(data) {
  for (let i = 0; i < data.length; i++) {
    let waitingName = data[i].name;
    let phoneNumber = data[i].phone_number;
    let waitingSize = data[i].party_size;
    let waitingQue = data[i].queueNumber;
    let blacklist = data[i].blacklisted;
    let userNo = data[i].userNo;
    let adminNo = data[i].adminNo;
    let timeAdded = data[i].timeAdded;
    let waitings = document.createElement("tr");
    if (blacklist === true) {
      waitings.className = "TRtable black";
    } else {
      waitings.className = "TRtable";
    }

    waitings.dataset.userNo = userNo;
    waitings.dataset.adminNo = adminNo;

    let WaiterName = document.querySelector(".WaiterName");
    let NumText = document.querySelector(".NumText");
    let CountNum = document.querySelector(".CountNumText");
    let PeopleText = document.querySelector(".PepleNumText");
    let DateText = document.querySelector(".DateText");

    waitings.innerHTML = `
            <tr class="TRtable">
              <td class="NumberTD">${i + 1} 번</td>
              <td class="NameTD">${waitingName}</td>
              <td class="MenTD">${waitingSize} 명</td>
              <td class="PhoneTD">${phoneNumber}</td>
            </tr>`;
    wait_Zone.append(waitings);
	let time = timeAdded;
	let people = waitingSize;
    let waitingsChildren = waitings.children;
    if (blacklist === true) {
      for (let i = 0; i < waitingsChildren.length; i++) {
        waitingsChildren[i].style.color = "red";
      }
    }

    waitings.addEventListener("click", function () {
      WaiterName.textContent = this.querySelector('.NameTD').textContent;
      NameText.textContent = this.querySelector('.NameTD').textContent;
      NumText.textContent = this.querySelector('.PhoneTD').textContent;
      PhoneNumText.textContent = this.querySelector('.PhoneTD').textContent;
      DateText.innerHTML = time;
      PeopleNumText.innerHTML = `${people}명`;

      if (this.classList.contains('black')) {
        lockBtn.style.display = "block";
        unlockBtn.style.display = "none";
        BlackPeople.style.display = "block";
      } else {
        lockBtn.style.display = "none";
        unlockBtn.style.display = "block";
        BlackPeople.style.display = "none";
      }

      lockBtn.addEventListener("click", function () {
        console.log(adminNo, userNo)
        sendData3(adminNo, userNo);
      });
      ReserveBtn.addEventListener("click", function () {
        console.log(adminNo, userNo)
        sendData2(adminNo, userNo,phoneNumber);
        alert("삭제되었습니다.");
      });
      BlackBtn.addEventListener("click", function () {
        console.log(adminNo, userNo)
        sendData1(adminNo, userNo);
      });

      showdetails();
    });
  }
}

function showdetails() {
  if (!details.classList.contains("show")) {
    details.classList.add("show");
  }
}

// 모달창 닫기 함수
function closeModal(e) {
  e.style.display = 'none';
}

X.addEventListener("click", () => {
  details.classList.remove("show");
});

unlockBtn.addEventListener("click", function () {
  modal.style.display = "flex";
});

closeBtn.addEventListener("click", function () {
  closeModal(modal)
});
