package com.tencent.tmf.module.qapm.performance.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;
import com.qmuiteam.qmui.widget.QMUIAnimationListView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.common.BuildConfig;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.module.qapm.R;
import com.tencent.tmf.module.qapm.performance.PerformanceConstants;
import com.tencent.tmf.module.qapm.performance.api.TMFPerformanceConstants;
import com.tencent.tmf.module.qapm.performance.api.TMFPerformanceService;

/**
 * Author: jinfazhang
 * Date: 2019/4/16 21:37
 */
public class DropFrameActivity extends TopBarActivity {

    private static final String TAG = "DropFrameActivity";
    private String[] mTitles;
    private QMUIAnimationListView mListView;
    private Handler mHandler;

    private class ExitRun implements Runnable {

        @Override
        public void run() {
            DropFrameActivity.this.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListView = mContentView.findViewById(R.id.listview);
        int size = 100;
        mTitles = new String[size];
        for (int i = 0; i < size; i++) {
            mTitles[i] = "item" + i;
        }
        initView();
        if (null == mHandler) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        mHandler.postDelayed(new Runnable() {
            int count = 0;

            @Override
            public void run() {
                mListView.smoothScrollToPosition(600);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "count:" + count);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mListView.smoothScrollToPosition(0);
                if (count < 5) {
                    mHandler.postDelayed(this, 1000);
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "finish!" + count);
                    }
                    mHandler.postDelayed(new ExitRun(), 1000);
                }
                count += 1;
            }
        }, 1000);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_drop_frame, null);
    }

    private void initView() {
        mTopBar.setTitle(R.string.performance_test_slide_list);

        SampleAdapter adapter = new SampleAdapter();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DropFrameActivity.this, "click item" + position, Toast.LENGTH_SHORT).show();
            }
        });
        if (Build.VERSION.SDK_INT >= 19) {
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        TMFPerformanceService.endScene(PerformanceConstants.SCENE_LISTVIEW_DROP_FRAME,
                                TMFPerformanceConstants.MODE_DROP_FRAME);
                    } else {
                        TMFPerformanceService.beginScene(PerformanceConstants.SCENE_LISTVIEW_DROP_FRAME,
                                TMFPerformanceConstants.MODE_DROP_FRAME);
                    }
                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                }
            });
        }
    }

    private class SampleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitles[position];
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
                View view = DropFrameActivity.this.getLayoutInflater().inflate(R.layout.list_item, parent, false);
                mViewHolder.mItemBtn = view.findViewById(R.id.btn_title);
                view.setTag(mViewHolder);
                convertView = view;
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }

            mViewHolder.mItemBtn.setText(mTitles[position]);
            return convertView;
        }
    }

    private class ViewHolder {

        QMUIRoundButton mItemBtn;
    }
}
