<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Index</title>
    <link rel="stylesheet" type="text/css" href="./statics/css/index.css">
    <script type="text/javascript" src="./statics/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript">
        $(function () {
            $("button").click(function () {
                var url = "GetTicket";
                $.get(url, function (ticket) {
                    var src = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
                    $("img").attr("src", src);
                });
            });
        });
    </script>
</head>
<body>
<input type="button" value="生成二维码"> <br/>
<img alt="" src="">
</body>
</html>