function logout() {
    $.getJSON("/logout",
        function (data) {
            if (data.code === 200) {
                window.location.href = "/";
            }
            layer.alert(data.msg);
        }
    )
}

function getUser() {
    $.ajax({
        type: "GET",
        url: "/token",
        success: function (data) {
            if (data.code === 200) {
                $("#user").val(data.data);
                $("#userName").text(data.data.nickname);
            } else {
                $("#user").val(null);
                $("#userName").text(null);
            }
            showUserBtn();

        }
    })
}

function showUserBtn() {
    let userName = $("#user").val();
    if (userName == null || userName === "") {
        $("#hasUser").hide();
        $("#noUser").show();
    } else {
        $("#noUser").hide();
        $("#hasUser").show();
    }
}

function countDown() {
    //秒杀还没开始
    let remainSeconds = $("#remainSeconds").val();
    let timeout;
    if (remainSeconds > 0) {
        $("#buyButton").attr("disabled", true);
        timeout = setTimeout(function () {
            $("#countDown").text(remainSeconds - 1);
            $("#remainSeconds").val(remainSeconds - 1);
            countDown();
        }, 1000);
    } else if (remainSeconds == 0) {
        $("#buyButton").attr("disabled", false);
        $("#miaoshaTip").text("秒杀进行中");
        if (timeout) {
            clearTimeout(timeout);
        }
    } else {
        $("#buyButton").attr("disabled", true);
        $("#miaoshaTip").html("秒杀已经结束");
    }
}