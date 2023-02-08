package com.tencent.tmf.module.storage.db.cruddb;

import androidx.annotation.NonNull;

/**
 * Created by winnieyzhou on 2019/4/17.
 */
public class Person {

    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NonNull
    @Override
    public String toString() {
        return "name:" + getName() + "\naddress:" + getAddress() + "\n";
    }
}
