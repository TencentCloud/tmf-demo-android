//  二维码扫描
function tmf_scan() {
    TMFJSBridge.invoke('scanQRCode', {}, function(res) {
        if (res.ret == 0) {
        tmf_logObject(res, 2);
         tmf_logObject({
                    result : res.result,  // string，当前 app 的版本号
                });
        } else {
            tmf_alert('扫码失败')
        }
    });
}