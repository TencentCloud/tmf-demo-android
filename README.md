
# TMF Demo For Android

## 简介

腾讯移动开发平台（ Tencent Mobile Framework）整合了腾讯在移动产品中开发、测试、发布和运营的技术能力，为企业提供一站式、 覆盖全生命周期的移动端技术平台。TMF Demo For Android演示了如何在Android端接入TMF。

## 环境要求

|                | 版本          |
| -------------- | ------------- |
| Android Studio | 4.0及以上版本 |
| Android        | 5.0及以上版本 |

## 工程结构

工程整体采用组件化架构，将不同业务组件示例代码分散在了不同的module中，目录结构如下：

```
├── app                       //主module
├── buildscripts              //编译脚本目录
├── commonlib                 //公共库module, 为了方便管理，所有组件依赖都在本module中配置
├── module-base               //基础库
├── module-conch              //数据同步【私有化】
├── module-hotpatch           //热修复
├── module-hybrid             //Hybrid
├── module-main               //主界面
├── module-offline            //离线包
├── module-push               //推送
├── module-qmui               //QMUI【私有化】
├── module-upgrad             //应用发布
```

## 公有云开放组件说明

TMF支持公有云及私有化两种部署方式，不过目前公有云只开放了应用发布、热修复、离线包、消息推送四个组件，其余组件暂时只支持私有化部署，后续会逐步迭代开放到公有云，敬请期待。

[TMF官网](https://cloud.tencent.com/product/tmf)

[公有云控制台](https://console.cloud.tencent.com/tmf)

## 编译脚本说明

### 目录结构

Demo将应用基本信息、组件版本、module公共配置等统一抽取到了buildscripts目录，方便管理。

```
├── app.gradle                 //app module公共配置
├── build.gradle               //java lib公共配置
├── common-lib.gradle          //android lib公共配置
├── common.gradle              //app及lib公共配置
├── for-dev                    //仅TMF内部开发使用，无需关心
├── globalConfig.gradle        //应用基本信息、组件版本、推送、分享等配置项
├── module-build-config.gradle //仅TMF内部开发使用
└── module.gradle              //组件module通用配置
```

### 编译变体

- tmfMain： 默认变体，除热修复包外，默认使用该变体构建即可。
- tmfHotpatch:  热修复包变体, 构建热修复包使用。

### 应用基本信息配置

```
TMFDemo_version = [
        compileSdkVersion: 30,																	
        buildToolsVersion: "30.0.3",
        //oppo push要求最低19
        minSdkVersion    : 21,
        targetSdkVersion : 30,
        versionCode      : 1,
        versionName      : isCI() ? System.env.versionName : "1.0.0",
        buildNo          : isCI() ? System.env.build_no : 100001,
        channel          : "channel_googleplay"
        //是否使用新版协议：
        // 1. 公有云必须改为true。
        // 2. 私有化默认使用false即可。如需改为true，请确认服务版本是否支持新协议。
        protoTypeShark   : isCI() ? System.env.protoTypeShark : false,
]
```

## 运行Demo

1. 在TMF控制台下载您的应用配置文件tmf-android-configurations.json。注：应用包名需与demo一致，默认是com.tencent.tmf.demo。

2. 将tmf-android-configurations.json放置在app/src/main/assets目录下。

3. 如果是公有云将TMFDemo_version.protoTypeShark修改为true。

4. 使用默认变体tmfMain构建Demo。
   ```
   gradlew app:assembleTMFMain
   ```
5. 安装运行构建好的Demo。



