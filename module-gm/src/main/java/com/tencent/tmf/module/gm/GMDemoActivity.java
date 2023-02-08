package com.tencent.tmf.module.gm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.gm.Sm2;
import com.tencent.tmf.gm.Sm3;
import com.tencent.tmf.gm.Sm3Hmac;
import com.tencent.tmf.gm.Sm4;
import com.tencent.tmf.gm.SmBase;
import com.tencent.tmf.gm.SmCryptor;
import com.tencent.tmf.module.gm.utils.EncodeUtil;
import com.tencent.tmf.portal.Launcher;
import com.tencent.tmf.portal.annotations.Destination;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 国密算法加解密示例
 */
@Destination(
        url = "portal://com.tencent.tmf.module.gm/gm-activity",
        launcher = Launcher.ACTIVITY,
        description = "国密算法"
)
public class GMDemoActivity extends TopBarActivity {

    private static final String TAG = "GMDemoActivity";

    private String plainText;
    private EditText etPlainText;
    private TextView tvMsg;

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_gm, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String gm = getResources().getString(R.string.gm_demo_title);
        mTopBar.setTitle(gm);
        etPlainText = (EditText) findViewById(R.id.et_plainText);
        tvMsg = (TextView) findViewById(R.id.tv_msg);

        QMUIRoundButton btnRun = (QMUIRoundButton) findViewById(R.id.btn_run);
        btnRun.setOnClickListener(v -> new Thread(() -> {
            println("\n");

            plainText = etPlainText.getText().toString();
            testSM2();
            testSM2_2();
            testSM4Ecb();
            testSM4Cbc();
        }).start());

        QMUIRoundButton btnCleanMsg = (QMUIRoundButton) findViewById(R.id.btn_cleanmsg);
        btnCleanMsg.setOnClickListener(v -> clearMsg());

        QMUIRoundButton btnSign = (QMUIRoundButton) findViewById(R.id.btn_sm2sign);
        btnSign.setOnClickListener(v -> new Thread(() -> {
            println("\n");
            plainText = etPlainText.getText().toString();
            testSm2Sign();
        }).start());

        QMUIRoundButton btnSm3 = (QMUIRoundButton) findViewById(R.id.btn_sm3hash);
        btnSm3.setOnClickListener(v -> new Thread(() -> {
            println("\n");
            plainText = etPlainText.getText().toString();
            testSm3();
            testSm3Hmac();
        }).start());

        QMUIRoundButton btnGMSSL = (QMUIRoundButton) findViewById(R.id.btn_tsmssl);

