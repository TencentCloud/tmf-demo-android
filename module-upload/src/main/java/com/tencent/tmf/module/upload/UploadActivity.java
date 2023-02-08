package com.tencent.tmf.module.upload;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.Nullable;
import com.qmuiteam.qmui.widget.QMUIFontFitTextView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.android.upload.api.TMFUploader;
import com.tencent.tmf.android.upload.api.UploadCallback;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.shark.SharkService;
import com.tencent.tmf.shark.api.IShark;
import java.io.File;
import java.io.UnsupportedEncodingException;

@Destination(
        url = "portal://com.tencent.tmf.module.upload/upload-activity",
        launcher = Launcher.ACTIVITY,
        description = "分片上传"
)
public class UploadActivity extends TopBarActivity implements View.OnClickListener {

    private static final int ALBUM_REQUEST_CODE = 10;
    QMUIRoundButton mUploadPicBtn = null;
    QMUIFontFitTextView mResult = null;
    private StringBuilder sb = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResult = findViewById(R.id.stepResult);
        mUploadPicBtn = findViewById(R.id.upload_pic);
        mTopBar.setTitle(getStringById(R.string.module_upload_0));

        mUploadPicBtn.setOnClickListener(this);
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }
    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_upload, null);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.upload_pic) {
            sb = new StringBuilder();
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, ALBUM_REQUEST_CODE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode != ALBUM_REQUEST_CODE
                || intent == null || intent.getData() == null || resultCode != RESULT_OK) {
            println(getStringById(R.string.module_upload_1));
            return;
        }

        Uri uri = intent.getData();

        String imgPath = null;
        if ("file".equalsIgnoreCase(uri.getScheme())) { //使用第三方应用打开
            imgPath = uri.getPath();
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) { //4.4以后
            imgPath = getPath(this, uri);
        } else { //4.4以下下系统调用方法
            imgPath = getRealPathFromURI(uri);
        }
        println(getStringById(R.string.module_upload_2) + imgPath);
        try {
            File inFile = null;
            if (!TextUtils.isEmpty(imgPath)) {
                inFile = new File(imgPath);
            }
            if (inFile == null || !inFile.exists()) {
                println(getStringById(R.string.module_upload_3));
                return;
            }

            IShark shark = SharkService.getSharkWithInit();
            if (shark == null) {
                return;
            }

            println(getStringById(R.string.module_upload_4) + shark.getGuid());
            TMFUploader.with(getApplicationContext()).builder()
                    .setBusinessId("busId1")//设置图片上传的BusinessId
                    .setSharkNet(shark)//设置鲨鱼网络
                    .targetFile(inFile)//设置要上传的图片
                    .addParam("file", inFile.getName())//添加参数
                    .upload(new UploadCallback() { //上传回调
                        @Override
                        public void onStart() {
                            println(getStringById(R.string.module_upload_5) + shark.getGuid());
                        }

                        @Override
                        public void onSuccess(byte[] bytes) {
                            println(getStringById(R.string.module_upload_6));
                            try {
                                println(bytes != null ? new String(bytes, "utf-8") : "null");
                            } catch (UnsupportedEncodingException e) {
                                println(getStringById(R.string.module_upload_7) + e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(int errorCode) {
                            println(getStringById(R.string.module_upload_8) + errorCode);
                        }

                        @Override
                        public void onProgress(float progress) {
                            println(getStringById(R.string.module_upload_9) + progress);
                        }
                    });
        } catch (Throwable e) {
            println(getStringById(R.string.module_upload_10) + e.getMessage());
        }
    }

    private void println(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sb.append("\n").append(msg);
                mResult.setText(sb.toString());
            }
        });
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        Cursor cursor = null;
        try {
            String scheme = contentUri.getScheme();
            if (scheme != null && scheme.equals("content")) {
                ContentResolver contentResolver = getApplicationContext().getContentResolver();
                cursor = contentResolver.query(contentUri, null, null, null, null);
                cursor.moveToFirst();
                String uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                cursor.close();
                return uri;
            }
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            if (null != cursor && cursor.moveToFirst()) {
                ;
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(columnIndex);
                cursor.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return res;
    }

    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris
                        .withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public String getDataColumn(Context context, Uri uri, String selection,
            String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        try {
            String scheme = uri.getScheme();
            if (scheme != null && scheme.equals("content")) {
                ContentResolver contentResolver = getApplicationContext().getContentResolver();
                cursor = contentResolver.query(uri, null, null, null, null);
                cursor.moveToFirst();
                return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
