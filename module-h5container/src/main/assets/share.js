// 分享至微信朋友圈
function tmf_shareWxTimeLine() {
    TMFJSBridge.invoke('shareApp', {
            appKeys : 'wx_timeline',     // string，必选，分享的 app
            title   : '分享到微信朋友圈',               // string，可选，分享标题
            desc    : '分享内容',               // string，可选，分享描述
            link    : 'https://www.qq.com/?fromdefault',               // string，可选，分享链接
            imgUrl  : 'base64'               // string，可选，分享图标
        }, function (res) {
            tmf_logObject(res, 2);
            tmf_logObject({
                error : res.error,  // string，错误码
            });
        });
}

// 分享到微信好友
function tmf_shareWxFriend() {
    TMFJSBridge.invoke('shareApp', {
            appKeys : 'wx_message',     // string，必选，分享的 app
            title   : '分享到微信好友',               // string，可选，分享标题
            desc    : '分享内容',               // string，可选，分享描述
            link    : 'https://www.qq.com/?fromdefault',               // string，可选，分享链接
            imgUrl  : 'base64',               // string，可选，分享图标
        }, function (res) {
            tmf_logObject(res, 2);
            tmf_logObject({
                error : res.error,  // string，错误码
            });
        });
}

function tmf_shareCwxFriend(){
    TMFJSBridge.invoke('shareApp', {
            appKeys : 'cwx_message',     // string，必选，分享的 app
            title   : '分享到企业微信好友',               // string，可选，分享标题
            desc    : '分享内容',               // string，可选，分享描述
            link    : 'https://developer.work.weixin.qq.com/tutorial',               // string，可选，分享链接
            imgUrl  : 'base64'               // string，可选，分享图标 为图片的base64，以data:image/png;base64开头
        }, function (res) {
            tmf_logObject(res, 2);
            tmf_logObject({
                error : res.error,  // string，错误码
            });
        });
}

// 分享到QQ好友
function tmf_shareQQFriend() {
    TMFJSBridge.invoke('shareApp', {
            appKeys : 'qq_message',     // string，必选，分享的 app
            title   : '分享给QQ好友',               // string，可选，分享标题
            desc    : '分享内容',               // string，可选，分享描述
            link    : 'https://www.qq.com/?fromdefault',               // string，可选，分享链接
            imgUrl  : 'http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg',               // string，可选，分享图标
        }, function (res) {
            tmf_logObject(res, 2);
            tmf_logObject({
                error : res.error,  // string，错误码
            });
        });
}

// 分享到新浪微博
function tmf_shareSinaWeibo() {
    TMFJSBridge.invoke('shareApp', {
            appKeys : 'weibo_message',     // string，必选，分享的 app
            title   : '分享给新浪微博好友',               // string，可选，分享标题
            desc    : '我正在使用微博客户端发分享文字',               // string，可选，分享描述
            link    : 'https://www.qq.com/?fromdefault',               // string，可选，分享链接
            imgUrl  : 'base64',               // string，可选，分享图标
        }, function (res) {
            tmf_logObject(res, 2);
            tmf_logObject({
                error : res.error,  // string，错误码
            });
        });
}

// 分享到支付宝会话好友
function tmf_shareAlipayFriend() {
    TMFJSBridge.invoke('shareApp', {
            appKeys : 'ali_message',     // string，必选，分享的 app
            title   : '分享给支付宝会话好友',               // string，可选，分享标题
            desc    : '我正在使用支付宝客户端发分享给会话好友',               // string，可选，分享描述
            link    : 'https://www.qq.com/',               // string，可选，分享链接
            imgUrl  : 'http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg',               // string，可选，分享图标
        }, function (res) {
            tmf_logObject(res, 2);
            tmf_logObject({
                error : res.error,  // string，错误码
            });
        });
}

// 分享网页到支付宝生活圈
function tmf_shareAlipayTimeline() {
    TMFJSBridge.invoke('shareApp', {
            appKeys : 'ali_timeline',     // string，必选，分享的 app
            title   : '分享给支付宝生活圈',               // string，可选，分享标题
            desc    : '我正在使用支付宝客户端发分享到生活圈',               // string，可选，分享描述
            link    : 'https://www.qq.com/',               // string，可选，分享链接
            imgUrl  : 'http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg',               // string，可选，分享图标
        }, function (res) {
            tmf_logObject(res, 2);
            tmf_logObject({
                error : res.error,  // string，错误码
            });
        });
}

// 分享到钉钉会话好友
function tmf_shareDingFriend() {
    TMFJSBridge.invoke('shareApp', {
            appKeys : 'dding_message',     // string，必选，分享的 app
            title   : '分享给钉钉会话好友',               // string，可选，分享标题
            desc    : '我正在使用钉钉客户端发分享给会话好友',               // string，可选，分享描述
            link    : 'https://www.qq.com/',               // string，可选，分享链接
            imgUrl  : 'http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg',               // string，可选，分享图标
        }, function (res) {
            tmf_logObject(res, 2);
            tmf_logObject({
                error : res.error,  // string，错误码
            });
        });
}
