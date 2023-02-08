package com.tencent.tmf.applet.demo.ui.entity;

public class DebugEntity {
    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;
    public String title;
    public String content;
    public int type;

    public DebugEntity(String title, String content, int type) {
        this.title = title;
        this.content = content;
        this.type = type;
    }
}
