/******************** 页面上下文 ********************/

// 打开新页面
function tmf_openContainer(aUrl) {
    TMFJSBridge.invoke('openContainer', {
        url     : aUrl,    // string，必选，URL 地址
        params  : {                         // dictionary，可选，参数
            defaultTitle    : '这是标题',           // string，可选，默认标题
            showsContainerTitle  : true,         // bool，可选，是否展示页面标题
        },
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject();
    });
}

// 获取 H5 容器栈
// array，H5 容器栈，格式如下：
/*
 *  [
 *      { containerId: id_0, url: '...', }, // 容器 0
 *      { containerId: id_1, url: '...', }, // 容器 1
 *      { containerId: id_2, url: '...', }, // 容器 2
 *      // ...
 *      {                                   // 容器 N
 *        containerId: id_n,                // integer，容器 ID
 *        url: '...',                       // string，容器 URL
 *      }, // 容器 N
 *  ]
 */
function tmf_getContainerStack() {
    TMFJSBridge.invoke('getContainerStack', {
    }, function(res) {
//        tmf_logObject(res, 2);
        tmf_logObject({
            containerStack : res.containerStack,
        });
        tmf_closeContainers();
    });
}

function tmf_showContainerStack() {
    TMFJSBridge.invoke('getContainerStack', {
    }, function(res) {
        tmf_logObject(res, 2);
    });
}

// 关闭当前页面
function tmf_closeContainer() {
    TMFJSBridge.invoke('closeContainer', {
    }, function (res) {
        //tmf_logObject(res, 2);
        tmf_logObject();
    });
}

// 关闭多个页面
function tmf_closeContainers() {
    TMFJSBridge.invoke('closeContainers', {
        containerIds : [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20],    // array，必选，需要关闭的 H5 容器 ID 的集合
    }, function (res) {
        //tmf_logObject(res, 2);
        tmf_logObject();
    });
}

// 判断其他应用是否安装
function tmf_isAppInstalled() {
    TMFJSBridge.invoke('isAppInstalled', {
        url_ios             : 'mqqsecure://',           // string，必选（iOS），iOS app URL 地址
        packageName_android : 'com.tencent.qqpimsecure'   // string，必选（Android），Android app 包名
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject({
            url         : res.url,          // string，实际判断的 URL 地址
            installed   : res.installed,    // bool，是否安装了 app
        });
    });
}

// 启动其他应用
function tmf_openApp() {
    TMFJSBridge.invoke('openApp', {
        url_ios             : 'mqqsecure://',           // string，必选（iOS），iOS app URL 地址
        url_android         : 'weixin://dl/moments',           // string，必选（Android），Android app URL 地址
        packageName_android : 'com.tencent.qqpimsecure'   // string，必选（Android），Android app 包名
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject({
            url         : res.url,          // string，实际打开的 URL 地址
            packageName : res.packageName,  // string，实际打开的包名
            error       : res.error,        // integer，错误码
        });
    });
}