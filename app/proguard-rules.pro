-repackageclasses tmf

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
#-dontskipnonpubliclibraryclassmembers
# 载Log要开优化
#-dontshrink
#-dontoptimize
-dontpreverify
-useuniqueclassmembernames
-ignorewarnings

# Keep Options
# 行数信息
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#fix 字节码优化引起的编译问题
-optimizations !code/simplification/*,!field/*,!class/merging/*,!method/removal/parameter,!method/propagation/*,!method/marking/static,!class/unboxing/enum,!code/removal/advanced,!code/allocation/variable

##=======================================keep 公共协议 开始====================================================
-keep class Protocol.** { *;}
-keep class android.annotation.** {
   *;
}
##=======================================keep 公共协议 结束====================================================


#TMFDemo特有的业务代码Keep
-keep class com.tencent.tmf.demo.TmfDelegaleApplication { *; }
-keep class com.tencent.tmf.demo.TmfApplication { *; }

-keep class com.tencent.stat.** { *;}

### ！！！！！以下的keep代码要移动到具体的组件中去
#=======================================keep 存储 开始====================================================
# Keep all native methods, their classes and any classes in their descriptors
-keepclasseswithmembers,includedescriptorclasses class com.tencent.mmkv.** {
    native <methods>;
    long nativeHandle;
    private static *** onMMKVCRCCheckFail(***);
    private static *** onMMKVFileLengthError(***);
    private static *** mmkvLogImp(...);
}

# Keep all native methods, their classes and any classes in their descriptors
-keepclasseswithmembers,includedescriptorclasses class com.tencent.wcdb.** {
    native <methods>;
}

# Keep all exception classes
-keep class com.tencent.wcdb.**.*Exception

# Keep classes referenced in JNI code
-keep class com.tencent.wcdb.database.WCDBInitializationProbe { <fields>; }
-keep,includedescriptorclasses class com.tencent.wcdb.database.SQLiteCustomFunction { *; }
-keep class com.tencent.wcdb.database.SQLiteDebug$* { *; }
-keep class com.tencent.wcdb.database.SQLiteCipherSpec { <fields>; }
-keep interface com.tencent.wcdb.support.Log$* { *; }

# Keep methods used as callbacks from JNI code
-keep class com.tencent.wcdb.repair.RepairKit { int onProgress(java.lang.String, int, long); }
-keep class com.tencent.wcdb.database.SQLiteConnection {
    void notifyCheckpoint(java.lang.String, int);
    void notifyChange(java.lang.String, java.lang.String, long[], long[], long[]);
}
#=======================================keep 存储 结束====================================================

##QAPM
-keep class com.tencent.qapmsdk.** { *;}

##OCR
-keep public class com.tencent.md.ocr.api.EMOCRDetector {
    <fields>;
    <methods>;
}

-keep public class com.tencent.md.ocr.api.DetectorResult {
    <fields>;
    <methods>;
}
## 企业微信
-keep class com.tencent.wework.api.** {
   *;
}
## 智慧投放
-keep class com.tencent.tmf.icdp.** { *;}
