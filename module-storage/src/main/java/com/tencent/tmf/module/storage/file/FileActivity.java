package com.tencent.tmf.module.storage.file;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.tmf.cipher.api.TMFCipher;
import com.tencent.tmf.module.storage.R;
import com.tencent.tmf.module.storage.StorageActivity;
import com.tencent.tmf.storage.FileUtils;
import com.tencent.tmf.storage.TMFFile;
import com.tencent.tmf.storage.TMFStorage;

import java.io.File;

public class FileActivity extends Activity implements View.OnClickListener {

    private byte[] mKey;
    private File mStorageFile;
    private File mStorageFileEncrypted;
    private String mUserInput;
    private byte[] mFileOutput;
    private boolean mFileStorageResult = false;
    private EditText mInputEdt;
    private TextView mOutputTv;
    private TextView mDecryptOutputTv;
    private TMFFile mFile;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        //动态设置密钥，使用全局默认加密算法获取默认密钥
        mKey = TMFCipher.getDefaultKey(TMFCipher.getsDefaultAlgorithmType());

        String storageType = getIntent().getStringExtra(StorageActivity.KEY_STORAGE_TYPE);
        String storageUID = getIntent().getStringExtra(StorageActivity.KEY_STORAGE_UID);
        if (StorageActivity.STORAGE_TYPE_TEMP.equals(storageType)) {
            mFile = TMFStorage.getTemporary().file();
            ;
        } else if (StorageActivity.STORAGE_TYPE_USER.equals(storageType) && !TextUtils.isEmpty(storageUID)) {
            mFile = TMFStorage.getByUser(storageUID).file();
        } else {
            mFile = TMFStorage.getDefault().file();
        }
        if (mFile == null) {
            Toast.makeText(this, getStringById(R.string.module_storage_17), Toast.LENGTH_LONG).show();
            finish();
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(PERMISSIONS_STORAGE, 0);
            }
        }
        mStorageFile = mFile.getInternalFile("file_storage_demo");
        mStorageFileEncrypted = mFile.getExternalFile("file_storage_encrypt_demo");
        ((TextView) findViewById(R.id.tv_file_path)).setText(getStringById(R.string.module_storage_18) + mStorageFile);
        ((TextView) findViewById(R.id.tv_file_encrypted_path)).setText(getStringById(R.string.module_storage_19) + mStorageFileEncrypted);

        mInputEdt = findViewById(R.id.edt_input);
        findViewById(R.id.btn_file_write).setOnClickListener(this);
        findViewById(R.id.btn_file_read).setOnClickListener(this);
        findViewById(R.id.btn_file_write_encrypt).setOnClickListener(this);
        findViewById(R.id.btn_file_read_decrypt).setOnClickListener(this);
        findViewById(R.id.btn_file_encrypt).setOnClickListener(this);
        findViewById(R.id.btn_file_decrypt).setOnClickListener(this);
        findViewById(R.id.btn_file_delete).setOnClickListener(this);
        mOutputTv = findViewById(R.id.tv_output);
        mDecryptOutputTv = findViewById(R.id.tv_output_decrypt);
    }

    @Override
    public void onClick(View v) {
        mOutputTv.setText("");
        mDecryptOutputTv.setText("");
        int id = v.getId();
        if (id == R.id.btn_file_write) {
            mUserInput = mInputEdt.getText().toString();
            if (TextUtils.isEmpty(mUserInput)) {
                Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_20), Toast.LENGTH_SHORT).show();
            } else {
                mFileStorageResult = FileUtils.writeFile(mInputEdt.getText().toString().getBytes(), mStorageFile);
                if (mFileStorageResult) {
                    Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_21), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_22), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (id == R.id.btn_file_read) {
            mFileOutput = FileUtils.readFile(mStorageFile);
            if (null != mFileOutput) {
                mOutputTv.setText(new String(mFileOutput));
                Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_23), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_24), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.btn_file_write_encrypt) {
            mUserInput = mInputEdt.getText().toString();
            if (TextUtils.isEmpty(mUserInput)) {
                Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_20), Toast.LENGTH_SHORT).show();
            } else {
                mFileStorageResult = FileUtils.writeFileEncrypted(mInputEdt.getText().toString().getBytes(),
                        mStorageFileEncrypted, mKey, TMFCipher.getsDefaultAlgorithmType());
                if (mFileStorageResult) {
                    Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_25), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_26), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (id == R.id.btn_file_read_decrypt) {
            mFileOutput = FileUtils.readFile(mStorageFileEncrypted);
            if (null != mFileOutput) {
                mOutputTv.setText(getStringById(R.string.module_storage_27) + new String(mFileOutput));
            }
            mFileOutput = FileUtils.readFileEncrypted(mStorageFileEncrypted, mKey,
                    TMFCipher.getsDefaultAlgorithmType());
            if (null != mFileOutput) {
                mDecryptOutputTv.setText(getStringById(R.string.module_storage_28) + new String(mFileOutput));
                Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_29), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_30), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.btn_file_encrypt) {
            mFileStorageResult = FileUtils.encryptFile(mStorageFile, mStorageFileEncrypted,
                    mKey, TMFCipher.getsDefaultAlgorithmType());
            if (mFileStorageResult) {
                Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_31), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_32), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.btn_file_decrypt) {
            mFileStorageResult = FileUtils.decryptFile(mStorageFileEncrypted, mStorageFile,
                    mKey, TMFCipher.getsDefaultAlgorithmType());
            if (mFileStorageResult) {
                Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_33), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_34), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.btn_file_delete) {
            mFileStorageResult = FileUtils.removeFileOrDirectory(mStorageFile);
            if (mFileStorageResult) {
                Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_35), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getStringById(R.string.module_storage_36), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String getStringById(int id){
        return getResources().getString(id);
    }
}
