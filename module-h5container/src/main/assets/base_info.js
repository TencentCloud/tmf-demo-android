// 页面出现
function tmf_onContainerAppear() {
    document.addEventListener('containerAppear', function (e) {
        tmf_logObject("打开页面: " + JSON.stringify(e), 2);
    }, false);
}

// 页面消失
function tmf_onContainerDisappear() {
    document.addEventListener('containerDisappear', function (e) {
        tmf_logObject("关闭页面: " + JSON.stringify(e), 2);
    }, false);
}

/******************** 基础信息 ********************/
// 获取版本号
function tmf_getVersion() {
    TMFJSBridge.invoke('getVersion', {
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject({
            version : res.version,  // string，当前 app 的版本号
        });
    });
}

// 获取 Build 号
function tmf_getBuild() {
    TMFJSBridge.invoke('getBuild', {
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject({
            build : res.build,  // string，当前 app 的 build 号
        });
    });
}

// 获取 GUID
function tmf_getGUID() {
    TMFJSBridge.invoke('getGUID', {
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject({
            guid : res.guid,    // string，当前用户的 GUID
        });
    });
}

// 获取网络状态
function tmf_getNetworkType() {
    TMFJSBridge.invoke('getNetworkType', {
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject({
            networkType : res.networkType,    // string，网络类型，有效值：'none'，'cellular'，'wifi'
        });
    });
}

// 获取共享的 KV 数据
// iOS 为 NSUserDefaults
function tmf_getSharedStorage() {
    TMFJSBridge.invoke('getSharedStorage', {
        //key : 'key1',   // string，必选，KV 的 key
        keys: ['key_01','key_02','key_03'],
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject({
            //value : res.value,  // any，KV 的 value
            value_01:res.values.key_01,
            value_02:res.values.key_02,
            value_03:res.values.key_03,
        });
    });
}

// 设置共享的 KV 数据
// iOS 为 NSUserDefaults
function tmf_setSharedStorage(avalue) {
    TMFJSBridge.invoke('setSharedStorage', {
        values  :{
            akey   : avalue,
            key_02 : 'key_02',
            key_03 : 'key_03',
            key_04 : 'key_04',
        }
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject();
    });
}

// 移除共享的 KV 数据
// iOS 为 NSUserDefaults
function tmf_removeSharedStorage() {
    TMFJSBridge.invoke('removeSharedStorage', {
        keys : ['key_02'],   // string，必选，KV 的 key
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject();
    });
}