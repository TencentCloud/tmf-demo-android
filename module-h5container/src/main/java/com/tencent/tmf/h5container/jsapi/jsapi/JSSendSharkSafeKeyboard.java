package com.tencent.tmf.h5container.jsapi.jsapi;

import Protocol.Keyboard.ReqRecoverInputText;
import Protocol.Keyboard.RespRecoverInputText;
import android.util.Log;
import com.qq.taf.jce.JceStruct;
import com.tencent.tmf.base.api.utils.Base64Util;
import com.tencent.tmf.common.keyboard.Consts;
import com.tencent.tmf.common.shark.SharkService;
import com.tencent.tmf.shark.api.ESharkCode;
import com.tencent.tmf.shark.api.ISendJceCallback;
import com.tencent.tmf.shark.api.IShark;
import com.tencent.tmf.shark.api.ISharkCallBack;
import com.tencent.tmf.shark.api.SharkExtra;
import com.tencent.tmf.shark.api.SharkRetCode;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.callback.ErrorCode;
import com.tencent.tmf.webview.api.param.JsCallParam;
import com.tencent.tmf.webview.impl.jsapi.sendShark;
import org.json.JSONObject;

/**
 * sendShark jsapi的实现
 * 用于安全键盘演示
 */
public class JSSendSharkSafeKeyboard extends sendShark {

    @Override
    public String method() {
        return JSSendSharkSafeKeyboard.class.getSimpleName();
    }

    @Override
    public void handle(final BaseTMFWeb webView, final JsCallParam jsCallParam) {
        println("js call JSSendSharkSafeKeyboard");
        IShark shark = SharkService.getSharkWithInit();
        ReqRecoverInputText req = new ReqRecoverInputText();
        try {
            JSONObject jo = new JSONObject(jsCallParam.paramStr);
            req.inputText = jo.optString("inputText");
            req.context = Base64Util.decode(jo.optString("context"), Base64Util.DEFAULT);
        } catch (Exception e) {
            println("jsapi param parse failed: " + e.getMessage());
            jsCallParam.mCallback.ret = ErrorCode.ERROR_BUSINESS;
            jsCallParam.mCallback.errMsg = e.getMessage();
            jsCallParam.mCallback.callback(webView, null);
            return;
        }

        ISendJceCallback callback = new ISendJceCallback() {
            @Override
            public void onFinish(int seqNo, int cmdId, SharkRetCode retCode, JceStruct resp, SharkExtra extra) {
                JSONObject result = new JSONObject();
                try {
                    if (retCode.isAccessLayerOk() && retCode.errorCode == ESharkCode.ERR_NONE && resp != null) { // 成功
                        println("restore values success， primary value ：" + ((RespRecoverInputText) resp).recoveredText);
                        result.put("decryption", ((RespRecoverInputText) resp).recoveredText + "");
                    } else { // 失败
                        println("failed, seqNo: " + seqNo + " cmdId: " + cmdId
                                + " retCode: " + retCode.errorCode + " errorDomain: "
                                + retCode.errorDomain + " resp: " + resp);
                        result.put("error", retCode.errorCode);
                        result.put("errorDescription", retCode.errorDomain);
                    }
                } catch (Exception e) {
                    jsCallParam.mCallback.ret = ErrorCode.ERROR_BUSINESS;
                    jsCallParam.mCallback.errMsg = e.getMessage();
                    println("response data parse exception： " + e.getMessage());
                }

                String callbackStr = result.toString();
                jsCallParam.mCallback.callback(webView, callbackStr);
                println("callback js: " + callbackStr);
            }
        };
        // 调Shark发起网络通信
        shark.sendJceStruct(Consts.ECID_REQ_RECOVERED_INPUT_TEXT, req, new RespRecoverInputText(), 0, callback, 60 * 1000);
    }

    private static void println(String msg) {
        Log.i("JSSendSharkSafeKeyboard", "" + msg);
    }
}
