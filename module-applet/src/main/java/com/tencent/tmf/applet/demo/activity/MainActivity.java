package com.tencent.tmf.applet.demo.activity;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.tencent.tmf.applet.demo.R;
import com.tencent.tmf.applet.demo.dialog.AppidDialog;
import com.tencent.tmf.applet.demo.sp.impl.CommonSp;
import com.tencent.tmf.applet.demo.ui.adapter.AppAdapter;
import com.tencent.tmf.common.gen.ModuleAppletConst;
import com.tencent.tmf.mini.api.TmfMiniSDK;
import com.tencent.tmf.mini.api.bean.MiniApp;
import com.tencent.tmf.mini.api.bean.MiniCode;
import com.tencent.tmf.mini.api.bean.MiniScene;
import com.tencent.tmf.mini.api.bean.MiniStartOptions;
import com.tencent.tmf.mini.api.callback.IRecentMiniCallback;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import qiu.niorgai.StatusBarCompat;
import xiao.framework.adapter.XGCOnRVItemClickListener;
import xiao.framework.adapter.XGCOnRVItemLongClickListener;

@Destination(
        url = ModuleAppletConst.U_MAIN_ACTIVITY,
        launcher = Launcher.ACTIVITY,
        description = "小程序主页面"
)
public class MainActivity extends AppCompatActivity implements OnClickListener, XGCOnRVItemClickListener,
        XGCOnRVItemLongClickListener {

    //定位需要申请的权限
    public static String[] perms = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.VIBRATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //蓝牙
            permission.BLUETOOTH,
            permission.BLUETOOTH_ADMIN,
            permission.BLUETOOTH_PRIVILEGED,
            permission.ACCESS_COARSE_LOCATION,
            "android.permission.BLUETOOTH_SCAN",
            "android.permission.BLUETOOTH_CONNECT",
            //nfc
            permission.NFC,
            //日历
            permission.READ_CALENDAR,
            permission.WRITE_CONTACTS,
            //联系人
            permission.READ_CONTACTS,
            permission.WRITE_CONTACTS,
            //短信
            permission.SEND_SMS,
            permission.READ_SMS
    };
    private static final int ITEMS_PER_ROW = 4;
    private Activity mActivity;
    private AppAdapter mAppAdapter;
    private Toolbar mToolbar;
    private ImageView mSettingImg;
    private ImageView mSearchImg;
    private ImageView mMenuImg;
    private CardView mCardView;
    private RecyclerView mRecyclerView;
    private TextView mHintText;
    private QMUIPopup mNormalPopup;
    private QMUIListPopup mListPopup;
    private ResultReceiver mResultReceiver = new ResultReceiver(new Handler()) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode != MiniCode.CODE_OK) {
                String errMsg = resultData.getString("errMsg");
                Toast.makeText(mActivity, errMsg + resultCode, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applet_activity_applet_main);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.applet_color_primary_dark));

        mActivity = this;
        initView();
        initRecyclerView();

    }


    private static final int REQUEST_PERMISSION = 4040;

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> noPermissionList = new ArrayList<>();
            for (String permission : perms) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    noPermissionList.add(permission);
                }
            }
            if (noPermissionList.size() > 0) {
                String[] p = new String[noPermissionList.size()];
                p = noPermissionList.toArray(p);
                requestPermissions(p, REQUEST_PERMISSION);
//                requestPermissions(p, REQUEST_PERMISSION);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mSettingImg = findViewById(R.id.setting_img);
        mSearchImg = findViewById(R.id.search_img);
        mMenuImg = findViewById(R.id.menu_img);
        mCardView = findViewById(R.id.cardView);
        mRecyclerView = findViewById(R.id.recyclerView1);
        mHintText = findViewById(R.id.hint_text);

        mSettingImg.setOnClickListener(this);
        mSearchImg.setOnClickListener(this);
        mMenuImg.setOnClickListener(this);
    }

    private void initRecyclerView() {
        mAppAdapter = new AppAdapter(this);
        mAppAdapter.setOnRVItemClickListener(this);
        mAppAdapter.setOnRVItemLongClickListener(this);
        mRecyclerView.setAdapter(mAppAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, ITEMS_PER_ROW));
    }

    @Override
    protected void onResume() {
        super.onResume();
        TmfMiniSDK.getRecentList(new IRecentMiniCallback() {
            @Override
            public void get(List<MiniApp> data) {
                mAppAdapter.setDatas(data);
                updateUi();
            }
        });
    }

    private void updateUi() {
        List<MiniApp> data = mAppAdapter.getDatas();
        if (data != null && data.size() > 0) {
            mCardView.setVisibility(View.VISIBLE);
            mHintText.setVisibility(View.GONE);
        } else {
            mCardView.setVisibility(View.GONE);
            mHintText.setVisibility(View.VISIBLE);
        }
    }

    private void delete(final int pos) {
        final MiniApp item = mAppAdapter.getItem(pos);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.applet_main_act_delete)//标题
                .setMessage(getString(R.string.applet_main_act_delete_msg, item.appId, item.version))//内容
                .setPositiveButton(R.string.applet_main_act_delete_msg_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TmfMiniSDK.deleteMiniApp(item.appId, item.appVerType, item.version);
                        mAppAdapter.removeItem(pos);
                        updateUi();
                    }
                })
                .setNegativeButton(R.string.applet_main_act_delete_msg_cancal, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.menu_img) {
            showMenu1();
        } else if (id == R.id.search_img) {
            startActivity(new Intent(this, SearchActivity.class));
        }
    }

    private void showMenu1() {
        String[] listItems = getResources().getStringArray(R.array.applet_menu_item);
        ArrayList<MenuData> menuData = new ArrayList<>();
        menuData.add(new MenuData(listItems[0], R.mipmap.applet_ic_scan));
        menuData.add(new MenuData(listItems[1], R.mipmap.applet_ic_search));
        menuData.add(new MenuData(listItems[2], R.mipmap.applet_ic_debug));
        menuData.add(new MenuData(listItems[3], R.mipmap.applet_ic_logout));
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        CustomAdapter adapter = new CustomAdapter();
        adapter.setData(menuData);
        mListPopup = new QMUIListPopup(this, QMUIPopup.DIRECTION_NONE, adapter);
        mListPopup.create(QMUIDisplayHelper.dp2px(this, 160), QMUIDisplayHelper.dp2px(this, 300),
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i) {
                            case 0:
                                TmfMiniSDK.scan(MainActivity.this);
//                                Log.d("xiao1", TmfMiniSDK.getAuthStateList("tmfgzswcderhdbst8l", MiniApp.TYPE_DEVELOP).toString());
//                                TmfMiniSDK.setAuthState("tmfgzswcderhdbst8l", MiniApp.TYPE_DEVELOP, "scope.userInfo", false);
                                break;
                            case 1:
                                new AppidDialog(MainActivity.this).show();
                                break;
                            case 2:
                                startActivity(new Intent(MainActivity.this, DebugActivity.class));
                                break;
                            case 3:
                                TmfMiniSDK.logout();
                                CommonSp.getInstance().removeSkipLogin();
                                finish();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                break;
                        }
                        if (mListPopup != null) {
                            mListPopup.dismiss();
                        }
                    }
                });

        mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        mListPopup.setPreferredDirection(QMUIPopup.DIRECTION_BOTTOM);
        mListPopup.show(mMenuImg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        JSONObject scanResult = TmfMiniSDK.getScanResult(requestCode, data);
        if (scanResult != null) {
            //获取内置扫码组件二维码内容
            String result = scanResult.optString("result");
            if (!TextUtils.isEmpty(result)) {
                //处理小程序二维码，非小程序二维码会返回错误
                TmfMiniSDK.startMiniAppByLink(this, result, mResultReceiver);
            }
        }
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        MiniApp item = mAppAdapter.getItem(position);

        MiniStartOptions miniStartOptions = new MiniStartOptions();
        miniStartOptions.resultReceiver = mResultReceiver;
        //"entryPagePath": "page/API/index",
        //pages/cart/index.html
//        miniStartOptions.entryPath = "packageAPI/pages/api/login/login.html";
//        miniStartOptions.entryPath = "pages/cart/index";
//        miniStartOptions.entryPath = "/pages/webH5/index?url=https%3A%2F%2Fwx.vzan.com%2Flive%2Ftvchat-1765195222%3Fv%3D1671595835420";
        TmfMiniSDK.startMiniApp(this, item.appId, MiniScene.LAUNCH_SCENE_MAIN_ENTRY, item.appVerType, miniStartOptions);
    }

    @Override
    public boolean onRVItemLongClick(ViewGroup parent, View itemView, int position) {
//        AppEntity item = mAppAdapter.getItem(position);
//        if (TmfAppletService.isTestApp(item.appId)) {
//            Toast.makeText(MainActivity.this, "本地测试小程序不支持删除", Toast.LENGTH_LONG)
//                    .show();
//        } else {
//            delete(position);
//        }
        delete(position);
        return true;
    }

    public void checkPermisison(View view) {
        checkPermission();
    }

    public class CustomAdapter extends BaseAdapter {

        private List<MenuData> data = new ArrayList<>();

        public void setData(List<MenuData> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();

            if (convertView == null) {
                //把XML文件读到Java中，变成一个View对象用于操作
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.applet_popup_menu_list_item, null);

                // 获取控件
                viewHolder.mAppIconImageView = (ImageView) convertView.findViewById(R.id.icon_img);
                viewHolder.mAppNameTextView = (TextView) convertView.findViewById(R.id.text);
                //让convertView和viewHolder之间有一个对应关系
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            MenuData menuData = data.get(position);
            viewHolder.mAppNameTextView.setText(menuData.title);
            viewHolder.mAppIconImageView.setImageResource(menuData.iconResId);

            return convertView;
        }
    }

    public static class ViewHolder {

        public ImageView mAppIconImageView;
        public TextView mAppNameTextView;
    }

    private static class MenuData {

        public String title;
        public int iconResId;

        public MenuData(String title, int iconResId) {
            this.title = title;
            this.iconResId = iconResId;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("xiao1", "onDestroy");
    }
}
