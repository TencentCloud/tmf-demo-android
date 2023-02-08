package com.tencent.tmf.applet.demo.ui.entity;

import java.io.File;

public class ServerConfigEntity {
    public String title;
    public String tcpHost;
    public String httpUrl;
    public String productId;
    public File file;
    public boolean isSelect;

    public ServerConfigEntity(String title, String tcpHost, String httpUrl, String productId, File file, boolean isSelect) {
        this.title = title;
        this.tcpHost = tcpHost;
        this.httpUrl = httpUrl;
        this.productId = productId;
        this.file = file;
        this.isSelect = isSelect;
    }
}
