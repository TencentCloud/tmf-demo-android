package com.tencent.tmf.module.keyboard;

import Protocol.Keyboard.ReqRecoverInputText;
import Protocol.Keyboard.RespRecoverInputText;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.qq.taf.jce.JceStruct;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.common.keyboard.Consts;
import com.tencent.tmf.common.shark.SharkService;
import com.tencent.tmf.common.utils.ToastUtil;
import com.tencent.tmf.gm.Sm2;
import com.tencent.tmf.gm.Sm4;
import com.tencent.tmf.gm.SmBase;
import com.tencent.tmf.keyboard.api.ICustomKeyboardWrapper;
import com.tencent.tmf.keyboard.api.IInputCompleteListener;
import com.tencent.tmf.keyboard.api.InputTextCheckCallBack;
import com.tencent.tmf.keyboard.api.KeyboardConfigApi;
import com.tencent.tmf.keyboard.api.KeyboardWrapperApi;
import com.tencent.tmf.keyboard.common.KeyboardConfigMode;
import com.tencent.tmf.keyboard.component.keyboard.CustomKeyboardWrapper;
import com.tencent.tmf.keyboard.component.text.PasswordEditText;
import com.tencent.tmf.keyboard.config.DefaultKeyboardLayoutProvider;
import com.tencent.tmf.keyboard.task.TaskThread;
import com.tencent.tmf.keyboard.utils.EncodeUtil;
import com.tencent.tmf.keyboard.utils.KeyboardHelper;
import com.tencent.tmf.shark.api.ESharkCode;
import com.tencent.tmf.shark.api.ISendJceCallback;
import com.tencent.tmf.shark.api.IShark;
import com.tencent.tmf.shark.api.ISharkCallBack;
import com.tencent.tmf.shark.api.Shark;
import com.tencent.tmf.shark.api.SharkExtra;
import com.tencent.tmf.shark.api.SharkRetCode;
import java.nio.charset.Charset;

/**
 * 密码检查演示
 */
public class PasswordCheckActivity extends TopBarActivity {

    private PasswordEditText mSafeEditText = null;
    private PasswordEditText mSafeEditText2 = null;
    private EditText mRepeatNumET = null;
    private EditText mCheckStrengthET = null;
    // 显示日志组件
    private TextView mLogText;
    private byte[] firstInputContext;
    private byte[] secondInputContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // title和右上角按钮
        mTopBar.setTitle(getStringById(R.string.pass_check_activity_title));

