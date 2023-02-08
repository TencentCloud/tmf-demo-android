package com.tencent.tmf.demo;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

public class TmfApplication extends TinkerApplication {

    public TmfApplication() {
        super(
                // tinkerFlags, tinker支持的类型，dex,library，还是全部都支持！
                ShareConstants.TINKER_ENABLE_ALL,
                // ApplicationLike的实现类，只能传递字符串(继承自BaseHotpatchApplication的类)
                "com.tencent.tmf.demo.TmfDelegaleApplication",
                // Tinker的加载器，一般来说用默认的即可
                "com.tencent.tinker.loader.TinkerLoader",
                // tinkerLoadVerifyFlag, 运行加载时是否校验dex与,ib与res的Md5
                // 由于合成过程中我们已经校验了各个文件的Md5，并将它们存放在/data/data/..目录中。
                // 默认每次加载时我们并不会去校验tinker文件的Md5,但是你也可通过开启loadVerifyFlag强制每次加载时校验，但是这会带来一定的时间损耗。
                false);
    }
}
