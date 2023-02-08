package com.tencent.tmf.module.portal;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.Request;
import com.tencent.tmf.portal.Response;

/**
 * Created by Xiaomao Yi on 2022/6/1.
 */
public class CustomLauncherFactory implements Launcher.Factory {

    public static final String NAME = "custom";

    @NonNull
    @Override
    public Launcher newLauncher(@NonNull Request request) {
        return new CustomLauncher(request);
    }

    @NonNull
    @Override
    public String name() {
        return NAME;
    }

    private static class CustomLauncher implements Launcher {

        public CustomLauncher(Request request) {
            this.mRequest = request;
        }

        private final Request mRequest;

        @Override
        public void launch(Callback callback) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mRequest.context())
                    .setTitle(mRequest.context().getResources().getString(R.string.module_portal_0))
                    .setMessage(mRequest.context().getString(R.string.module_portal_1))
                    .setPositiveButton(mRequest.context().getString(R.string.module_portal_2), (dialog, which) -> {
                        dialog.cancel();
                    });
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(builder::show);

            callback.onComplete(Response.create(Response.STATUS_SUCCESS).build());
        }
    }
}
