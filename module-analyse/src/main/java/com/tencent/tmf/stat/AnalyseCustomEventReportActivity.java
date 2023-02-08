package com.tencent.tmf.stat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import androidx.annotation.Nullable;
import com.tencent.tmf.base.api.utils.LogUtil;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.utils.JsonUtil;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.stat.api.TMFStatService;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AnalyseCustomEventReportActivity extends TopBarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopBar.setTitle(getString(R.string.analyse_custom_report));
        initViewData();

        findViewById(R.id.analyse_custom_event_report).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventId = getTextStringFromEditByById(R.id.analyse_custom_event_key);
                String value = getTextStringFromEditByById(R.id.analyse_custom_event_value);
                if (!TextUtils.isEmpty(eventId)) {
                    try {
                        JSONObject jsonObject = new JSONObject(value);
                        Map<String, String> data = JsonUtil.json2Map(jsonObject);
                        LogUtil.d("custom event report " + jsonObject + " eventId " + eventId);
                        //上报自定义数据
                        TMFStatService.reportEvent(eventId, data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.showToast("自定义事件参数异常，请按照标准Json格式填充数据");
                    }
                } else {
                    ToastUtil.showToast("自定义事件key不能为空");
                }
            }
        });
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_custom_event_report, null);
    }

    private String getTextStringFromEditByById(int id) {
        EditText editText = findViewById(id);
        if (null != editText) {
            return editText.getText().toString().trim();
        }
        return "";
    }

    private void setEditTextById(int id, String text) {
        EditText editText = findViewById(id);
        editText.setText(text);
    }

    private void initViewData() {
        String eventId = "100053";
        setEditTextById(R.id.analyse_custom_event_key, eventId);
        Map<String, String> data = new HashMap<>();
        data.put("custom1", "hello");
        data.put("custom2", "tmf");
        data.put("custom3", "customReport");
        data.put("XXName", "tmf66666");
        setEditTextById(R.id.analyse_custom_event_value, JsonUtil.map2json(data).toString());

    }
}
