package com.tencent.tmf.h5container.jsapi.jsapi;

import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import com.tencent.tmf.common.utils.FileUtil;
import com.tencent.tmf.webview.api.JsApi;
import com.tencent.tmf.webview.api.TMFWebConfig;
import com.tencent.tmf.webview.api.base.BaseTMFWeb;
import com.tencent.tmf.webview.api.callback.CallbackH5;
import com.tencent.tmf.webview.api.callback.ErrorCode;
import com.tencent.tmf.webview.api.param.JsCallParam;
import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;

public class JsApiLoadBigImage extends JsApi {

    public static final int REQUEST_CODE_BASE64 = 100;
    public static final int REQUEST_CODE_URL = 101;
    private BaseTMFWeb mBaseTMFWeb;
    private CallbackH5 mCallback;

    @Override
    public String method() {
        return "loadBigImage";
    }

    @Override
    public void handle(BaseTMFWeb baseTMFWeb, JsCallParam jsCallParam) {
        mBaseTMFWeb = baseTMFWeb;
        mCallback = jsCallParam.mCallback;
        int type = REQUEST_CODE_BASE64;
        try {
            JSONObject jsonObject = new JSONObject(jsCallParam.paramStr);
            type = jsonObject.optInt("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        baseTMFWeb.mActivity.startActivityForResult(intent, type);
    }

    public void callbackData(String filePath, int requestCode) {
        if (TextUtils.isEmpty(filePath)) {
            mCallback.ret = ErrorCode.ERROR_FAIL;
            mCallback.callback(mBaseTMFWeb, null);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            if (requestCode == REQUEST_CODE_BASE64) {
                String img1Base64Data = "data:image/jpg;base64," + Base64
                        .encodeToString(FileUtil.readFile(new File(filePath)), Base64.DEFAULT);
                jsonObject.put("img1", img1Base64Data);
            } else {
                jsonObject.put("img1", TMFWebConfig.getLocalDomian() + filePath);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mCallback.ret = ErrorCode.ERROR_NONE;
        mCallback.callback(mBaseTMFWeb, jsonObject.toString());
    }

    private String getData() {
        JSONObject jsonObject = new JSONObject();

//        String imageDirPath = "file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
//        String img1Path = imageDirPath + "1.jpg";
//        String img2Path = imageDirPath + "2.jpg";
//        String img3Path = imageDirPath + "3.jpg";
//        String img4Path = imageDirPath + "4.jpg";
//        String img5Path = imageDirPath + "5.jpg";
//
//        try {
//            jsonObject.put("img1", img1Path);
//            jsonObject.put("img2", img2Path);
//            jsonObject.put("img3", img3Path);
//            jsonObject.put("img4", img4Path);
//            jsonObject.put("img5", img5Path);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        String imageDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
        String img1Path = imageDirPath + "1.jpg";
        String img2Path = imageDirPath + "2.jpg";
        String img3Path = imageDirPath + "3.jpg";
        String img4Path = imageDirPath + "4.jpg";
        String img5Path = imageDirPath + "5.jpg";

//        byte[] bytes = FileUtil.readFile(new File(img1Path));
//        String s = Base64.encodeToString(bytes, Base64.DEFAULT);
//        byte[] decode = Base64.decode(s, Base64.DEFAULT);

        String img1Base64Data =
                "data:image/jpg;base64," + Base64.encodeToString(FileUtil.readFile(new File(img1Path)), Base64.DEFAULT);
        String img2Base64Data =
                "data:image/jpg;base64," + Base64.encodeToString(FileUtil.readFile(new File(img2Path)), Base64.DEFAULT);
        String img3Base64Data =
                "data:image/jpg;base64," + Base64.encodeToString(FileUtil.readFile(new File(img3Path)), Base64.DEFAULT);
//        String img4Base64Data = "data:image/jpg;base64," + Base64.encodeToString(FileUtil.readFile(new File
//        (img4Path)), Base64.DEFAULT);
//        String img5Base64Data = "data:image/jpg;base64," + Base64.encodeToString(FileUtil.readFile(new File
//        (img5Path)), Base64.DEFAULT);

        try {
            jsonObject.put("img1", img1Base64Data);
            jsonObject.put("img2", img2Base64Data);
            jsonObject.put("img3", img3Base64Data);
//            jsonObject.put("img4", img4Base64Data);
//            jsonObject.put("img5", img5Base64Data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String str = jsonObject.toString();

        return str;
    }
}
