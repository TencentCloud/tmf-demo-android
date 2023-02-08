package com.tencent.tmf.applet.demo.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.tmf.mini.api.TmfMiniSDK;
import com.tencent.tmf.applet.demo.ModuleApplet;
import com.tencent.tmf.applet.demo.R;
import com.tencent.tmf.applet.demo.sp.impl.CommonSp;
import com.tencent.tmf.applet.demo.ui.SpaceItemDecoration;
import com.tencent.tmf.applet.demo.ui.adapter.DebugAdapter;
import com.tencent.tmf.applet.demo.ui.entity.DebugEntity;

import java.util.List;
import org.json.JSONObject;

import java.util.ArrayList;

import qiu.niorgai.StatusBarCompat;
import xiao.framework.adapter.XGCOnRVItemClickListener;

public class DebugActivity extends AppCompatActivity implements XGCOnRVItemClickListener {

    private Toolbar mToolbar;
    private ImageView mAddImg;
    private RecyclerView mRecyclerView;
    private DebugAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applet_activity_debug);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.applet_color_primary_dark));
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAddImg = (ImageView) findViewById(R.id.menu_img);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView1);

        mAdapter = new DebugAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(0, 0, 0, 1, 1));
        mAdapter.setOnRVItemClickListener(this);

        ArrayList<DebugEntity> serverConfigEntities = new ArrayList<>();
        serverConfigEntities.add(new DebugEntity("UserName", CommonSp.getInstance().getUserName(), DebugEntity.TYPE_1));
        JSONObject debugInfo = TmfMiniSDK.getDebugInfo();
        serverConfigEntities
                .add(new DebugEntity("BaseLibVersion", debugInfo.optString("MiniBaseLibVersion"), DebugEntity.TYPE_1));
        serverConfigEntities.add(new DebugEntity("IMEI", ModuleApplet.IMEI, DebugEntity.TYPE_1));
        serverConfigEntities.add(new DebugEntity("MODEL", Build.MODEL, DebugEntity.TYPE_1));
        serverConfigEntities.add(new DebugEntity("MANUFACTURER", Build.MANUFACTURER, DebugEntity.TYPE_1));
        serverConfigEntities.add(new DebugEntity("BOARD", Build.BOARD, DebugEntity.TYPE_1));
        serverConfigEntities
                .add(new DebugEntity("TcpHost", debugInfo.optString("TcpHost"), DebugEntity.TYPE_1));
        serverConfigEntities
                .add(new DebugEntity("HttpUrl", debugInfo.optString("HttpUrl"), DebugEntity.TYPE_1));
        serverConfigEntities.add(new DebugEntity("AreaInfo", getString(R.string.applet_mini_data_country) + " " + getString(R.string.applet_mini_proxy_province) + " " + getString(R.string.applet_mini_proxy_city),
                DebugEntity.TYPE_1));
        serverConfigEntities.add(new DebugEntity("SdkVersion", debugInfo.optString("SdkVersion"), DebugEntity.TYPE_1));
        serverConfigEntities.add(new DebugEntity("GUID", debugInfo.optString("GUID"), DebugEntity.TYPE_1));
        serverConfigEntities
                .add(new DebugEntity("ProductId", debugInfo.optString("ProductId"),
                        DebugEntity.TYPE_1));
//        serverConfigEntities.add(new DebugEntity("清除小程序基础库", "点击清除已下载的小程序基础库", DebugEntity.TYPE_2));
//        serverConfigEntities.add(new DebugEntity("小程序测试", "点击进入小程序测试页面", DebugEntity.TYPE_2));
        serverConfigEntities.add(new DebugEntity("CheckPermission", "CheckPermission", DebugEntity.TYPE_2));
        mAdapter.setDatas(serverConfigEntities);
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        if ("CheckPermission".equals(mAdapter.getItem(position).title)){
            checkPermission();
        }
//        if ("清除小程序基础库".equals(mAdapter.getItem(position).title)) {
//            TmfAppletService.clearBaseLib();
//            mAdapter.getItem(1).content = TmfAppletService.getLocalBaseLibVersion();
//            mAdapter.notifyItemChanged(1);
//        } else if ("小程序测试".equals(mAdapter.getItem(position).title)) {
//
//        }
    }
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> noPermissionList = new ArrayList<>();
            for (String permission : MainActivity.perms) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    noPermissionList.add(permission);
                }
            }
            if (noPermissionList.size() > 0) {
                String[] p = new String[noPermissionList.size()];
                p = noPermissionList.toArray(p);
                requestPermissions(p, 9527);
//                requestPermissions(p, REQUEST_PERMISSION);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
