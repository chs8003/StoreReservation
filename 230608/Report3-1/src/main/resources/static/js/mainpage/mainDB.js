fetch("/waitlist")
    .then(res => {
        return res.json();
    })
    .then(data => {
		console.log(data);
        waitingBar(data);
    })
    .catch(error => {
        console.log(error);
    });
    
    
    fetch("/userinformation")
    .then(res => {
        return res.json();
    })
    .then(data => {
        Users(data);
    })
    .catch(error => {
        console.log(error);
    });
    
      fetch("/userinformation1")
    .then(res => {
        return res.json();
    })
    .then(data => {
		console.log(data);
        Users(data);
    })
    .catch(error => {
        console.log(error);
    });


function waitingBar(data) {
    let PartyBox = document.querySelector(".memberChange");
    let Name = document.querySelector(".RestaurantName p");
    let Num = document.querySelector(".Num");
    let Phone = document.querySelector(".call")

    Name.innerHTML = data[0].adminCafe;
    Num.innerHTML = data[0].queueNumber;

    PartyBox.addEventListener('mouseenter',function() {
        this.innerHTML = data[0].partySize;
    });
    PartyBox.addEventListener('mouseleave', function() {
        this.innerHTML = '';
    });

    Phone.addEventListener('mouseenter',function() {
        this.innerHTML = data[0].storePhone
    });
    Phone.addEventListener('mouseleave', function() {
        this.innerHTML = '';
    });
}




function Users(data) {
    let UserName = document.querySelector(".utilList_User");
   if (data.userName && data.userName.length) {
  UserName.innerHTML = data.userName;
} else if (data.name && data.name.length) {
  UserName.innerHTML = data.name;
} else {
  UserName.innerHTML = "";
}
}


