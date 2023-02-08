// 网关接口调用（RPC）
function tmf_sendShark() {
    TMFJSBridge.invoke('sendShark', {
        apiName : "TMFEcho",                                // string，必选，Shark 命令字（API name）
        //cmdId   : 998,                                    // integer，可选，Shark 命令字（Cmd ID）
        headers : { 'headers_key1' : 'headers_value1' },    // dictionary，可选，业务自定义的请求头部
        cookies : { 'cookies_key1' : 'cookies_value1' },    // dictionary，可选，业务自定义的请求 Cookies
        queries : { 'queries_key1' : 'queries_value1' },    // dictionary，可选，业务自定义的请求 Queries
        data    : 'test',                                   // string，可选，业务自定义的请求数据
        timeout : 15000,                                    // integer，可选，超时时间，单位 ms
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject({
            error               : res.error,                // integer，业务的返回错误码，没有错误时为 undefined
            errorDescription    : res.errorDescription,     // string，业务的返回错误描述，没有错误时为 undefined
            httpResponseCode    : res.httpResponseCode,     // integer，业务返回的 HTTP 状态码
            headers             : res.headers,              // dictionary，业务的返回头部
            data                : res.data,                 // string，业务的返回数据
        });
    });
}

// 网关接口调用（RPC）
function tmf_sendSharkJson() {
    TMFJSBridge.invoke('sendSharkJson', {
        apiName : "TMFEcho",                           // string，必选，Shark 命令字（API name）
        //cmdId   : 998,                                    // integer，可选，Shark 命令字（Cmd ID）
        headers : { 'headers_key1' : 'headers_value1' },    // dictionary，可选，业务自定义的请求头部
        cookies : { 'cookies_key1' : 'cookies_value1' },    // dictionary，可选，业务自定义的请求 Cookies
        queries : { 'queries_key1' : 'queries_value1' },    // dictionary，可选，业务自定义的请求 Queries
        data    : '{"key1":"value1","key2":"value2"}',      // string，可选，业务自定义的请求数据
        timeout : 15000,                                    // integer，可选，超时时间，单位 ms
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject({
            error               : res.error,                // integer，业务的返回错误码，没有错误时为 undefined
            errorDescription    : res.errorDescription,     // string，业务的返回错误描述，没有错误时为 undefined
            httpResponseCode    : res.httpResponseCode,     // integer，业务返回的 HTTP 状态码
            headers             : res.headers,              // dictionary，业务的返回头部
            data                : res.data,                 // string，业务的返回数据
        });
    });
}

// 网关接口调用（RPC）
function tmf_sendSharkForm() {
    TMFJSBridge.invoke('sendSharkForm', {
        apiName : "TMFEcho",                           // string，必选，Shark 命令字（API name）
        //cmdId   : 998,                                    // integer，可选，Shark 命令字（Cmd ID）
        headers : { 'headers_key1' : 'headers_value1' },    // dictionary，可选，业务自定义的请求头部
        cookies : { 'cookies_key1' : 'cookies_value1' },    // dictionary，可选，业务自定义的请求 Cookies
        queries : { 'queries_key1' : 'queries_value1' },    // dictionary，可选，业务自定义的请求 Queries
        data    : 'key1=value1&key2=value2',                // string，可选，业务自定义的请求数据
        timeout : 15000,                                    // integer，可选，超时时间，单位 ms
    }, function (res) {
        tmf_logObject(res, 2);
        tmf_logObject({
            error               : res.error,                // integer，业务的返回错误码，没有错误时为 undefined
            errorDescription    : res.errorDescription,     // string，业务的返回错误描述，没有错误时为 undefined
            httpResponseCode    : res.httpResponseCode,     // integer，业务返回的 HTTP 状态码
            headers             : res.headers,              // dictionary，业务的返回头部
            data                : res.data,                 // string，业务的返回数据
        });
    });
}