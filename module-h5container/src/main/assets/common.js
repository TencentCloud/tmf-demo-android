/******************** Log ********************/
function tmf_log(content, logType) {
    console.log(logType);
    if (logType == 1 || logType == null) {
        console.log(content);
    } else if (logType == 2) {
        setTimeout(function() { alert(content); }, 1);
    } else {

    }
}

function tmf_logObject(object, logType) {
    tmf_log(JSON.stringify(object), logType);
}

function tmf_href(url) {
    window.location.href = url;
}

var tmf_alert = function(message) {
    setTimeout(function() {
        alert(message);
    }, 10);
};

/******************** 初始化 ********************/
/**
 * 当 window.onload 后，H5 容器会初始化，TMFJSBridge 会注入容器，然后触发 TMFJSBridge 初始化完毕（TMFJSBridgeReady）事件。
 * TMFJSBridge 注入是一个异步过程，因此尽可能监听该事件后，再使用 TMFJSBridge.invoke 来调用 JS API。
 */
function tmf_ready(callback) {
    if (window.TMFJSBridge) {
        callback && callback();
    } else {
        document.addEventListener("TMFJSBridgeReady", callback, false);
    }
}

// 打开新页面
function tmf_openContainer(aUrl) {
    TMFJSBridge.invoke('openContainer', {
        url     : aUrl,    // string，必选，URL 地址
        params  : {                         // dictionary，可选，参数
            defaultTitle    : '',           // string，可选，默认标题
            showsContainerTitle  : true,         // bool，可选，是否展示页面标题
        },
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject();
    });
}