package com.tencent.tmf.module.shark.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PocCmdBean {

    String apiName;
    String apiDesc;
    int cmdId;
    String data;
    Map headers;
    Map cookies;
    Map queries;

    public Map getPathParams() {
        return pathParams;
    }

    public void setPathParams(Map pathParams) {
        this.pathParams = pathParams;
    }

    Map pathParams;

    public PocCmdBean() {

    }

    public PocCmdBean(String apiName, String apiDesc, int cmdId, String data, Map headers,
            Map cookies, Map queries, Map pathParams) {
        this.apiName = apiName;
        this.apiDesc = apiDesc;
        this.cmdId = cmdId;
        this.data = data;
        this.headers = headers;
        this.cookies = cookies;
        this.queries = queries;
        this.pathParams = pathParams;
    }

    public String getApiDesc() {
        return apiDesc;
    }

    public void setApiDesc(String apiDesc) {
        this.apiDesc = apiDesc;
    }

    public int getCmdId() {
        return cmdId;
    }

    public void setCmdId(int cmdId) {
        this.cmdId = cmdId;
    }

    public Map getCookies() {
        return cookies;
    }

    public void setCookies(Map cookies) {
        this.cookies = cookies;
    }

    public Map getHeaders() {
        return headers;
    }

    public void setHeaders(Map headers) {
        this.headers = headers;
    }

    public Map getQueries() {
        return queries;
    }

    public void setQueries(Map queries) {
        this.queries = queries;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static ArrayList<PocCmdBean> initPocCmdBeans() {

        PocCmdBean tmpCmdBean = new PocCmdBean();
        tmpCmdBean.setApiName("TMFEcho");
        tmpCmdBean.setApiDesc("interface transmit");
//        tmpCmdBean.setCmdId(20038);
        tmpCmdBean.setData("this is data");
        Map headers = new HashMap<>();
        headers.put("header_1", "h1");
        tmpCmdBean.setHeaders(headers);
        Map cookies = new HashMap<>();
        cookies.put("cookie_1", "c1");
        cookies.put("cookie_2", "value_2");
        tmpCmdBean.setCookies(cookies);
        Map queries = new HashMap<>();
        queries.put("stewardNo", "9808858");
        queries.put("isWap", "1");
        queries.put("processCode", "AM0013");
        tmpCmdBean.setQueries(queries);

        Map pathParams = new HashMap<>();
        pathParams.put("version", "v1");
        pathParams.put("aid", "2608");
        pathParams.put("uuid", "7027688298306865000");
        tmpCmdBean.setPathParams(pathParams);

        ArrayList<PocCmdBean> pocCmdBeans = new ArrayList<>();
        pocCmdBeans.add(tmpCmdBean);

        tmpCmdBean = new PocCmdBean();
        tmpCmdBean.setApiName("RATELIMIT");
        tmpCmdBean.setApiDesc("interface rate limit");
        tmpCmdBean.setCmdId(20040);
        tmpCmdBean.setData("this is data");
        headers = new HashMap<>();
        headers.put("header_1", "h1");
        tmpCmdBean.setHeaders(headers);
        cookies = new HashMap<>();
        cookies.put("cookie_1", "c1");
        cookies.put("cookie_2", "value_2");
        tmpCmdBean.setCookies(cookies);
        queries = new HashMap<>();
        queries.put("stewardNo", "9808858");
        queries.put("isWap", "1");
        queries.put("processCode", "AM0013");
        tmpCmdBean.setQueries(queries);
        pathParams.put("placeholder", "1");
        tmpCmdBean.setPathParams(pathParams);
        pocCmdBeans.add(tmpCmdBean);

        tmpCmdBean = new PocCmdBean();
        tmpCmdBean.setApiName("APIMOCK");
        tmpCmdBean.setApiDesc("interface MOCK");
        tmpCmdBean.setCmdId(20041);
        tmpCmdBean.setData("this is data");
        headers = new HashMap<>();
        headers.put("header_1", "h1");
        tmpCmdBean.setHeaders(headers);
        cookies = new HashMap<>();
        cookies.put("cookie_1", "c1");
        cookies.put("cookie_2", "value_2");
        tmpCmdBean.setCookies(cookies);
        queries = new HashMap<>();
        queries.put("stewardNo", "9808858");
        queries.put("isWap", "1");
        queries.put("processCode", "AM0013");
        tmpCmdBean.setQueries(queries);
        pathParams.put("placeholder", "1");
        tmpCmdBean.setPathParams(pathParams);
        pocCmdBeans.add(tmpCmdBean);

        tmpCmdBean = new PocCmdBean();
        tmpCmdBean.setApiName("ROUTEWeight");
        tmpCmdBean.setApiDesc("route weight");
        tmpCmdBean.setCmdId(20042);
        tmpCmdBean.setData("this is data");
        headers = new HashMap<>();
        headers.put("header_1", "h1");
        tmpCmdBean.setHeaders(headers);
        cookies = new HashMap<>();
        cookies.put("cookie_1", "c1");
        cookies.put("cookie_2", "value_2");
        tmpCmdBean.setCookies(cookies);
        queries = new HashMap<>();
        queries.put("stewardNo", "9808858");
        queries.put("isWap", "1");
        queries.put("processCode", "AM0013");
        tmpCmdBean.setQueries(queries);
        pathParams.put("placeholder", "1");
        tmpCmdBean.setPathParams(pathParams);
        pocCmdBeans.add(tmpCmdBean);

        tmpCmdBean = new PocCmdBean();
        tmpCmdBean.setApiName("SIGNMOCK");
        tmpCmdBean.setApiDesc("interface sign");
        tmpCmdBean.setCmdId(20044);
        tmpCmdBean.setData("this is data");
        headers = new HashMap<>();
        headers.put("header_1", "h1");
        tmpCmdBean.setHeaders(headers);
        cookies = new HashMap<>();
        cookies.put("cookie_1", "c1");
        cookies.put("cookie_2", "value_2");
        tmpCmdBean.setCookies(cookies);
        queries = new HashMap<>();
        queries.put("stewardNo", "9808858");
        queries.put("isWap", "1");
        queries.put("processCode", "AM0013");
        tmpCmdBean.setQueries(queries);
        pathParams.put("placeholder", "1");
        tmpCmdBean.setPathParams(pathParams);
        pocCmdBeans.add(tmpCmdBean);

        return pocCmdBeans;
    }
}
