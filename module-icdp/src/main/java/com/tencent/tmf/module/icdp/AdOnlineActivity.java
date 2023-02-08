package com.tencent.tmf.module.icdp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.tmf.icdp.ICDPBannerView;
import com.tencent.tmf.icdp.ICDPBroadcastView;
import com.tencent.tmf.icdp.ICDPImageView;
import com.tencent.tmf.icdp.ICDPListView;
import com.tencent.tmf.icdp.ICDPPopupView;

public class AdOnlineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_online);
        initView();
    }

    private void initView() {
        ICDPBannerView icdpBanner = findViewById(R.id.icdp_banner);
        icdpBanner.setBoothIdentityId(Config.getInstance(this).getBanner());

        ICDPBroadcastView icdpBroadcast = findViewById(R.id.icdp_broadcast);
        icdpBroadcast.setBoothIdentityId(Config.getInstance(this).getBroadcast());

        ICDPListView icdpList = findViewById(R.id.icdp_list);
        icdpList.setBoothIdentityId(Config.getInstance(this).getList());

        ICDPImageView icdpImage = findViewById(R.id.icdp_image);
        icdpImage.setBoothIdentityId(Config.getInstance(this).getImage());

        ICDPPopupView.showAsDialogFragmentAsync(Config.getInstance(this).getPopup(), this, null);
    }
}
