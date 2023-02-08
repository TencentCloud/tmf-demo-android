package com.tencent.tmf.module.storage.db.cruddb;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tencent.tmf.common.activity.TopBarActivity;
import com.tencent.tmf.module.storage.R;
import com.tencent.tmf.module.storage.StorageActivity;
import com.tencent.tmf.storage.TMFDatabase;
import com.tencent.tmf.storage.TMFStorage;

import java.util.List;

public class CRUDActivity extends TopBarActivity implements View.OnClickListener {

    private static final int TYPE_INSERT = 1;
    private static final int TYPE_SELECT = 2;
    private static final int TYPE_DELETE = 3;
    private static final int TYPE_UPDATE = 4;
    private int operationType = TYPE_INSERT;
    private String name;
    private String address;
    private PersonDBManager personDBManager;
    private EditText etName;
    private EditText etAddress;
    private TextView tvResult;
    private QMUIRoundButton btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TMFDatabase database;
        String storageType = getIntent().getStringExtra(StorageActivity.KEY_STORAGE_TYPE);
        String storageUID = getIntent().getStringExtra(StorageActivity.KEY_STORAGE_UID);
        if (StorageActivity.STORAGE_TYPE_TEMP.equals(storageType)) {
            database = TMFStorage.getTemporary().database();
            ;
        } else if (StorageActivity.STORAGE_TYPE_USER.equals(storageType) && !TextUtils.isEmpty(storageUID)) {
            database = TMFStorage.getByUser(storageUID).database();
        } else {
            database = TMFStorage.getDefault().database();
        }
        mTopBar.setTitle(getResources().getString(R.string.db_CRUD));
        personDBManager = new PersonDBManager(CRUDActivity.this, database);
        etName = findViewById(R.id.et_name);
        etAddress = findViewById(R.id.et_address);
        tvResult = findViewById(R.id.tv_result);
        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        findViewById(R.id.btn_insert).setOnClickListener(this);
        findViewById(R.id.btn_select).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_update).setOnClickListener(this);
    }

    @Override
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_crud, null);
    }

    private String getStringById(int id){
        return getResources().getString(id);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_insert) {
            etAddress.setVisibility(View.VISIBLE);
            etAddress.setText("");
            tvResult.setText("");
            btnOk.setText(getStringById(R.string.module_storage_0));
            operationType = TYPE_INSERT;
        } else if (id == R.id.btn_select) {
            etAddress.setVisibility(View.GONE);
            tvResult.setText("");
            btnOk.setText(getStringById(R.string.module_storage_1));
            operationType = TYPE_SELECT;
        } else if (id == R.id.btn_delete) {
            etAddress.setVisibility(View.GONE);
            tvResult.setText("");
            btnOk.setText(getStringById(R.string.module_storage_2));
            operationType = TYPE_DELETE;
        } else if (id == R.id.btn_update) {
            etAddress.setVisibility(View.VISIBLE);
            etAddress.setText("");
            tvResult.setText("");
            btnOk.setText(getStringById(R.string.module_storage_3));
            operationType = TYPE_UPDATE;
        } else if (id == R.id.btn_ok) {
            name = etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(CRUDActivity.this, getStringById(R.string.module_storage_4), Toast.LENGTH_LONG).show();
                return;
            }
            if (operationType == TYPE_INSERT || operationType == TYPE_UPDATE) {
                address = etAddress.getText().toString().trim();
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(CRUDActivity.this, getStringById(R.string.module_storage_5), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            //做好非空判断后真正执行数据库CRUD
            executeCRUD();
        }
    }

    private void executeCRUD() {
        switch (operationType) {
            case TYPE_INSERT:
                insertPerson();
                break;
            case TYPE_SELECT:
                selectPerson();
                break;
            case TYPE_DELETE:
                deletePerson();
                break;
            case TYPE_UPDATE:
                updatePerson();
                break;
            default:
                break;
        }
    }

    private void insertPerson() {
        List<Person> dataExists = personDBManager.getPersonByName(name);
        if (dataExists != null && dataExists.size() > 0) {
            Toast.makeText(CRUDActivity.this, getStringById(R.string.module_storage_6), Toast.LENGTH_LONG).show();
        } else {
            Person person = new Person();
            person.setAddress(address);
            person.setName(name);
            boolean result = personDBManager.addPersonData(person);
            if (result) {
                Toast.makeText(CRUDActivity.this, getStringById(R.string.module_storage_7), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CRUDActivity.this, getStringById(R.string.module_storage_8), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void selectPerson() {
        StringBuffer result = new StringBuffer();
        List<Person> dataSelect = personDBManager.getPersonByName(name);
        if (dataSelect != null && dataSelect.size() > 0) {
            for (int i = 0; i < dataSelect.size(); i++) {
                result.append(dataSelect.get(i).toString());
            }
            tvResult.setText(result);
        } else {
            tvResult.setText(getStringById(R.string.module_storage_9));
        }
    }

    private void deletePerson() {
        List<Person> dataDeleteExists = personDBManager.getPersonByName(name);
        if (null != dataDeleteExists && dataDeleteExists.size() > 0) {
            String nameDel = dataDeleteExists.get(0).getName();
            String addressDel = dataDeleteExists.get(0).getAddress();
            if (!TextUtils.isEmpty(nameDel) && !TextUtils.isEmpty(addressDel)) {
                boolean b = personDBManager.delPersonByName(nameDel);
                if (b) {
                    Toast.makeText(CRUDActivity.this, getStringById(R.string.module_storage_10), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CRUDActivity.this, getStringById(R.string.module_storage_11), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(CRUDActivity.this, getStringById(R.string.module_storage_12), Toast.LENGTH_LONG).show();
        }
    }

    private void updatePerson() {
        List<Person> dataUpdate = personDBManager.getPersonByName(name);
        if (null != dataUpdate && dataUpdate.size() > 0) {
            boolean result = personDBManager.updatePersonByName(name, address);
            if (result) {
                Toast.makeText(CRUDActivity.this, getStringById(R.string.module_storage_13), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CRUDActivity.this, getStringById(R.string.module_storage_14), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(CRUDActivity.this, getStringById(R.string.module_storage_15), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        personDBManager.closeDB();
    }
}