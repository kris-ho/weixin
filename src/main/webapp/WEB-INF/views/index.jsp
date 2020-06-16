<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Index</title>
    <link rel="stylesheet" type="text/css" href="css/index.css">
    <script type="text/javascript" src="js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript">
        $(function () {
            $(".btn").on("click", function () {
                    // alert("yes");
                    let url = "GetTicket";
                    $.get(url, function (ticket) {
                        let src = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
                        $("img").attr("src", src);
                    });
                });
        });
    </script>
</head>
<body>
<h2>扫码关注微信公众号</h2>
<input class="btn" type="button" value="生成二维码"> <br/>
<img alt="" src="">
</body>
</html>