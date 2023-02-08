package com.tencent.tmf.common.config;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.github.barteksc.pdfviewer.util.FileUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIAnimationListView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.tencent.tmf.common.R;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.storage.sp.ConfigSp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 加载TMF本地配置文件
 */
//@Destination(
//        url = "portal://com.tencent.tmf.shark/TMFConfigListActivity",
//        launcher = Launcher.ACTIVITY,
//        description = "加载TMF本地配置文件"
//)
public class TMFConfigListActivity extends TopBarActivity {

    public static final int OPEN_FILE_REQUEST_CODE = 1000;
    private Context mContext;
    private SampleAdapter mAdapter;
    private QMUIAnimationListView mListView;
    private TextView mConfigDirText;
    private TextView mHintText;
    private QMUIListPopup mListPopup;

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_tmf_config_list, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        String title = getResources().getString(R.string.tmf_config_list_activity_title);
        mTopBar.setTitle(title);

        mListView = mContentView.findViewById(R.id.listview);
        mConfigDirText = mContentView.findViewById(R.id.text_config_dir);
        mHintText = mContentView.findViewById(R.id.text_hint);

        mConfigDirText.setText(getConfigDirPath());

        copyFilesFromAssets(mContext, "tmfconfig", getConfigDirPath());

        mAdapter = new SampleAdapter();
        String[] data = getConfigList();
        if (data != null && data.length > 0) {
            mAdapter.setData(data);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String fileName = (String) mAdapter.getItem(position);
                    TMFConfigActivity
                            .startActivity(TMFConfigListActivity.this,
                                    new File(getConfigDirPath(), fileName).getAbsolutePath(), fileName);
                }
            });
        } else {
            mListView.setVisibility(View.GONE);
            mHintText.setVisibility(View.VISIBLE);
        }
        String text = getResources().getString(R.string.tmf_config_list_activity_menu);

        final Button menuButton = mTopBar.addRightTextButton(text, 13479);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initListPopupIfNeed();

                mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
                mListPopup.setPreferredDirection(QMUIPopup.DIRECTION_BOTTOM);
                mListPopup.show(menuButton);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, new String[]{"_display_name"}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    String name = cursor.getString(0);
                    try {
                        InputStream is = getContentResolver().openInputStream(uri);
                        FileUtils.copy(is, new File(getConfigDirPath(), name));
                        TMFConfigActivity
                                .startActivity(TMFConfigListActivity.this,
                                        new File(getConfigDirPath(), name).getAbsolutePath(), name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    private void refreshList() {
        mAdapter = new SampleAdapter();
        String[] data = getConfigList();
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
        mListView.setAdapter(mAdapter);
    }

    public String getConfigDirPath() {
        return getExternalFilesDir("TMFConfig").getAbsolutePath();
    }

    /**
     * 配置文件列表
     *
     * @return
     */
    private String[] getConfigList() {
        String configDirPath = getConfigDirPath();
        File configDir = new File(configDirPath);
        if (configDir.exists()) {
            return configDir.list();
        }

        return null;
    }

    private void showCurrentTmfConfigInfo() {
        String titile = mContext.getString(R.string.tmf_config_list_activity_current_config);
        String confirm = mContext.getString(R.string.tmf_config_list_activity_confirm);
        new QMUIDialog.MessageDialogBuilder(mContext)
                .setTitle(titile)
                .setMessage(TMFConfigHelper.getCurrentConfigJson())
                .addAction(confirm, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void initListPopupIfNeed() {
        if (mListPopup == null) {
            String text1 = mContext.getResources().getString(R.string.tmf_config_list_activity_text_1);
            String text2 = mContext.getResources().getString(R.string.tmf_config_list_activity_text_2);
            String text3 = mContext.getResources().getString(R.string.tmf_config_list_activity_text_3);
            String text4 = mContext.getResources().getString(R.string.tmf_config_list_activity_text_4);
            String[] listItems = new String[]{
//                    "拷贝测试配置",
                    text1,
                    text2,
                    text3,
                    text4
            };
            List<String> data = new ArrayList<>();

            Collections.addAll(data, listItems);
            ArrayAdapter adapter = new ArrayAdapter<>(mContext, R.layout.simple_list_item, data);

            mListPopup = new QMUIListPopup(mContext, QMUIPopup.DIRECTION_NONE, adapter);
            mListPopup.create(QMUIDisplayHelper.dp2px(mContext, 160), QMUIDisplayHelper.dp2px(mContext, 280),
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            switch (i) {
//                                        case 0:
//                                            copyFilesFromAssets(mContext, "tmfconfig", getConfigDirPath());
//                                            String[] configList = getConfigList();
//                                            if (configList != null && configList.length > 0) {
//                                                mAdapter.setData(configList);
//                                                mListView.setAdapter(mAdapter);
//                                                mListView.setVisibility(View.VISIBLE);
//                                                mHintText.setVisibility(View.GONE);
//                                            }
//                                            L.d("configList=" + Arrays.toString(configList));
//                                            break;
                                case 0:
                                    showCurrentTmfConfigInfo();
                                    break;
                                case 1:
                                    String text = mContext.getResources().getString(R.string.tmf_config_list_activity_clear_cache_succes);
                                    Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
                                    ConfigSp.getInstance().clearAll();
                                    AppDataUtil.clearData(mContext);
                                    AppDataUtil.killMyself();
                                    break;
                                case 2:
                                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent.setType("application/json");
                                    startActivityForResult(intent, 1000);
                                    break;
                                case 3:
                                    loadConfigFromClipboard();
                                    break;
                                default:
                                    break;
                            }
                            mListPopup.dismiss();
                        }
                    });
            mListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                }
            });
        }
    }

    private void loadConfigFromClipboard() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                String content = clipData.getItemAt(0).coerceToText(this).toString();
                com.tencent.tmf.common.utils.FileUtil.write2File(new File(getConfigDirPath(),
                        "from-clipboard.json"), content.getBytes(), false);
                TMFConfigActivity
                        .startActivity(TMFConfigListActivity.this,
                                new File(getConfigDirPath(), "from-clipboard.json").getAbsolutePath(),
                                "from-clipboard.json");
            } else {
                String tex = mContext.getResources().getString(R.string.tmf_config_list_activity_copy_clip_failed);
                Toast.makeText(this, tex, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SampleAdapter extends BaseAdapter {

        private String[] data;

        public SampleAdapter() {
        }

        public void setData(String[] data) {
            this.data = data;
            notifyDataSetInvalidated();
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.length;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                View view = getLayoutInflater().inflate(R.layout.item_config_file, parent, false);
                mViewHolder.fileNameText = view.findViewById(R.id.text_file_name);
                view.setTag(mViewHolder);
                convertView = view;
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }

            mViewHolder.fileNameText.setText(data[position]);
            return convertView;
        }
    }

    private class ViewHolder {

        TextView fileNameText;
    }

    private boolean copyFilesFromAssets(Context context, String assetsPath, String savePath) {
        try {
            String[] fileNames = context.getAssets().list(assetsPath); // 获取assets目录下的所有文件及目录名
            if (fileNames != null && fileNames.length > 0) { // 如果是目录
                File file = new File(savePath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFromAssets(context, assetsPath + "/" + fileName,
                            savePath + "/" + fileName);
                }
            } else { // 如果是文件
                if (assetsPath.endsWith(".java")) {
                    return true;
                }
                InputStream is = context.getAssets().open(assetsPath);
                FileOutputStream fos = new FileOutputStream(new File(savePath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) { // 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount); // 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