        mSafeEditText = findViewById(R.id.pwd_view);
        mSafeEditText2 = findViewById(R.id.pwd_view2);
        mCheckStrengthET = findViewById(R.id.checkStrength);
        mRepeatNumET = findViewById(R.id.repeatNum);
        mSafeEditText2 = findViewById(R.id.pwd_view2);
        mSafeEditText.setShowPlainText();
        mSafeEditText2.setShowPlainText();

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
                KeyboardWrapperApi.doInputWithPopupKeyboard(PasswordCheckActivity.this, mSafeEditText,
                        PasswordCheckActivity.this.hashCode(), new ICustomKeyboardWrapper() {
                            @Override
                            public void wrapper(CustomKeyboardWrapper wrapper) {
                                wrapper.setBrandIcon(
                                        ContextCompat.getDrawable(PasswordCheckActivity.this, R.drawable.brand_icon));
                                wrapper.setBrandName(getStringById(R.string.custom_keyboard_activity_brand_name));
                            }
                        }, new IInputCompleteListener() {
                            @Override
                            public void onComplete(byte[] context, String msg) {
                                firstInputContext = context;
                                doCommit(context, msg);
                            }
                        });
                // 使用系统键盘
                //  KeyboardHelper.showKeyboard(mSafeEditText);
            }
        });

        mSafeEditText2.setInputListener(new PasswordEditText.InputHandler() {

            @Override
            public void showKeyboard() {
                // 使用安全键盘
//                KeyboardWrapperApi.doInputWithPopupKeyboard(KeyboardDemoActivity.this, mSafeEditText2, new
//                IInputCompleteListener() {
//                    @Override
//                    public void onComplete(byte[] context, String msg) {
//                        doCommit(context, msg);
//                    }
//                });
                KeyboardWrapperApi.doInputWithPopupKeyboard(PasswordCheckActivity.this, mSafeEditText2,
                        PasswordCheckActivity.this.hashCode(), new ICustomKeyboardWrapper() {
                            @Override
                            public void wrapper(CustomKeyboardWrapper wrapper) {
                                wrapper.setBrandIcon(
                                        ContextCompat.getDrawable(PasswordCheckActivity.this, R.drawable.brand_icon));
                                wrapper.setBrandName(getStringById(R.string.custom_keyboard_activity_brand_name));
                            }
                        }, new IInputCompleteListener() {
                            @Override
                            public void onComplete(byte[] context, String msg) {
                                secondInputContext = context;
                                doCommit(context, msg);
                            }
                        });
                // 使用系统键盘
                //  KeyboardHelper.showKeyboard(mSafeEditText2);
            }
        });

        initSharkAndKeyboardProxy();

        mLogText = findViewById(R.id.log_text_view);
        mLogText.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }

    private void initSharkAndKeyboardProxy() {
        // Shark
        Shark.setAppContext(getApplicationContext());
        SharkService.getSharkWithInit();
        // 初始化安全键盘配置
        KeyboardConfigApi.init(getApplicationContext());
        // 设置键位显示与键值映射数据从云端拉取
        KeyboardConfigApi.getInstance().setKeyboardDataConfigMode(KeyboardConfigMode.CONFIG_MODE_ONLINE);
        // 设置键位显示与键值映射关系数据网络拉取接口的实现, 已内置默认实现
//        KeyboardConfigApi.getInstance().setFetchRemoteConfigDataNetHelper(new GetKeyPositionImpl());
        // 设置为自定义键位布局
        KeyboardConfigApi.getInstance().customKeyboardLayoutProvider(new DefaultKeyboardLayoutProvider());
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
                            public void onFinish(int seqNo, int cmdId, SharkRetCode retCode, JceStruct resp, SharkExtra extra) {
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
                appendLog(spanText(txt4, Color.RED));            }

        } else {
            String txt5 = getResources().getString(R.string.keyboard_demo_activity_sm4_ecb_encrypt_failed_0,getSmErrorText(result.ret));
            appendLog(spanText(txt5, Color.RED));        }
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
                appendLog(spanText(txt4, Color.RED));            }

        } else {
            String txt5 = getResources().getString(R.string.keyboard_demo_activity_sm4_cbc_encrypt_failed_0,getSmErrorText(result.ret));
            appendLog(spanText(txt5, Color.RED));        }
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
                    appendLog(spanText(txt4, Color.RED));                }

            } else {
                String txt5 = getResources().getString(R.string.keyboard_demo_activity_sm2_cbc_encrypt_failed_0,getSmErrorText(result.ret));
                appendLog(spanText(txt5, Color.RED));            }
        } catch (Throwable t) {
            // ignore
        }
    }

    public void weakCheck(View view) {
        if (firstInputContext == null) {
            return;
        }

        String repeatNumStr = mRepeatNumET.getText().toString().trim();
        int repeatNum = Integer.parseInt(TextUtils.isEmpty(repeatNumStr) ? "0" : repeatNumStr);

        String checkStrengthStr = mCheckStrengthET.getText().toString().trim();
        int checkStrength = Integer.parseInt(TextUtils.isEmpty(checkStrengthStr) ? "0" : checkStrengthStr);

        String input = mSafeEditText.getText().toString().trim();

        if (TextUtils.isEmpty(input)) {
            return;
        }

        KeyboardHelper
                .inputTextWeakCheck(firstInputContext, input, repeatNum, checkStrength,
                        new InputTextCheckCallBack() {
                            @Override
                            public void result(int retCode, boolean isWeak) {
                                if (retCode == 0 && !isWeak) {
                                    ToastUtil.showToast(getStringById(R.string.pass_check_activity_pass));
                                } else {
                                    ToastUtil.showToast(getStringById(R.string.pass_check_activity_not_pass) + retCode + " isWeak: " + isWeak);
                                }
                            }
                        });
    }

    public void doubleCheck(View view) {
        if (firstInputContext == null || secondInputContext == null) {
            return;
        }

        String repeatNumStr = mRepeatNumET.getText().toString().trim();
        int repeatNum = Integer.parseInt(TextUtils.isEmpty(repeatNumStr) ? "0" : repeatNumStr);

        String checkStrengthStr = mCheckStrengthET.getText().toString().trim();
        int checkStrength = Integer.parseInt(TextUtils.isEmpty(checkStrengthStr) ? "0" : checkStrengthStr);

        String input = mSafeEditText.getText().toString().trim();
        String input2 = mSafeEditText2.getText().toString().trim();
        if (TextUtils.isEmpty(input) || TextUtils.isEmpty(input2)) {
            return;
        }

        /*
         * 二次密码校验 包含 密码强度检测功能
         */
        KeyboardHelper
                .inputTextDoubleCheck(firstInputContext, secondInputContext, input,
                        input2, repeatNum, checkStrength, new InputTextCheckCallBack() {
                            @Override
                            public void result(int retCode, boolean isWeak) {
                                if (retCode == 0 && !isWeak) {
                                    ToastUtil.showToast(getStringById(R.string.pass_check_activity_pass));
                                } else {
                                    ToastUtil.showToast(getStringById(R.string.pass_check_activity_not_pass) + retCode + " isWeak: " + isWeak);
                                }
                            }
                        });
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.password_check, null);
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
}
