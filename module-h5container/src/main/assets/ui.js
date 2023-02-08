
/** Native 事件通知 H5 **/
function tmf_sample_onNavigationItemClick() {
    document.addEventListener('onNavigationItemClick', function (e) {
        alert(JSON.stringify(e.tmf));
    }, false);
}

// 显示左上角关闭
function tmf_showFinish() {
    TMFJSBridge.invoke('showFinish', {
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject();
    });
}

// 隐藏左上角关闭
function tmf_hideFinish() {
    TMFJSBridge.invoke('hideFinish', {
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject();
    });
}

// 设置标题及相关属性
function tmf_setTitle(titleStr) {
    TMFJSBridge.invoke('setTitle', {
        title       : titleStr,   // string，可选，标题
        titleColor  : '#ff000000',   // string，可选，标题颜色
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject();
    });
}

// 选择列表
function tmf_actionSheet() {
    TMFJSBridge.invoke('actionSheet', {
        title           : '请选择',   // string，可选，标题
        buttons         : ["A","B","C", "D"],   // array，必选，按钮列表
        cancelButton    : '取消'    // string，必选，取消按钮
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject({
            clickedButtonIndex  : res.clickedButtonIndex,   // integer，点击按钮的索引
        });
    });
}

// 警告框
function tmf_alert() {
    TMFJSBridge.invoke('alert', {
        title       : '警告',   // string，可选，标题
        message     : '警告警告再次警告',   // string，可选，信息
        okButton    : '我知道了',   // string，必选，确定按钮
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject({
            clickedButtonIndex  : res.clickedButtonIndex,   // integer，点击按钮的索引
        });
    });
}

// 确认框
function tmf_confirm() {
    TMFJSBridge.invoke('confirm', {
        title           : '嗯哼？',   // string，可选，标题
        message         : '选择取消还是确定你决定',   // string，可选，信息
        okButton        : '好的',   // string，必选，确定按钮
        cancelButton    : '不了',   // string，必选，取消按钮
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject({
            clickedButtonIndex  : res.clickedButtonIndex,   // integer，点击按钮的索引
        });
    });
}

// 设置右上角按钮
function tmf_customOptionMenu(color) {
    TMFJSBridge.invoke('setOptionMenu', {
        items:[
            {
               index       : 0 ,
               title       : '关闭网页（set）',   // string，可选，标题
               titleColor  : color,   // string，可选，标题颜色
               action      : function(res){
                    alert(JSON.stringify(res));
                    tmf_closeContainer();
               }
            },
            {
              index       : 1 ,
              title       : '复制链接（set）',   // string，可选，标题
              titleColor  : color,   // string，可选，标题颜色
              action      : function(res){
                  alert(JSON.stringify(res));
              }
            },
            {
              index       : 2 ,
              title       : '在浏览器打开（set）',
              titleColor  : color,
              action      : function(res){
                  alert(JSON.stringify(res));
              }
            }
    ]}, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject();
    });
}

// 显示右上角按钮
function tmf_showOptionMenu() {
    TMFJSBridge.invoke('showOptionMenu', {
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject();
    });
}

// 隐藏右上角按钮
function tmf_hideOptionMenu() {
    TMFJSBridge.invoke('hideOptionMenu', {
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject();
    });
}

// 显示加载中
function tmf_showLoading() {
    TMFJSBridge.invoke('showLoading', {
        title       : '',   // string，可选，标题
        detail      : '',   // string，可选，内容
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject();
    });
}

// 隐藏加载中
function tmf_hideLoading() {
    TMFJSBridge.invoke('hideLoading', {
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject();
    });
}

// 弱提示
function tmf_showToast() {
    TMFJSBridge.invoke('showToast', {
        title       : '',   // string，可选，标题
        detail      : '这是一条Toast提示！',   // string，可选，内容
        duration    : 2.0,  // double，可选，toast 出现的时间，单位 s，默认为 2.0s
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject();
    });
}