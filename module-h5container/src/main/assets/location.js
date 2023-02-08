function tmf_getLocation() {
    TMFJSBridge.invoke('getLocation', {
        type : 'wgs84', // string，必选，坐标类型，有效值：'wgs84' 表示真实坐标，'gcj02' 表示火星坐标
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject({
            latitude    : res.latitude,     // double，纬度，范围为 90 ~ -90
            longitude   : res.longitude,    // double，经度，范围为 180 ~ -180
            speed       : res.speed,        // double，速度，单位为 m/s
            accuracy    : res.accuracy,     // double，位置精度
        });
    });
}

// 使用内置地图查看位置
function tmf_openLocation() {
    TMFJSBridge.invoke('openLocation', {
        latitude    : 22.5403145,   // double，必选，纬度，范围为 90 ~ -90
        longitude   : 113.874348,   // double，必选，经度，范围为 180 ~ -180
        name        : '',           // string，可选，位置名
        address     : '',           // string，可选，地址详情说明
        scale       : 1.0,          // double，可选，地图缩放级别，默认为 1.0
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject();
    });
}