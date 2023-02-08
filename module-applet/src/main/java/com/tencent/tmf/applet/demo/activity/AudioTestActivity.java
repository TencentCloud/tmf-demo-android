package com.tencent.tmf.applet.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.tmf.applet.demo.R;

public class AudioTestActivity extends Activity implements OnClickListener {

    TextView mListenerStateCallback;
    EditText mParamName;
    EditText mParamValue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applet_activity_audio_test);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_audio_createInnerAudioContext).setOnClickListener(this);
        findViewById(R.id.btn_audio_InnerAudioContext_play).setOnClickListener(this);
        findViewById(R.id.btn_audio_InnerAudioContext_pause).setOnClickListener(this);
        findViewById(R.id.btn_audio_InnerAudioContext_seek).setOnClickListener(this);
        findViewById(R.id.btn_audio_InnerAudioContext_regist).setOnClickListener(this);
        findViewById(R.id.btn_audio_InnerAudioContext_set).setOnClickListener(this);
        findViewById(R.id.btn_audio_InnerAudioContext_get).setOnClickListener(this);
        mListenerStateCallback = findViewById(R.id.tv_audio_state_listen);
        mParamName = findViewById(R.id.et_audio_InnerAudioContext_param_name);
        mParamValue = findViewById(R.id.et_audio_InnerAudioContext_param_value);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_audio_createInnerAudioContext) {
        } else if (id == R.id.btn_audio_InnerAudioContext_play) {
        } else if (id == R.id.btn_audio_InnerAudioContext_pause) {
        } else if (id == R.id.btn_audio_InnerAudioContext_seek) {
        } else if (id == R.id.btn_audio_InnerAudioContext_regist) {
        } else if (id == R.id.btn_audio_InnerAudioContext_set) {
        } else if (id == R.id.btn_audio_InnerAudioContext_get) {
        }

    }
}
