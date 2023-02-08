package com.tencent.tmf.module.portal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.Portal;
import com.tencent.tmf.portal.annotations.Destination;

/**
 * Created by Xiaomao Yi on 2022/5/31.
 */

@Destination(
        url = PortalConst.SIMPLE_FRAGMENT,
        launcher = Launcher.FRAGMENT,
        description = "路由示例Fragment"
)
public class SimpleFragment extends Fragment {

    private static final int REQUEST_CODE = 102;

    public SimpleFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple, container, false);
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            ((TextView) view.findViewById(R.id.parameter)).setText(getStringById(R.string.module_portal_50) + getArguments().getString("param"));
        }
        view.findViewById(R.id.start_btn_a).setOnClickListener(v -> {
            Portal.from(this)
                    .url(PortalConst.WITH_RESULT_ACTIVITY)
                    .startForActivityResult(REQUEST_CODE)
                    .launch();
        });
        view.findViewById(R.id.start_btn_b).setOnClickListener(v -> {
            Portal.from(this)
                    .url(PortalConst.WITH_RESULT_ACTIVITY)
                    .startActivityWithCallback((i, intent) -> {
                        if (i == Activity.RESULT_OK && intent != null) {
                            showAlert(getStringById(R.string.module_portal_51) + intent.getStringExtra("result"));
                        }
                    })
                    .launch();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            showAlert(getStringById(R.string.module_portal_52) + data.getStringExtra("result"));
        }
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(getStringById(R.string.module_portal_53))
                .setMessage(message)
                .setPositiveButton(getStringById(R.string.module_portal_54), (dialog, which) -> {
                    dialog.cancel();
                }).show();
    }
}
