package com.tencent.tmf.module.main.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.qmuiteam.qmui.widget.QMUIAnimationListView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.gen.ModuleAnalyseConst;
import com.tencent.tmf.common.gen.ModuleColorlogConst;
import com.tencent.tmf.common.gen.ModuleConchConst;
import com.tencent.tmf.common.gen.ModuleGmConst;
import com.tencent.tmf.common.gen.ModuleH5containerConst;
import com.tencent.tmf.common.gen.ModuleHotpatchConst;
import com.tencent.tmf.common.gen.ModuleHybridConst;
import com.tencent.tmf.common.gen.ModuleIcdpConst;
import com.tencent.tmf.common.gen.ModuleKeyboardConst;
import com.tencent.tmf.common.gen.ModuleLocationConst;
import com.tencent.tmf.common.gen.ModuleOfflineConst;
import com.tencent.tmf.common.gen.ModulePortalConst;
import com.tencent.tmf.common.gen.ModulePushConst;
import com.tencent.tmf.common.gen.ModuleQapmConst;
import com.tencent.tmf.common.gen.ModuleScanConst;
import com.tencent.tmf.common.gen.ModuleShareConst;
import com.tencent.tmf.common.gen.ModuleSharkConst;
import com.tencent.tmf.common.gen.ModuleStorageConst;
import com.tencent.tmf.common.gen.ModuleSubinstanceConst;
import com.tencent.tmf.common.gen.ModuleUpgradeConst;
import com.tencent.tmf.common.gen.ModuleUploadConst;
import com.tencent.tmf.common.service.IAppletService;
import com.tencent.tmf.demo.main.R;
import com.tencent.tmf.demo.qmui.QDMainActivity;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.portal.annotations.Destination;
import java.util.ArrayList;
import java.util.List;

@Destination(
        url = "portal://com.tencent.tmf.module.main/component-list-activity",
        launcher = Launcher.ACTIVITY,
        description = "TMF测试主页"
)
public class ComponentListActivity extends TopBarActivity {

    private List<ItemData> data = new ArrayList<>();
    private QMUIAnimationListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListView = mContentView.findViewById(R.id.listview);
        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                String content = bundle.getString(key);
                Log.i("TMF_PUSH",
                        "ComponentListActivity, receive data from push, key = " + key + ", content = " + content);
            }
        }
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_main, null);
    }

    private String getStringById(int id){
        return getResources().getString(id);
    }
    private void initView() {
        mTopBar.setTitle(getResources().getString(R.string.app_name));
        data.add(new ItemData(getStringById(R.string.item_0), ModuleSharkConst.U_SHARK_POC_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_1), ModuleConchConst.U_CONCH_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_2), ModulePushConst.U_PUSH_TEST_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_3), ModuleHotpatchConst.U_HOT_PATCH_TEST_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_4), ModuleH5containerConst.U_H_5_CONTAINER_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_5), ModuleOfflineConst.U_OFFLINE_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_6), ModuleQapmConst.U_PERFORMANCE_MAIN_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_7), ModuleUploadConst.U_UPLOAD_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_8), ModuleColorlogConst.U_COLOR_LOG_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_9), ModuleKeyboardConst.U_KEYBOARD_DEMO_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_10), ModuleUpgradeConst.U_POC_UPGRADE_ACTIVITY));
//        data.add(new ItemData("小应用", ModuleTinyappConst.U_TINY_APP_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_11), ModuleScanConst.U_Q_R_CODE_SCAN_MAIN_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_12), ""));
        data.add(new ItemData(getStringById(R.string.item_13), ModuleStorageConst.U_STORAGE_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_14), ModuleShareConst.U_SHARE_MAIN_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_15), ModuleLocationConst.U_LOCATION_REPORT_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_16), ModuleSharkConst.U_DEVICE_ID_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_17), ModuleIcdpConst.U_ICDP_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_18), ModulePortalConst.U_PORTAL_TEST_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_19), ModuleGmConst.U_G_M_DEMO_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_20), ModuleSubinstanceConst.U_SUB_INSTANCE_DEMO_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_21), ModuleHybridConst.U_HYBRID_ACTIVITY));
        data.add(new ItemData(getStringById(R.string.item_22), ""));
        data.add(new ItemData(getStringById(R.string.item_24), ModuleAnalyseConst.U_ANALYSE_ACTIVITY));

        SampleAdapter adapter = new SampleAdapter();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                performOnClick(position);
            }
        });
    }

    private void performOnClick(int position) {
        if (getStringById(R.string.item_12).equalsIgnoreCase(data.get(position).name)) {
            startActivity(new Intent(this, QDMainActivity.class));
        } else if (getStringById(R.string.item_22).equalsIgnoreCase(data.get(position).name)) {
            IAppletService service = Portal.getService(IAppletService.class);
            service.startAppletModule(this);
        } else {
            Portal.from(this)
                    .url(data.get(position).url)
                    .launch();
        }
    }

    private class SampleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position).name;
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
                View view = ComponentListActivity.this.getLayoutInflater().inflate(R.layout.list_item, parent, false);
                mViewHolder.mItemBtn = view.findViewById(R.id.btn_title);
                view.setTag(mViewHolder);
                convertView = view;
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }

            mViewHolder.mItemBtn.setText(data.get(position).name);
            return convertView;
        }
    }

    private class ViewHolder {

        QMUIRoundButton mItemBtn;
    }

    private class ItemData {

        public String name;
        public String url;

        public ItemData(String name, String url) {
            this.name = name;
            this.url = url;
        }
    }
}
