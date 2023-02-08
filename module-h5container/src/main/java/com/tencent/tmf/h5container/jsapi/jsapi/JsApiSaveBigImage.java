package com.tencent.tmf.h5container.jsapi.jsapi;

import android.app.Activity;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.tencent.tmf.common.utils.FileUtil;
import com.tencent.tmf.h5container.R;
import com.tencent.tmf.webview.api.JsApi;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.callback.ErrorCode;
import com.tencent.tmf.webview.api.param.JsCallParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class JsApiSaveBigImage extends JsApi {
    private Activity activity;

    public JsApiSaveBigImage(Activity activity) {
        this.activity = activity;
    }

    @Override
    public String method() {
        return "saveBigImage";
    }

    @Override
    public void handle(BaseTMFWeb baseTMFWeb, JsCallParam jsCallParam) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = saveData(jsCallParam.paramStr);
                    JSONObject jsonObject = new JSONObject();
                   String txt= baseTMFWeb.getContext().getResources().getString(R.string.js_api_save_success);
                    jsonObject.put("ret", txt);
                    jsonObject.put("path", path);
                    jsCallParam.mCallback.callback(baseTMFWeb, jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    jsCallParam.mCallback.ret = ErrorCode.ERROR_FAIL;
                    jsCallParam.mCallback.errMsg = e.toString();
                    jsCallParam.mCallback.callback(baseTMFWeb, null);
                }
            }
        }).start();
    }

    /**
     * 无法传更大数据量，例如>10M
     *
     * @param paramStr
     */
    private String saveData(String paramStr) throws JSONException {
        String imageDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/testsave/";
        File file = new File(imageDirPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        String img1Path = imageDirPath + System.currentTimeMillis() + ".jpg";

        JSONObject jsonObject = new JSONObject(paramStr);
        String img1 = jsonObject.optString("img1");

        if (!TextUtils.isEmpty(img1)) {
            byte[] imgData = getImgData(img1);
            if (imgData != null) {
                FileUtil.write2File(new File(img1Path), imgData, false);
            }
        }

        return img1Path;
    }

//    /**
//     * 无法传更大数据量，例如>10M
//     * @param paramStr
//     */
//    private void saveData(String paramStr){
//        String imageDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/testsave/";
//        File file = new File(imageDirPath);
//        if(!file.exists()){
//            file.mkdirs();
//        }
//
//        String img1Path = imageDirPath + "1.jpg";
//        String img2Path = imageDirPath + "2.jpg";
//        String img3Path = imageDirPath + "3.jpg";
//        String img4Path = imageDirPath + "4.jpg";
//        String img5Path = imageDirPath + "5.jpg";
//
//        try {
//            JSONObject jsonObject = new JSONObject(paramStr);
//            String img1 = jsonObject.optString("img1");
//            String img2 = jsonObject.optString("img2");
//            String img3 = jsonObject.optString("img3");
//
//            if(!TextUtils.isEmpty(img1)){
//                byte[] imgData = getImgData(img1);
//                if(imgData != null){
//                    FileUtil.write2File(new File(img1Path), imgData, false);
//                }
//            }
//            if(!TextUtils.isEmpty(img2)){
//                byte[] imgData = getImgData(img2);
//                if(imgData != null){
//                    FileUtil.write2File(new File(img2Path), imgData, false);
//                }
//            }
//            if(!TextUtils.isEmpty(img3)){
//                byte[] imgData = getImgData(img3);
//                if(imgData != null){
//                    FileUtil.write2File(new File(img3Path), imgData, false);
//                }
//            }
//            if(activity != null){
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(activity, "保存图片完成", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private byte[] getImgData(String data) {
        String[] split = data.split(",");
        if (split.length == 2) {
            return Base64.decode(split[1], Base64.DEFAULT);
        }

        return null;
    }

}
