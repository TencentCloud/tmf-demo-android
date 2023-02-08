package com.tencent.tmf.module.keyboard;

import Protocol.Keyboard.ReqRecoverInputText;
import Protocol.Keyboard.RespRecoverInputText;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.qmuiteam.qmui.util.QMUIColorHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qq.taf.jce.JceStruct;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.keyboard.Consts;
import com.tencent.tmf.common.shark.SharkService;
import com.tencent.tmf.gm.Sm2;
import com.tencent.tmf.gm.Sm4;
import com.tencent.tmf.gm.SmBase;
import com.tencent.tmf.keyboard.api.ICustomKeyboardWrapper;
import com.tencent.tmf.keyboard.api.IInputCompleteListener;
import com.tencent.tmf.keyboard.api.KeyboardWrapperApi;
import com.tencent.tmf.keyboard.component.keyboard.CustomKeyboardWrapper;
import com.tencent.tmf.keyboard.component.text.PasswordEditText;
import com.tencent.tmf.keyboard.utils.EncodeUtil;
import com.tencent.tmf.keyboard.utils.Tools;
import com.tencent.tmf.shark.api.ESharkCode;
import com.tencent.tmf.shark.api.ISendJceCallback;
import com.tencent.tmf.shark.api.IShark;
import com.tencent.tmf.shark.api.ISharkCallBack;
import com.tencent.tmf.shark.api.SharkExtra;
import com.tencent.tmf.shark.api.SharkRetCode;
import java.nio.charset.Charset;


@Destination(
        url = "portal://com.tencent.tmf.module.keyboard/keyboard-demo-activity",
        launcher = Launcher.ACTIVITY,
        description = "安全键盘演示"
)
/*
 * 动态创建键盘View 放入PopupWindow 弹出 集成演示
 */
public class KeyboardDemoActivity extends TopBarActivity {

    private Switch mBorderSwitch = null;
    private Switch mShowPlainTextSwitch = null;
    private Switch mScreenShotSwitch = null;
    private PasswordEditText mSafeEditText = null;
    private SeekBar mBorderColorBar = null;
    private SeekBar mTextGapBar = null;
    private SeekBar mBorderWidthBar = null;
    private SeekBar mBorderCornerBar = null;
    private SeekBar mTextLengthBar = null;
    private RadioGroup mBorderColorOptions = null;
    private int mTextGap = 0;
    private int mBorderNormalStateColor = Color.BLACK;
    private int mBorderSelectedStateColor = Color.BLACK;
    private int mNorFillColor = Color.TRANSPARENT;
    private int mSelectedFillColor = Color.TRANSPARENT;
    private int mConfigBorderColorType = IConfigBorderColorType.NORMAL_STATE_BORDER_COLOR;
    private float mBorderWidth;
    private float mBorderCorner;
    private int mTextLength = 6;
    // 显示日志组件
    private TextView mLogText;

    private interface IConfigBorderColorType {

        int NORMAL_STATE_BORDER_COLOR = 1;
        int SELECTED_STATE_BORDER_COLOR = 2;
        int NORMAL_STATE_FILL_COLOR = 3;
        int SELECTED_STATE_FILL_COLOR = 4;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // title和右上角按钮
        String text7 = getResources().getString(R.string.keyboard_demo_activity);
        mTopBar.setTitle(text7);

        mBorderSwitch = findViewById(R.id.switch_border_state);
        mShowPlainTextSwitch = findViewById(R.id.switch_plain_text);
        mScreenShotSwitch = findViewById(R.id.switch_screen_shot);
        mBorderColorBar = findViewById(R.id.color_seek_bar);
        mBorderWidthBar = findViewById(R.id.border_width_seek_bar);
        mBorderCornerBar = findViewById(R.id.border_corner_seek_bar);
        mBorderColorOptions = findViewById(R.id.border_color_options);
        mTextGapBar = findViewById(R.id.gap_seek_bar);
        mTextLengthBar = findViewById(R.id.length_seek_bar);
        mSafeEditText = findViewById(R.id.pwd_view);
        mSafeEditText.setInputListener(new PasswordEditText.InputHandler() {

            @Override
            public void showKeyboard() {
                // 使用安全键盘
//                KeyboardWrapperApi.doInputWithPopupKeyboard(KeyboardDemoActivity.this, mSafeEditText, new
//                IInputCompleteListener() {
//                    @Override
//                    public void onComplete(byte[] context, String msg) {
//                        doCommit(context, msg);
//                    }
//                });
                KeyboardWrapperApi
                        .doInputWithPopupKeyboard(KeyboardDemoActivity.this, mSafeEditText, mSafeEditText.hashCode(),
                                new ICustomKeyboardWrapper() {
                                    @Override
                                    public void wrapper(CustomKeyboardWrapper wrapper) {
                                        //安全键盘属性设置
                                        String text7 = getResources().getString(R.string.custom_keyboard_activity_brand_name);

                                        wrapper.setBrandIcon(ContextCompat
                                                .getDrawable(KeyboardDemoActivity.this, R.drawable.brand_icon));
                                        wrapper.setBrandName(text7);
                                    }
                                }, new IInputCompleteListener() {
                                    @Override
                                    public void onComplete(byte[] context, String msg) {
                                        doCommit(context, msg);
                                    }
                                });
                setWindowSecure(mScreenShotSwitch.isChecked());
                // 使用系统键盘
//                KeyboardHelper.showKeyboard(mSafeEditText);
            }
        });

