<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>测试Jsbridge传输大数据图片</title>
    <link rel="stylesheet" href="common.css">
    <script type="text/javascript" src="common.js"></script>
    <style type="text/css">
        img {
            width: 100%;
            height: 300px;
            background-repeat: no-repeat;
            margin-top: 5px;
        }
        input {
            width: 100%;
            height: 40px;
            margin-top: 5px;
        }
    </style>
    <script type="text/javascript">
        var data;
        function logObject(object) {
            console.log(JSON.stringify(object))
        }

        function tmf_ready(callback) {
            if (window.TMFJSBridge) {
                callback && callback();
            } else {
                document.addEventListener("TMFJSBridgeReady", callback, false);
            }
        }

        function loadBigImage() {
            TMFJSBridge.invoke('loadBigImage', {}, function (res) {
                data = res;
                var img1 = document.getElementById("img1");
                img1.src = res.img1;
                var img2 = document.getElementById("img2");
                img2.src = res.img2;
                var img3 = document.getElementById("img3");
                img3.src = res.img3;
                var img4 = document.getElementById("img4");
                img4.src = res.img4;
                var img5 = document.getElementById("img5");
                img5.src = res.img5;
            });
        }

        function getBase64Image(img) {
            var canvas = document.createElement("canvas");
            canvas.width = img.width;
            canvas.height = img.height;
            var ctx = canvas.getContext("2d");
            ctx.drawImage(img, 0, 0, img.width, img.height);
            var dataURL = canvas.toDataURL("image/png");
            return dataURL;
        }

        function getBase64Image1(img) {
            var canvas = document.createElement("canvas");
            canvas.width = img.naturalWidth;
            canvas.height = img.naturalHeight;
            var ctx = canvas.getContext("2d");
            ctx.drawImage(img, 0, 0, img.naturalWidth, img.naturalHeight);
            var dataURL = canvas.toDataURL("image/png");
            return dataURL;
        }

        function saveBigImage() {
            var img1 = document.getElementById("img1");
            var img2 = document.getElementById("img2");
            var img3 = document.getElementById("img3");
            var img4 = document.getElementById("img4");
            var img5 = document.getElementById("img5");

            var param = {
                'img1':data.img1,
                'img2':data.img2,
                'img3':data.img3
            };

            TMFJSBridge.invoke('saveBigImage', param, function (res) {
                logObject(res)
            });
        }

        tmf_ready(function () {
            document.getElementById("load_img").addEventListener("click", loadBigImage);
            document.getElementById("save_img").addEventListener("click", saveBigImage);
        })
    </script>
</head>
<body>
<button id="load_img" type="button">加载图片</button>
<button id="save_img" type="button">保存图片</button>
<img id="img1"><br/>
<img id="img2"><br/>
<img id="img3"><br/>
<img id="img4"><br/>
<img id="img5"><br/>
</body>
</html>