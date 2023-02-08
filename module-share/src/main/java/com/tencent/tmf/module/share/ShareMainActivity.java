package com.tencent.tmf.module.share;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.share.ShareManager;
import com.tencent.tmf.share.api.ITMFShareListener;
import com.tencent.tmf.share.api.TMFShareContants;
import com.tencent.tmf.share.api.TMFShareService;

@Destination(
        url = "portal://com.tencent.tmf.module.share/share-main-activity",
        launcher = Launcher.ACTIVITY,
        description = "分享组件的主界面"
)
public class ShareMainActivity extends TopBarActivity implements View.OnClickListener, ITMFShareListener {

    private static final String TAG = "ShareMainActivity";

    private QMUICommonListItemView shareToWxTimeline;
    private QMUICommonListItemView shareToAlipayTimeline;
    private QMUICommonListItemView shareWeiboWord;
    private QMUICommonListItemView shareWeiboImage;
    private QMUICommonListItemView shareWxBitmapImage;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(PERMISSIONS_STORAGE, 0);
            }
        }

        mTopBar.setTitle(getStringById(R.string.module_share_1));

        // 微信分享
        shareToWxTimeline = findViewById(R.id.wx_share_timeline);
        shareToWxTimeline.setText(getStringById(R.string.module_share_2));
        shareToWxTimeline.getSwitch().setChecked(true);
        shareToWxTimeline.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String toastText = isChecked ? getStringById(R.string.module_share_3) : getStringById(R.string.module_share_4);
                Toast.makeText(ShareMainActivity.this, toastText, Toast.LENGTH_SHORT).show();
            }
        });
        shareToWxTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareToWxTimeline.getSwitch().toggle();
            }
        });
        shareWxBitmapImage = findViewById(R.id.wx_share_image_bitmap);
        shareWxBitmapImage.setText(getStringById(R.string.module_share_5));
        shareWxBitmapImage.getSwitch().setChecked(true);
        shareWxBitmapImage.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String toastText = isChecked ? getStringById(R.string.module_share_5) : getStringById(R.string.module_share_6);
                Toast.makeText(ShareMainActivity.this, toastText, Toast.LENGTH_SHORT).show();
            }
        });
        shareWxBitmapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareWxBitmapImage.getSwitch().toggle();
            }
        });
        findViewById(R.id.wx_share_word).setOnClickListener(this);
        findViewById(R.id.wx_share_image).setOnClickListener(this);
        findViewById(R.id.wx_share_webpage).setOnClickListener(this);

        // 企业微信分享
        findViewById(R.id.cwx_share_word).setOnClickListener(this);
        findViewById(R.id.cwx_share_image).setOnClickListener(this);
        findViewById(R.id.cwx_share_webpage).setOnClickListener(this);
        findViewById(R.id.cwx_share_video).setOnClickListener(this);
        findViewById(R.id.cwx_share_file).setOnClickListener(this);

        // 支付宝分享
        shareToAlipayTimeline = findViewById(R.id.alipay_share_timeline);
        if (TMFShareService.isIgnoreAlipayShareChannel(this)) {
            shareToAlipayTimeline.setVisibility(View.GONE);
        } else {
            shareToAlipayTimeline.setVisibility(View.VISIBLE);
        }
        shareToAlipayTimeline.setText(getStringById(R.string.module_share_7));
        shareToAlipayTimeline.getSwitch().setChecked(true);
        shareToAlipayTimeline.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String toastText = isChecked ? getStringById(R.string.module_share_8) : getStringById(R.string.module_share_9);
                Toast.makeText(ShareMainActivity.this, toastText, Toast.LENGTH_SHORT).show();
            }
        });
        shareToAlipayTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareToAlipayTimeline.getSwitch().toggle();
            }
        });
        findViewById(R.id.alipay_share_word).setOnClickListener(this);
        findViewById(R.id.alipay_share_image).setOnClickListener(this);
        findViewById(R.id.alipay_share_webpage).setOnClickListener(this);

        findViewById(R.id.ding_share_word).setOnClickListener(this);
        findViewById(R.id.ding_share_image).setOnClickListener(this);
        findViewById(R.id.ding_share_webpage).setOnClickListener(this);

        // 新浪微博分享
        shareWeiboWord = findViewById(R.id.weibo_share_word);
        shareWeiboWord.setText(getStringById(R.string.module_share_10));
        shareWeiboWord.getSwitch().setChecked(true);
        shareWeiboWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareWeiboWord.getSwitch().toggle();
            }
        });

        shareWeiboImage = findViewById(R.id.weibo_share_image);
        shareWeiboImage.setText(getStringById(R.string.module_share_11));
        shareWeiboImage.getSwitch().setChecked(true);
        shareWeiboImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareWeiboImage.getSwitch().toggle();
            }
        });
        findViewById(R.id.weibo_share).setOnClickListener(this);

        // QQ分享
        findViewById(R.id.qq_share_image).setOnClickListener(this);
        findViewById(R.id.qq_share_word_and_image).setOnClickListener(this);

        // 短信分享
        findViewById(R.id.sms_share).setOnClickListener(this);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_share_main, null);
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.wx_share_word) {
            ShareManager.shareWordToWx(this, this, shareToWxTimeline.getSwitch().isChecked());
        } else if (viewId == R.id.wx_share_image) {
            ShareManager.shareImageToWx(this, this,
                    shareToWxTimeline.getSwitch().isChecked(), shareWxBitmapImage.getSwitch().isChecked());
        } else if (viewId == R.id.wx_share_webpage) {
            ShareManager.shareWebpageToWx(this, this, shareToWxTimeline.getSwitch().isChecked());
        } else if (viewId == R.id.cwx_share_word) {
            ShareManager.shareWordToCwx(this, this);
        } else if (viewId == R.id.cwx_share_image) {
            ShareManager.shareImageToCwx(this, this);
        } else if (viewId == R.id.cwx_share_webpage) {
            ShareManager.shareWebpageToCwx(this, this);
        } else if (viewId == R.id.cwx_share_file) {
            ShareManager.shareFileToCwx(this, this);
        } else if (viewId == R.id.cwx_share_video) {
            ShareManager.shareVideoToCwx(this, this);
        } else if (viewId == R.id.alipay_share_word) {
            ShareManager.shareWordToAlipay(this, this, shareToAlipayTimeline.getSwitch().isChecked());
        } else if (viewId == R.id.alipay_share_image) {
            ShareManager.shareImageToAlipay(this, this, shareToAlipayTimeline.getSwitch().isChecked());
        } else if (viewId == R.id.alipay_share_webpage) {
            ShareManager.shareWebpageToAlipay(this, this, shareToAlipayTimeline.getSwitch().isChecked());
        } else if (viewId == R.id.ding_share_word) {
            ShareManager.shareWordToDingDing(this, this);
        } else if (viewId == R.id.ding_share_image) {
            ShareManager.shareImageToDingDing(this, this);
        } else if (viewId == R.id.ding_share_webpage) {
            ShareManager.shareWebpageToDingDing(this, this);
        } else if (viewId == R.id.weibo_share) {
            ShareManager.shareToWeibo(this, this,
                    shareWeiboWord.getSwitch().isChecked(), shareWeiboImage.getSwitch().isChecked());
        } else if (viewId == R.id.qq_share_image) {
            ShareManager.shareImageToQQ(this, this);
        } else if (viewId == R.id.qq_share_word_and_image) {
            ShareManager.shareWordAndImageToQQ(this, this);
        } else if (viewId == R.id.sms_share) {
            ShareManager.shareToSMS(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TMFShareService.getInstance().handleWeiboIntent(data);
        TMFShareService.getInstance().handleQQIntent(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TMFShareService.getInstance().release();
    }

    @Override
    public void onSuccess(int shareType, Object object) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG,
                    getStringById(R.string.module_share_12) + shareType + getStringById(R.string.module_share_13) + (object != null ? object.toString() : "object is null"));
        }
        Toast.makeText(this, getStringById(R.string.module_share_14), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int shareType, int errCode, String errMessage) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, getStringById(R.string.module_share_15) + shareType + getStringById(R.string.module_share_16) + errCode + getStringById(R.string.module_share_17) + errMessage);
        }
        switch (errCode) {
            case TMFShareContants.ErrorCode.APP_NO_INSTALL:
                Toast.makeText(this, getStringById(R.string.module_share_18) + errMessage, Toast.LENGTH_SHORT).show();
                break;
            case TMFShareContants.ErrorCode.USER_CANCEL:
                Toast.makeText(this, getStringById(R.string.module_share_19) + errMessage, Toast.LENGTH_SHORT).show();
                break;
            case TMFShareContants.ErrorCode.AUTH_FAIL:
                Toast.makeText(this, getStringById(R.string.module_share_20) + errMessage, Toast.LENGTH_SHORT).show();
                break;
            case TMFShareContants.ErrorCode.UNVALID_PARAM:
                Toast.makeText(this, getStringById(R.string.module_share_21) + errMessage, Toast.LENGTH_SHORT).show();
                break;
            case TMFShareContants.ErrorCode.INTERFACE_NOT_SUPPORT:
                Toast.makeText(this, getStringById(R.string.module_share_22) + errMessage, Toast.LENGTH_SHORT).show();
                break;
            case TMFShareContants.ErrorCode.SEND_REQ_FAIL:
                Toast.makeText(this, getStringById(R.string.module_share_23) + errMessage, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, getStringById(R.string.module_share_24) + errMessage, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