        initConfig();
        mBorderSwitch.setChecked(false);
        mBorderColorOptions.check(R.id.normal_border_color);
        mLogText = findViewById(R.id.log_text_view);
        mLogText.setMovementMethod(ScrollingMovementMethod.getInstance());
//        openHardwareAccelerated();
    }

    private void setWindowSecure(boolean isSecure) {
        if (isSecure) {
            if ((getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_SECURE) != 0) {
                Log.i("Keyboard", "flag already set secure");
                return;
            }
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        } else {
            if ((getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_SECURE) == 0) {
                Log.i("Keyboard", "flag already set unsecure");
                return;
            }
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        reset();
    }

    private void reset() {
        mSafeEditText.getTextRecorder().clear();
        mSafeEditText.setText("");
        clearLog();
        KeyboardWrapperApi.hideKeyboard();
    }

    private String getStringById(int id){
        return getResources().getString(id);
    }

    private void doCommit(final byte[] context, String msg) {
        KeyboardWrapperApi.hideKeyboard();
        final String inputText = TextUtils.isEmpty(msg) ? "" : msg;
        clearLog();
        String text = getResources().getString(R.string.keyboard_demo_activity_input_content,inputText);
        appendLog(text);
        TaskThread.getInstance().postToWorker(new Runnable() {
            @Override
            public void run() {
                testSM4ECB(inputText);
                testSM4CBC(inputText);
                testSM2(inputText);

                final IShark netHelper = SharkService.getSharkWithInit();
                ReqRecoverInputText req = new ReqRecoverInputText();
                req.inputText = inputText;
                req.context = context;
                netHelper.sendJceStruct(Consts.ECID_REQ_RECOVERED_INPUT_TEXT, req, new RespRecoverInputText(), 0,
                        new ISendJceCallback() {
                            @Override
                            public void onFinish(int seqNo, int cmdId, SharkRetCode retCode,
                                    JceStruct resp, SharkExtra extra) {
                                Log.i("Keyboard", "recover text " + retCode);
                                if (resp instanceof RespRecoverInputText) {
                                    String text7 = getResources().getString(R.string.keyboard_demo_activity_recover_content_success,((RespRecoverInputText) resp).recoveredText);

                                    appendLog(spanText(
                                            text7,
                                            Color.GREEN));
                                } else {
                                    String text8 = getResources().getString(R.string.keyboard_demo_activity_recover_content_failed,retCode.errorCode,retCode.errorDomain);

                                    appendLog(spanText(text8, Color.RED));
                                }
                            }
                        }, 60 * 1000);
            }
        });
    }

    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    private void testSM4ECB(String plainText) {
        if (TextUtils.isEmpty(plainText)) {
            return;
        }
        byte[] testKey = {1, 2, 3, 4, 5, 0, 9, 8, 7, 6, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16};
        SmBase.SmResult result = Sm4.sm4EncryptECB(testKey, plainText.getBytes(Charset.forName("UTF-8")));
        if (result.ret == 0) {
            byte[] encrypted = result.data;
            String encryptedHexText = EncodeUtil.bytesToHexString(encrypted);
            appendLog("-----------------------");
            appendLog(spanText(getStringById(R.string.keyboard_demo_activity_sm4_ecb_encrypt), Color.GREEN));
            String txt = getResources().getString(R.string.keyboard_demo_activity_sm4_ecb_encrypt_result_0,new String(encrypted, Charset.forName("UTF-8")));
            String txt2 = getResources().getString(R.string.keyboard_demo_activity_sm4_ecb_encrypt_result_1,encryptedHexText);
            appendLog(txt);
            appendLog(txt2);
            appendLog("-----------------------");
            result = Sm4.sm4DecryptECB(testKey, encrypted);
            if (result.ret == 0) {
                byte[] decrypted = result.data;
                String text = new String(decrypted, Charset.forName("UTF-8"));
                String txt3 = getResources().getString(R.string.keyboard_demo_activity_sm4_ecb_decrypt_success,text,text.equals(plainText));

                appendLog(spanText(txt3,
                        Color.GREEN));
            } else {
                String txt4 = getResources().getString(R.string.keyboard_demo_activity_sm4_ecb_encrypt_failed,getSmErrorText(result.ret));
                appendLog(spanText(txt4, Color.RED));
            }

        } else {
            String txt5 = getResources().getString(R.string.keyboard_demo_activity_sm4_ecb_encrypt_failed_0,getSmErrorText(result.ret));
            appendLog(spanText(txt5, Color.RED));
        }
    }

    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    private void testSM4CBC(String plainText) {
        if (TextUtils.isEmpty(plainText)) {
            return;
        }
        byte[] key = {2, 1, 3, 4, 5, 0, 9, 8, 7, 6, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16};
        byte[] iv = {0x12, 0x13, 0x14, 0x15, 2, 1, 3, 4, 5, 0, 9, 8, 7, 6, 0x11, 0x16};
        SmBase.SmResult result = Sm4.sm4EncryptCBC(key, iv, plainText.getBytes(Charset.forName("UTF-8")));
        if (result.ret == 0) {
            byte[] encrypted = result.data;
            String encryptedHexText = EncodeUtil.bytesToHexString(encrypted);
            appendLog("-----------------------");
            appendLog(spanText(getStringById(R.string.keyboard_demo_activity_sm4_cbc_encrypt), Color.YELLOW));
            String txt = getResources().getString(R.string.keyboard_demo_activity_sm4_cbc_encrypt_result_0,new String(encrypted, Charset.forName("UTF-8")));
            String txt2 = getResources().getString(R.string.keyboard_demo_activity_sm4_cbc_encrypt_result_1,encryptedHexText);
            appendLog(txt);
            appendLog(txt2);
            appendLog("-----------------------");
            result = Sm4.sm4DecryptCBC(key, iv, encrypted);
            if (result.ret == 0) {
                byte[] decrypted = result.data;
                String text = new String(decrypted, Charset.forName("UTF-8"));
                String txt3 = getResources().getString(R.string.keyboard_demo_activity_sm4_cbc_decrypt_success,text,text.equals(plainText));

                appendLog(spanText(txt3,
                        Color.YELLOW));
            } else {
                String txt4 = getResources().getString(R.string.keyboard_demo_activity_sm4_cbc_encrypt_failed,getSmErrorText(result.ret));
                appendLog(spanText(txt4, Color.RED));
            }

        } else {
            String txt5 = getResources().getString(R.string.keyboard_demo_activity_sm4_cbc_encrypt_failed_0,getSmErrorText(result.ret));
            appendLog(spanText(txt5, Color.RED));
        }
    }

    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    private void testSM2(String plainText) {
        if (TextUtils.isEmpty(plainText)) {
            return;
        }
        String priKey = "8A0CEF075216F8E150604B2D627CF973CA274420964B648C6F6F77580467AE6A";
        String pubKeyX = "0128BEA05C9F9D10B5B65C8E3A1AAC16CA4812758908D55DDE44979BA6ABB4EF";
        String pubKeyY = "1950D9A40735BDD6FF11F455C83B8B79F2B68EF91BC9FC446B8301D3DF7E5366";

        byte[] priKeyBytes = EncodeUtil.hexStringToBytes(priKey);
        byte[] pubKeyXBytes = EncodeUtil.hexStringToBytes(pubKeyX);
        byte[] pubKeyYBytes = EncodeUtil.hexStringToBytes(pubKeyY);
        try {
            SmBase.Sm2Result result = Sm2
                    .sm2Encrypt(pubKeyXBytes, pubKeyYBytes, plainText.getBytes(Charset.forName("UTF-8")));
            if (result.ret == 0) {
                byte[] c1 = result.c1;
                byte[] c3 = result.c3;
                byte[] c2 = result.c2;
                appendLog("-----------------------");
                appendLog(spanText(getStringById(R.string.keyboard_demo_activity_sm2_cbc_encrypt), Color.CYAN));
                String head = getStringById(R.string.keyboard_demo_activity_sm2_cbc_encrypt_head);
                appendLog(head+"，c1: len = " + c1.length + ", hex text: " + EncodeUtil.bytesToHexString(c1));
                appendLog(head+"，c2: len = " + c2.length + ", hex text: " + EncodeUtil.bytesToHexString(c2));
                appendLog(head+"，c3: len = " + c3.length + ", hex text: " + EncodeUtil.bytesToHexString(c3));
                appendLog("-----------------------");
                SmBase.SmResult result1 = Sm2.sm2Decrypt(c1, c3, c2, priKeyBytes);
                if (result1.ret == 0) {
                    byte[] decrypted = result1.data;
                    String text = new String(decrypted, Charset.forName("UTF-8"));
                    String txt3 = getResources().getString(R.string.keyboard_demo_activity_sm2_cbc_decrypt_success,text,text.equals(plainText));

                    appendLog(spanText(txt3,
                            Color.CYAN));
                } else {
                    String txt4 = getResources().getString(R.string.keyboard_demo_activity_sm2_cbc_encrypt_failed,getSmErrorText(result.ret));
                    appendLog(spanText(txt4, Color.RED));
                }

            } else {
                String txt5 = getResources().getString(R.string.keyboard_demo_activity_sm2_cbc_encrypt_failed_0,getSmErrorText(result.ret));
                appendLog(spanText(txt5, Color.RED));
            }
        } catch (Throwable t) {
            // ignore
        }
    }

    private void setBorderConfigState(boolean enable) {
        mBorderColorBar.setEnabled(enable);
        mBorderWidthBar.setEnabled(enable);
        mBorderCornerBar.setEnabled(enable);
        mBorderColorOptions.setEnabled(enable);
    }

    private void openHardwareAccelerated() {
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initConfig() {
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomSheet();
                    }
                });
//
//        mTopBar.addRightImageButton(R.drawable.setting, QMUIViewHelper.generateViewId()).setOnClickListener(new
//        View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(KeyboardDemoActivity.this, CustomKeyboardActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        mTopBar.addRightImageButton(R.drawable.delete, QMUIViewHelper.generateViewId())
//                .setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mSafeEditText.getTextRecorder().clear();
//                        mSafeEditText.setText("");
//                        clearLog();
//                    }
//                });

        mBorderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSafeEditText.setShowBorder(!isChecked);
                setBorderConfigState(!isChecked);
            }
        });

        mShowPlainTextSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(KeyboardDemoActivity.this,getStringById(R.string.keyboard_demo_activity_tips), Toast.LENGTH_LONG).show();
                    mSafeEditText.setShowPlainText();
                } else {
                    mSafeEditText.setHidePlainText();
                }
            }
        });

        mBorderColorOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.normal_border_color) {
                    mConfigBorderColorType = IConfigBorderColorType.NORMAL_STATE_BORDER_COLOR;
                } else if (checkedId == R.id.selected_border_color) {
                    showCannotChangeColorToast();
                    mConfigBorderColorType = IConfigBorderColorType.SELECTED_STATE_BORDER_COLOR;
                } else if (checkedId == R.id.normal_fill_color) {
                    showCannotChangeColorToast();
                    mConfigBorderColorType = IConfigBorderColorType.NORMAL_STATE_FILL_COLOR;
                } else {
                    showCannotChangeColorToast();
                    mConfigBorderColorType = IConfigBorderColorType.SELECTED_STATE_FILL_COLOR;
                }
            }
        });

        // 根据比例，在两个 color 值之间计算出一个 color 值
        final int fromNormalBorderColor = Color.BLACK;
        final int toNormalBorderColor = Color.CYAN;

        final int fromSelectedBorderColor = Color.TRANSPARENT;
        final int toSelectedBorderColor = Color.BLUE;

        final int fromNormalFillColor = Color.WHITE;
        final int toNormalFillColor = Color.GRAY;

        final int fromSelectedFillColor = Color.TRANSPARENT;
        final int toSelectedFillColor = Color.YELLOW;

        mBorderColorBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mConfigBorderColorType == IConfigBorderColorType.NORMAL_STATE_BORDER_COLOR) {
                    mBorderNormalStateColor = QMUIColorHelper
                            .computeColor(fromNormalBorderColor, toNormalBorderColor, (float) progress / 100);
                } else if (mConfigBorderColorType == IConfigBorderColorType.SELECTED_STATE_BORDER_COLOR) {
                    mBorderSelectedStateColor = QMUIColorHelper
                            .computeColor(fromSelectedBorderColor, toSelectedBorderColor, (float) progress / 100);
                } else if (mConfigBorderColorType == IConfigBorderColorType.NORMAL_STATE_FILL_COLOR) {
                    mNorFillColor = QMUIColorHelper
                            .computeColor(fromNormalFillColor, toNormalFillColor, (float) progress / 100);
                } else {
                    mSelectedFillColor = QMUIColorHelper
                            .computeColor(fromSelectedFillColor, toSelectedFillColor, (float) progress / 100);
                }
                mSafeEditText.configBorderStyle(mBorderNormalStateColor, mNorFillColor, mBorderSelectedStateColor,
                        mSelectedFillColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBorderColorBar.setProgress(50);

        final int endGap = Tools.dip2px(this, 10);
        mTextGapBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTextGap = endGap * progress / 100;
                mSafeEditText.configTextLayout(-1, mTextGap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final float fromWidth = 1.0f;
        final float toWidth = 10.0f;

        final float fromCorner = Tools.dip2px(this, 6);
        final float toCorner = Tools.dip2px(this, 10);

        mBorderWidth = fromWidth;
        mBorderCorner = fromCorner;

        mBorderWidthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBorderWidth = toWidth * progress / 100;
                mSafeEditText.configBorderLayout(mBorderWidth, mBorderCorner);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBorderWidthBar.setProgress(0);

        mBorderCornerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBorderCorner = toCorner * progress / 100;
                mSafeEditText.configBorderLayout(mBorderWidth, mBorderCorner);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBorderCornerBar.setProgress((int) (fromCorner * 100 / toCorner));

        final int fromLength = 6;
        final int toLength = 12;
        mTextLengthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTextLength = fromLength + (toLength - fromLength) * progress / 100;
                mSafeEditText.setMaxTextLength(mTextLength);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mScreenShotSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setWindowSecure(isChecked);
            }
        });
    }

    private void showCannotChangeColorToast() {
        if (mTextGap <= 0) {
            Toast.makeText(this, getStringById(R.string.keyboard_demo_activity_tips_not_change_color), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.keyboard_demo, null);
    }

    //添加日志
    private void addText(CharSequence content) {
        mLogText.append(content);
        mLogText.append("\n");
        int offset = mLogText.getLineCount() * mLogText.getLineHeight();
        if (offset > mLogText.getHeight()) {
            mLogText.scrollTo(0, offset - mLogText.getHeight());
        }
    }

    // 生成彩色日志内容
    private CharSequence spanText(String text, int color) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(text);
        // 设置颜色
        spannableString.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    // 添加显示日志
    private void appendLog(final CharSequence content) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addText(content);
                }
            });
        } else {
            addText(content);
        }
    }

    //清除日志
    private void clearLog() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    clearText();
                }
            });
        } else {
            clearText();
        }
    }

    //清空日志
    private void clearText() {
        mLogText.setText("");
    }

    private String getSmErrorText(int ret) {
        switch (ret) {
            case 1: {
                return getStringById(R.string.keyboard_demo_activity_sm_error_1);
            }
            case 2: {
                return getStringById(R.string.keyboard_demo_activity_sm_error_2);
            }
            case 3: {
                return getStringById(R.string.keyboard_demo_activity_sm_error_3);
            }
            default:
                break;
        }
        return getStringById(R.string.keyboard_demo_activity_sm_error_4);
    }

    private void showBottomSheet() {
        new QMUIBottomSheet.BottomListSheetBuilder(this)
                .addItem(getStringById(R.string.keyboard_demo_activity_item_1))
                .addItem(getStringById(R.string.keyboard_demo_activity_item_2))
                .addItem(getStringById(R.string.keyboard_demo_activity_item_3))
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        switch (position) {
                            case 0:
                                reset();
                                break;
                            case 1:
                                startActivity(new Intent(KeyboardDemoActivity.this, CustomKeyboardActivity.class));
                                break;
                            case 2:
                                startActivity(new Intent(KeyboardDemoActivity.this, PasswordCheckActivity.class));
                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .build().show();
    }
}
