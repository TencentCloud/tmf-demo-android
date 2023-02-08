package com.tencent.tmf.common.config;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.tencent.tmf.common.R;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.storage.sp.ConfigSp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

//@Destination(
//        url = "portal://com.tencent.tmf.shark/TMFConfigListActivity",
//        launcher = Launcher.ACTIVITY,
//        description = "加载TMF本地配置文件"
//)
public class TMFConfigActivity extends TopBarActivity {

    private static final String TAG = "TMFConfigActivity";
    private Context mContext;
    private String mConfigFilePath;
    private String mConfigFileName;
    private TextView mFilePathText;
    private TextView mFileContentText;

    public static void startActivity(Context context, String configFilePath, String fileName) {
        Intent intent = new Intent(context, TMFConfigActivity.class);
        intent.putExtra("filePath", configFilePath);
        intent.putExtra("fileName", fileName);
        context.startActivity(intent);
    }


    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_tmf_config, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();//action
        String type = intent.getType();//类型
        if (Intent.ACTION_VIEW.equals(action) && type != null) { //微信打开文本文件
            Uri uri = intent.getData();
            mConfigFilePath = Uri.decode(uri.getEncodedPath());
            if (mConfigFilePath.contains("/external")) {
                mConfigFilePath = new File(Environment.getExternalStorageDirectory(),
                        mConfigFilePath.substring("/external".length())).getAbsolutePath();
            }
            mConfigFileName = new File(mConfigFilePath).getName();
        } else if (Intent.ACTION_SEND.equals(action) && type != null) { //企业微信打开txt文件
            Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
            mConfigFilePath = Uri.decode(uri.getEncodedPath());
            mConfigFileName = new File(mConfigFilePath).getName();
        } else {
            mConfigFilePath = getIntent().getStringExtra("filePath");
            mConfigFileName = getIntent().getStringExtra("fileName");
        }

        mContext = this;
        mFilePathText = findViewById(R.id.text_config_path);
        mFileContentText = findViewById(R.id.file_text);

        mTopBar.setTitle(mConfigFileName);
        mFilePathText.setText(mConfigFilePath);
        mFileContentText.setText(readFile(mConfigFilePath));
        String text = getResources().getString(R.string.tmf_config_activity_load_config);
        mTopBar.addRightTextButton(text, 12479).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadConifg();
            }
        });
    }

    private void loadConifg() {

        String configStr = TMFConfigHelper.readConfigJsonFromFile(mConfigFilePath);
        JSONObject configObj = null;
        try {
            configObj = new JSONObject(configStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (configObj == null || !TMFConfigHelper.isValidConfig(configObj)) {
            String text = getResources().getString(R.string.tmf_config_activity_load_config_failed);
            String text2 = getResources().getString(R.string.tmf_config_activity_load_config_get_it);

            new QMUIDialog.MessageDialogBuilder(mContext)
                    .setTitle(text)
                    .setMessage(configStr)
                    .addAction(text2, (dialog, index) -> dialog.dismiss()).create().show();
        } else {
            String text = getResources().getString(R.string.tmf_config_activity_load_config_success);
            String text2 = getResources().getString(R.string.tmf_config_activity_load_config_restart_app);

            new QMUIDialog.MessageDialogBuilder(mContext)
                    .setTitle(text)
                    .setMessage(configStr)
                    .setCancelable(false)
                    .addAction(text2, (dialog, index) -> {
                        dialog.dismiss();
                        AppDataUtil.clearData(mContext);
                        ConfigSp.getInstance().putTMFConfigPATH(mConfigFilePath);
                        AppDataUtil.killMyself();
                    }).create().show();
        }
    }

    private String readFile(String filePath) {
        String text = getResources().getString(R.string.tmf_config_activity_load_config_file_not_exists);

        File file = new File(filePath);
        if (!file.exists()) {
            return text;
        }

        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //一次读一行，读入null时文件结束
            while ((tempString = reader.readLine()) != null) {
                builder.append(tempString);
                builder.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return builder.toString();
    }

    /**
     * 微信intent
     * Intent { act=android.intent.action.VIEW dat=content://com.tencent.mm.external
     * .fileprovider/external/tencent/MicroMsg/Download/测试.doc
     * typ=application/msword flg=0x10400001 pkg=com.example.lwyread cmp=com.example.lwyread/.activity.ShareActivity }
     * QQintent
     * Intent { act=android.intent.action.VIEW cat=[android.intent.category.DEFAULT]
     * dat=content://com.tencent.mobileqq.fileprovider/external_files/storage/emulated/0/Android/data/com.tencent
     * .mobileqq/Tencent/QQfile_recv/测试.doc
     * typ=application/msword flg=0x1 pkg=com.example.lwyread cmp=com.example.lwyread/.activity.ShareActivity (has
     * extras) }
     * 文件管理intent
     * Intent { act=android.intent.action.VIEW dat=content://media/external/file/887 typ=application/msword flg=0x1
     * cmp=com.example.lwyread/.activity.ShareActivity }
     */
    //这种//media/external/file/887
    public static String getFilePathFromUri(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns
                    .DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    //QQ微信这种//com.tencent.mm.external.fileprovider/external/tencent/MicroMsg/Download/测试.doc
    public static String getFilePathFromUri2(Context context, Uri uri) {
        try {
            List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
            if (packs != null) {
                String fileProviderClassName = FileProvider.class.getName();
                for (PackageInfo pack : packs) {
                    ProviderInfo[] providers = pack.providers;
                    if (providers != null) {
                        for (ProviderInfo provider : providers) {
                            if (uri.getAuthority().equals(provider.authority)) {
                                if (provider.name.equalsIgnoreCase(fileProviderClassName)) {
                                    Class<FileProvider> fileProviderClass = FileProvider.class;
                                    try {
                                        Method getPathStrategy = fileProviderClass
                                                .getDeclaredMethod("getPathStrategy", Context.class, String.class);
                                        getPathStrategy.setAccessible(true);
                                        Object invoke = getPathStrategy.invoke(null, context, uri.getAuthority());
                                        if (invoke != null) {
                                            String pathStrategyStringClass =
                                                    FileProvider.class.getName() + "$PathStrategy";
                                            Class<?> pathStrategy = Class.forName(pathStrategyStringClass);
                                            Method getFileForUri = pathStrategy
                                                    .getDeclaredMethod("getFileForUri", Uri.class);
                                            getFileForUri.setAccessible(true);
                                            Object invoke1 = getFileForUri.invoke(invoke, uri);
                                            if (invoke1 instanceof File) {
                                                String filePath = ((File) invoke1).getAbsolutePath();
                                                return filePath;
                                            }
                                        }
                                    } catch (NoSuchMethodException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过Uri获取文件在本地存储的真实路径
     */
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        }
        cursor.close();
        return null;
    }
}