        btnGMSSL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        testTsmSSL();
                    }
                }).start();
            }
        });
    }


    private void testSM2() {
//        String plainText = "Hello world! 加密后C1、C2、C3分别输出。";
        String priKey = "8A0CEF075216F8E150604B2D627CF973CA274420964B648C6F6F77580467AE6A";
        String pubKeyX = "0128BEA05C9F9D10B5B65C8E3A1AAC16CA4812758908D55DDE44979BA6ABB4EF";
        String pubKeyY = "1950D9A40735BDD6FF11F455C83B8B79F2B68EF91BC9FC446B8301D3DF7E5366";

        byte[] priKeyBytes = EncodeUtil.hexStringToBytes(priKey);
        byte[] pubKeyXBytes = EncodeUtil.hexStringToBytes(pubKeyX);
        byte[] pubKeyYBytes = EncodeUtil.hexStringToBytes(pubKeyY);

        byte[] rawData = plainText.getBytes(StandardCharsets.UTF_8);
        String head = getResources().getString(R.string.gm_ssl_demo_seprate_out);
        println(head);
        String sm2EncryptIn = getResources().getString(R.string.gm_demo_sm2_encrypt_in);
        String sm2EncryptOut = getResources().getString(R.string.gm_demo_sm2_encrypt_out);
        println(sm2EncryptIn+" rawData: len = " + rawData.length + ", hex: " + bytesToHexString(rawData));
        SmBase.Sm2Result result = Sm2.sm2Encrypt(pubKeyXBytes, pubKeyYBytes, rawData);
        if (result.ret == 0) {
            byte[] c1 = result.c1;
            byte[] c3 = result.c3;
            byte[] c2 = result.c2;
            println(sm2EncryptOut+" c1: len = " + c1.length + ", hex: " + bytesToHexString(c1));
            println(sm2EncryptOut+" c3: len = " + c3.length + ", hex: " + bytesToHexString(c3));
            println(sm2EncryptOut+" c2: len = " + c2.length + ", hex: " + bytesToHexString(c2));

            SmBase.SmResult result1 = Sm2.sm2Decrypt(c1, c3, c2, priKeyBytes);
            String sm2DecryptResult = getResources().getString(R.string.gm_demo_sm2_encrypt_result);
            if (result1.ret == 0) {
                byte[] decrypted = result1.data;
                String sm2DecryptOut = getResources().getString(R.string.gm_demo_sm2_derypt_out);
                String text = new String(decrypted, StandardCharsets.UTF_8);
                println(sm2DecryptOut+" decrypted: len = " + decrypted.length + ", hex: " + bytesToHexString(decrypted));
                println(sm2DecryptOut+" decrypted text: " + text);
                println(">>"+sm2DecryptResult + (plainText.equals(text) ? "succ" : "failed"));
            } else {
                println(">>"+sm2DecryptResult+" decrypt failed, result1.ret: " + result1.ret);
            }
        } else {
            String sm2DecryptResult = getResources().getString(R.string.gm_demo_sm2_encrypt_result);
            println(">>"+sm2DecryptResult+" encrypt failed, result.ret: " + result.ret);
        }

    }

    private void testSM2_2() {
        String priKey = "8A0CEF075216F8E150604B2D627CF973CA274420964B648C6F6F77580467AE6A";
        String pubKey = "0128BEA05C9F9D10B5B65C8E3A1AAC16CA4812758908D55DDE44979BA6ABB4EF"
                + "1950D9A40735BDD6FF11F455C83B8B79F2B68EF91BC9FC446B8301D3DF7E5366";

        byte[] priKeyBytes = EncodeUtil.hexStringToBytes(priKey);
        byte[] pubKeyBytes = EncodeUtil.hexStringToBytes(pubKey);

        byte[] rawData = plainText.getBytes(StandardCharsets.UTF_8);
        String sm2EncryptIn = getResources().getString(R.string.gm_demo_sm2_encrypt_in);
        String sm2EncryptOut = getResources().getString(R.string.gm_demo_sm2_encrypt_out);
        String sm2DecryptOut = getResources().getString(R.string.gm_demo_sm2_derypt_out);
        String sm2EncryptResult= getResources().getString(R.string.gm_demo_sm2_encrypt_result);
        String merge = getResources().getString(R.string.gm_ssl_demo_merge);

        println(merge);
        println(sm2EncryptIn+" raw: len = " + rawData.length + ", hex: " + bytesToHexString(rawData));
        byte[] encrypted = SmCryptor.sm2Encrypt(rawData, pubKeyBytes);
        if (encrypted != null && encrypted.length != 0) {
            println(sm2EncryptOut+" encrypted: len = " + encrypted.length + ", hex: " + bytesToHexString(encrypted));
            byte[] decrypted = SmCryptor.sm2Decrypt(encrypted, priKeyBytes);
            if (decrypted != null && decrypted.length != 0) {
                String text = new String(decrypted, StandardCharsets.UTF_8);
                println(sm2DecryptOut+" decrypted: len = " + decrypted.length + ", hex: " + bytesToHexString(decrypted));
                println(sm2DecryptOut+" decrypted text: " + text);
                println(">>" +sm2EncryptResult+ (plainText.equals(text) ? "succ" : "failed"));
            } else {
                println(">>"+sm2EncryptResult+" decrypt failed");
            }
        } else {
            println(">>"+sm2EncryptResult+" encrypt failed");
        }
    }

    private void testSM4Ecb() {
//        String plainText = "SM4 ECB encrypt and decrypt 1234567890, 加中文内容";
        byte[] key = {1, 2, 3, 4, 5, 0, 9, 8, 7, 6, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16};

        try {
            String sm4EncryptIn = getResources().getString(R.string.gm_demo_sm4_encrypt_in);
            String sm4EncryptOut = getResources().getString(R.string.gm_demo_sm4_encrypt_out);
            String sm4DecryptOut = getResources().getString(R.string.gm_demo_sm4_derypt_out);
            String sm4EncryptResult= getResources().getString(R.string.gm_demo_sm4_encrypt_result);
            byte[] rawData = plainText.getBytes(StandardCharsets.UTF_8);
            println("\n--------SM4(ECB)--------");
            println(sm4EncryptIn+"rawData: len = " + rawData.length + ", hex: " + bytesToHexString(rawData));

            SmBase.SmResult result = Sm4.sm4EncryptECB(key, rawData);
            if (result.ret == 0) {
                byte[] encrypted = result.data;
                println(sm4EncryptOut+"encrypted: len = " + encrypted.length + ", hex: " + bytesToHexString(encrypted));

                result = Sm4.sm4DecryptECB(key, encrypted);
                if (result.ret == 0) {
                    byte[] decrypted = result.data;
                    String text = new String(decrypted, StandardCharsets.UTF_8);
                    println(sm4DecryptOut+" decrypted: len = " + decrypted.length + ", hex: " + bytesToHexString(decrypted));
                    println(sm4DecryptOut+" decrypted text: " + text);
                    println(">>"+sm4EncryptResult + (plainText.equals(text) ? "succ" : "failed"));
                } else {
                    println(">>"+sm4EncryptResult+" decrypt failed, result.ret: " + result.ret + " output: " + Arrays.toString(
                            result.data));
                }
            } else {
                println(">>"+sm4EncryptResult+" encrypt failed, result.ret: " + result.ret + " output: " + Arrays.toString(
                        result.data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testSM4Cbc() {
//        String plainText = "SM4 CBC encrypt and decrypt, 1234abcd, 加中文内容";
        byte[] key = {2, 1, 3, 4, 5, 0, 9, 8, 7, 6, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16};
        byte[] iv = {0x12, 0x13, 0x14, 0x15, 2, 1, 3, 4, 5, 0, 9, 8, 7, 6, 0x11, 0x16};

        try {
            String sm4EncryptIn = getResources().getString(R.string.gm_demo_sm4_encrypt_in);
            String sm4EncryptOut = getResources().getString(R.string.gm_demo_sm4_encrypt_out);
            String sm4DecryptOut = getResources().getString(R.string.gm_demo_sm4_derypt_out);
            String sm4EncryptResult= getResources().getString(R.string.gm_demo_sm4_encrypt_result);
            byte[] rawData = plainText.getBytes(StandardCharsets.UTF_8);
            println("\n--------SM4(CBC)--------");
            println(sm4EncryptIn+" rawData: len = " + rawData.length + ", hex: " + bytesToHexString(rawData));

            SmBase.SmResult result = Sm4.sm4EncryptCBC(key, iv, rawData);
            if (result.ret == 0) {
                byte[] encrypted = result.data;
                println(sm4EncryptOut+" encrypted: len = " + encrypted.length + ", hex: " + bytesToHexString(encrypted));

                result = Sm4.sm4DecryptCBC(key, iv, encrypted);
                if (result.ret == 0) {
                    byte[] decrypted = result.data;
                    String text = new String(decrypted, StandardCharsets.UTF_8);
                    println(sm4DecryptOut+" decrypted: len = " + decrypted.length + ", hex: " + bytesToHexString(decrypted));
                    println(sm4DecryptOut+" decrypted text: " + text);
                    println(">>"+sm4EncryptResult + (plainText.equals(text) ? "succ" : "failed"));
                } else {
                    println(">>"+sm4EncryptResult+" decrypt failed, result.ret: " + result.ret + " output: " + Arrays.toString(
                            result.data));
                }
            } else {
                println(">>"+sm4EncryptResult+" encrypt failed, result.ret: " + result.ret + " output: " + Arrays.toString(
                        result.data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testSm2Sign() {
        String priKey = "8A0CEF075216F8E150604B2D627CF973CA274420964B648C6F6F77580467AE6A";
        String pubKeyX = "0128BEA05C9F9D10B5B65C8E3A1AAC16CA4812758908D55DDE44979BA6ABB4EF";
        String pubKeyY = "1950D9A40735BDD6FF11F455C83B8B79F2B68EF91BC9FC446B8301D3DF7E5366";

        byte[] priKeyBytes = EncodeUtil.hexStringToBytes(priKey);
        byte[] pubKeyXBytes = EncodeUtil.hexStringToBytes(pubKeyX);
        byte[] pubKeyYBytes = EncodeUtil.hexStringToBytes(pubKeyY);
        String sm2SigntIn = getResources().getString(R.string.gm_demo_sm2_sign_in);
        String sm2SignOut = getResources().getString(R.string.gm_demo_sm2_sign_out);
        String sm2SignCheckout = getResources().getString(R.string.gm_demo_sm2_sign_check_out);
        String sm2SignResult= getResources().getString(R.string.gm_demo_sm2_sign_check_result);
        byte[] rawData = plainText.getBytes(StandardCharsets.UTF_8);
        println(sm2SigntIn+" raw: len = " + rawData.length + ", hex: " + bytesToHexString(rawData));
        SmBase.SmResult result = Sm2.sm2Sign(rawData, "1234567890".getBytes(), pubKeyXBytes, pubKeyYBytes,
                priKeyBytes);
        if (result.ret == 0) {
            byte[] encrypted = result.data;
            println(sm2SignOut+" sign: len = " + encrypted.length + ", hex: " + bytesToHexString(encrypted));
            println("id = " + bytesToHexString("1234567890".getBytes()));
            int ret = Sm2.sm2Verify(rawData, "1234567890".getBytes(), pubKeyXBytes, pubKeyYBytes, encrypted);
            if (ret == 0) {
                println(">>"+sm2SignCheckout+" succ");
            } else {
                println(">>"+sm2SignCheckout+" failed, ret: " + ret);
            }
        } else {
            println(">>"+sm2SignResult+" sign failed, result.ret: " + result.ret);
        }
    }

    private void testSm3() {
        byte[] rawData = plainText.getBytes(StandardCharsets.UTF_8);
        String sm2SigntIn = getResources().getString(R.string.gm_demo_sm3_encrypt_in);
        String sm2SignOut = getResources().getString(R.string.gm_demo_sm3_encrypt_out);
        String sm2SignCheckout = getResources().getString(R.string.gm_demo_sm3_derypt_out);
        println(sm2SigntIn+" raw: len = " + rawData.length + ", hex: " + bytesToHexString(rawData));
        Sm3 sm3 = new Sm3();
        sm3.update(rawData);
        byte[] result = sm3.digest();
        if (result != null && result.length == 32) {
            println(sm2SignOut+" sm3: len = " + result.length + ", hex: " + bytesToHexString(result));
        } else {
            println(">>"+sm2SignCheckout+" sm3 failed");
        }
    }

    private void testSm3Hmac() {
        byte[] rawData = plainText.getBytes(StandardCharsets.UTF_8);
        String sm2SigntIn = getResources().getString(R.string.gm_demo_sm3_hmac_encrypt_in);
        String sm2SignOut = getResources().getString(R.string.gm_demo_sm3_hmac_encrypt_out);
        String sm2SignCheckout = getResources().getString(R.string.gm_demo_sm3_hmac_derypt_out);
        println(sm2SigntIn+" raw: len = " + rawData.length + ", hex: " + bytesToHexString(rawData));
        Sm3Hmac sm3 = new Sm3Hmac("123456789".getBytes());
        sm3.update(rawData);
        byte[] result = sm3.digest();
        if (result != null && result.length == 32) {
            println(sm2SignOut+" sm3Hmac: len = " + result.length + ", hex: " + bytesToHexString(result));
        } else {
            println(">>"+sm2SignCheckout+" sm3Hmac failed");
        }
    }

    private static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        String sTemp;
        for (byte b : bArray) {
            sTemp = Integer.toHexString(0xFF & b);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    private final StringBuilder sb = new StringBuilder();

    private void clearMsg() {
        runOnUiThread(() -> {
            sb.setLength(0);
            tvMsg.setText(sb.toString());
        });
    }

    private void println(final String msg) {
        Log.i(TAG, msg);
        runOnUiThread(() -> {
            sb.append("\n").append(msg);
            tvMsg.setText(sb.toString());
        });
    }

    private void testTsmSSL() {
        startActivity(new Intent(this, GMSSLDemoActivity.class));
    }
}
