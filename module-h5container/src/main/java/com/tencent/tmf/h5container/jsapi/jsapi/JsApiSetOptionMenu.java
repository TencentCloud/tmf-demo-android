package com.tencent.tmf.h5container.jsapi.jsapi;

import com.google.gson.JsonObject;
import com.tencent.tmf.h5container.R;
import com.tencent.tmf.h5container.jsapi.JSapiActivity;
import com.tencent.tmf.webview.api.JsApi;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.callback.JsCallbackFunc;
import com.tencent.tmf.webview.api.param.JsCallParam;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsApiSetOptionMenu extends JsApi {

    JsParams mJsParams = null;
    JSapiActivity mJSapiActivity;
    BaseTMFWeb mBaseTMFWeb;

    public JsApiSetOptionMenu(JSapiActivity activity) {
        mJSapiActivity = activity;
    }

    @Override
    public String method() {
        return "setOptionMenu";
    }

    @Override
    public void handle(BaseTMFWeb webView, JsCallParam jsCallParam) {
        mBaseTMFWeb = webView;
        try {
            JSONObject jsonObject = new JSONObject(jsCallParam.paramStr);
            JSONArray jsonArray = jsonObject.optJSONArray("items");
            mJsParams = new JsParams();
            mJsParams.params = new ArrayList<>();
            if (jsonArray != null) {
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject object = (JSONObject) jsonArray.get(index);
                    if (object == null) {
                        continue;
                    }
                    ParamItem item = new ParamItem();
                    item.index = object.optInt("index");
                    item.title = object.optString("title");
                    item.titleColor = object.optString("titleColor");
                    item.action = new JsCallbackFunc();
                    JSONObject temp = new JSONObject(object.optString("action"));
                    item.action.sessionId = temp.optString("sessionId");
                    item.action.funcId = temp.optInt("funcId");
                    if (item != null) {
                        mJsParams.params.add(item);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (mJSapiActivity != null && mJsParams != null) {
            mJSapiActivity.updatePopupMenu(mJsParams.params);
        }
    }

    public class JsParams {

        public ArrayList<ParamItem> params;
    }

    public class ParamItem {

        public int index;
        public String title;
        public String titleColor;
        public JsCallbackFunc action;
    }

    public void onItemClick(int index) {
        if (mJsParams == null || mJsParams.params == null || mJsParams.params.size() == 0) {
            return;
        }
        for (ParamItem item : mJsParams.params) {
            if (item == null) {
                continue;
            }
            if (item.index == index) {
                JsonObject tempObject = new JsonObject();
                tempObject.addProperty("index", 0);
                String msg = mBaseTMFWeb.getContext().getResources().getString(R.string.js_api_has_clicked);

                tempObject.addProperty("tips", msg + item.title);
                item.action.callbackJs(mBaseTMFWeb, tempObject.toString());
            }
        }
    }
}
