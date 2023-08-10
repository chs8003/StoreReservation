const delete1 = document.querySelector(".delete");

delete1.addEventListener("click", function () {

     fetch('/waitlist' ,{
		method:'GET'
        }).then(response => {
            if (response.status === 401) {
				alert("로그인 후 삭제해주세요");
                location.href = "/deletelogin"
            } else {
                return response.json();
            }
        })
        .then(data => {
            const adminNo = data[0].adminNo;
            const userNo = data[0].userNo;
            console.log(data[0].adminNo);

            fetch('/delete/' + adminNo + '/' + userNo, {
                method: 'DELETE'
            }).then(response => {
                if (response.status === 401) {
                        location.href = "/deletelogin"
                } else {
                }
            }).catch(error => {
                console.log(error);
            });
        })
        .catch(error => {
            console.log(error);
        });
});