function logout() {
    $.getJSON("/logout",
        function (data) {
            if (data.code == 200) {
                window.location.href = "/";
            }
            layer.alert(data.msg);
        }
    )
}