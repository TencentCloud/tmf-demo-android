package com.tencent.tmf.applet.demo.jsplugin;

import com.tencent.tmfmini.sdk.annotation.JsEvent;
import com.tencent.tmfmini.sdk.annotation.JsPlugin;
import com.tencent.tmfmini.sdk.launcher.core.model.RequestEvent;
import com.tencent.tmfmini.sdk.launcher.core.plugins.BaseJsPlugin;

import org.json.JSONException;
import org.json.JSONObject;

@JsPlugin(secondary = true)
public class WxApiPlugin extends BaseJsPlugin {
    /**
     * 对应小程序wx.login调用
     * @param req
     */
    @JsEvent("wx.login")
    public void login(final RequestEvent req) {
        //获取参数
        //req.jsonParams
        //异步返回数据
        //req.fail();
        //req.ok();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", "wx.login");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        req.ok(jsonObject);
    }

    /**
     * 对应小程序wx.getUserInfo调用
     * @param req
     */
    @JsEvent("wx.getUserInfo")
    public void getUserInfo(final RequestEvent req) {
        //获取参数
        //req.jsonParams
        //异步返回数据
        //req.fail();
        //req.ok();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", "wx.getUserInfo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        req.ok(jsonObject);
    }

    /**
     * 对应小程序wx.getUserProfile调用
     * @param req
     */
    @JsEvent("wx.getUserProfile")
    public void getUserProfile(final RequestEvent req) {
        //获取参数
        //req.jsonParams
        //异步返回数据
        //req.fail();
        //req.ok();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", "wx.getUserProfile");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        req.ok(jsonObject);
    }
}
