package com.tencent.tmf.common.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;
import com.tencent.tmf.common.BuildConfig;
import com.tencent.tmf.common.R;
import com.tencent.tmf.share.api.ITMFShareListener;
import com.tencent.tmf.share.api.ITMFShareProperty;
import com.tencent.tmf.share.api.ITMFSharePropertyBuilder;
import com.tencent.tmf.share.api.TMFShareContants;
import com.tencent.tmf.share.api.TMFShareService;
import com.tencent.tmf.share.api.TMFShareService.SharePlatform;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 分享组件的公共类
 */
public class ShareManager {

    private static final String BASE64_PREFIX = "data:image/png;base64,";


    private static final String IMAGE_NAME = "share_test.png";
    private static final String IMAGE_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                    + Environment.DIRECTORY_DOWNLOADS + File.separator + IMAGE_NAME;

    private static final String FILE_NAME = "share_test.txt";
    private static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
            + Environment.DIRECTORY_DOWNLOADS + File.separator + FILE_NAME;

    private static final String VIDEO_NAME = "share_test.mp4";
    private static final String VIDEO_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Environment.DIRECTORY_MOVIES
                    + File.separator + VIDEO_NAME;

    public static void shareWordToWx(Activity activity, ITMFShareListener shareListener, boolean timeline) {
        String title = activity.getString(R.string.share_manager_share_to_wechat_friend);
        String content = activity.getString(R.string.share_manager_share_test_content);
        ITMFShareProperty shareProperty = TMFShareService.createSharePropertyBuilder()
                .setTitle(title).setContent(content)
                .build();
        TMFShareService shareService = TMFShareService.getInstance();
        shareService.setShareListener(shareListener);
        shareService.share(activity, com.tencent.tmf.common.BuildConfig.SHARE_WX_APP_ID, getWxShareType(timeline),
                shareProperty);
    }

    public static void shareImageToWx(Activity activity, ITMFShareListener shareListener, boolean timeline,
            boolean isBitmap) {
        ITMFShareProperty shareProperty = null;
        if (isBitmap) {
            // 根据二进制图片分享
            shareProperty = TMFShareService.createSharePropertyBuilder()
                    .setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.example_image0))
                    .build();
        } else {
            // 根据图片路径分享. 若要使用FileProvider方式,文件要保存在应用外部或内部私有路径中,子路径要和xml中配置的path一致.
            String imagePath = activity.getExternalFilesDir("img").getAbsolutePath() + File.separator + IMAGE_NAME;
//            imagePath = activity.getFilesDir().getAbsolutePath() + File.separator + IMAGE_NAME;
//            imagePath = activity.getCacheDir().getAbsolutePath() + File.separator + IMAGE_NAME;
//            imagePath = IMAGE_PATH; // 非应用私有路径则用的是 旧方式分享。若是非公共目录 在android11上会分享失败
            if (copyAssetsFileToSdcard(activity, IMAGE_NAME, imagePath)) {
                shareProperty = TMFShareService.createSharePropertyBuilder()
                        .setLocalImageUrl(imagePath)
                        .build();
            } else {
                String content = activity.getString(R.string.share_manager_create_local_pic_failed);
                Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
            }
        }
        if (shareProperty != null) {
            TMFShareService shareService = TMFShareService.getInstance();
            shareService.setShareListener(shareListener);
            shareService.share(activity, BuildConfig.SHARE_WX_APP_ID, getWxShareType(timeline), shareProperty);
        }
    }

    public static void shareWebpageToWx(Activity activity, ITMFShareListener shareListener, boolean timeline) {
        String title = activity.getResources().getString(R.string.share_manager_share_to_wechat);
        String content = activity.getResources().getString(R.string.share_manager_share_to_wechat_content);
        ITMFShareProperty shareProperty = TMFShareService.createSharePropertyBuilder()
                .setTitle(title).setContent(content)
                .setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.qmui_icon_tip_new))
                .setJumpUrl("https://www.qq.com/?fromdefault")
                .build();
        TMFShareService shareService = TMFShareService.getInstance();
        shareService.setShareListener(shareListener);
        shareService.share(activity, com.tencent.tmf.common.BuildConfig.SHARE_WX_APP_ID, getWxShareType(timeline),
                shareProperty);
    }

    // 企业微信分享目前没有回调，所以默认回调成功
    public static void shareWebpageToCwx(Activity activity, ITMFShareListener shareListener) {
        String title = activity.getResources().getString(R.string.share_manager_share_to_company_wechat);
        String content = activity.getResources().getString(R.string.share_manager_share_to_company_wechat_content);
        ITMFShareProperty shareProperty = TMFShareService.createSharePropertyBuilder()
                .setTitle(title).setContent(content)
                .setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.qmui_icon_tip_new))
                .setJumpUrl("https://www.qq.com/?fromdefault")
                .build();
        TMFShareService shareService = TMFShareService.getInstance();
        shareService.setShareListener(shareListener);
        shareService.share(activity, SharePlatform.CWx_Friend, shareProperty);
    }

    public static void shareWordToCwx(Activity activity, ITMFShareListener shareListener) {
        String content = activity.getResources().getString(R.string.share_manager_share_to_company_wechat_content_test);
        ITMFShareProperty shareProperty = TMFShareService.createSharePropertyBuilder()
                .setContent(content)
                .build();
        TMFShareService shareService = TMFShareService.getInstance();
        shareService.setShareListener(shareListener);
        shareService.share(activity, SharePlatform.CWx_Friend, shareProperty);
    }

    public static void shareImageToCwx(Activity activity, ITMFShareListener shareListener) {
        ITMFShareProperty shareProperty = null;
        // 根据图片路径分享
        String imagePath = activity.getExternalFilesDir("img").getAbsolutePath() + File.separator + IMAGE_NAME;
//        imagePath = activity.getFilesDir().getAbsolutePath() + File.separator + IMAGE_NAME;
//        imagePath = IMAGE_PATH;
        if (copyAssetsFileToSdcard(activity, IMAGE_NAME, imagePath)) {
            shareProperty = TMFShareService.createSharePropertyBuilder()
                    .setLocalImageUrl(imagePath)
                    .build();
        } else {
            String content = activity.getResources().getString(R.string.share_manager_create_local_pic_failed);
            Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
        }
        if (shareProperty != null) {
            TMFShareService shareService = TMFShareService.getInstance();
            shareService.setShareListener(shareListener);
            shareService.share(activity, SharePlatform.CWx_Friend, shareProperty);
        }
    }

    public static void shareFileToCwx(Activity activity, ITMFShareListener shareListener) {
        ITMFShareProperty shareProperty = null;
        String filePath = activity.getExternalFilesDir("").getAbsolutePath() + File.separator + FILE_NAME;
        // 根据文件路径分享
        if (copyAssetsFileToSdcard(activity, FILE_NAME, filePath)) {
            shareProperty = TMFShareService.createSharePropertyBuilder()
                    .setFilePath(filePath)
                    .build();
        } else {
            String content = activity.getResources().getString(R.string.share_manager_create_local_file_failed);
            Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
        }
        if (shareProperty != null) {
            TMFShareService shareService = TMFShareService.getInstance();
            shareService.setShareListener(shareListener);
            shareService.share(activity, SharePlatform.CWx_Friend, shareProperty);
        }
    }

    public static void shareVideoToCwx(Activity activity, ITMFShareListener shareListener) {
        ITMFShareProperty shareProperty = null;
        String filePath = activity.getExternalFilesDir("").getAbsolutePath() + File.separator + VIDEO_NAME;
        // 根据文件路径分享
        if (copyAssetsFileToSdcard(activity, VIDEO_NAME, filePath)) {
            shareProperty = TMFShareService.createSharePropertyBuilder()
                    .setVideoPath(filePath)
                    .build();
        } else {
            String content = activity.getResources().getString(R.string.share_manager_create_local_video_failed);
            Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
        }
        if (shareProperty != null) {
            TMFShareService shareService = TMFShareService.getInstance();
            shareService.setShareListener(shareListener);
            shareService.share(activity, SharePlatform.CWx_Friend, shareProperty);
        }
    }

    public static void shareWordToAlipay(Activity activity, ITMFShareListener shareListener, boolean timeline) {
        String content = activity.getResources().getString(R.string.share_manager_share_to_zfb);
        ITMFShareProperty shareProperty = TMFShareService.createSharePropertyBuilder()
                .setContent(content)
                .build();

        TMFShareService shareService = TMFShareService.getInstance();
        shareService.setShareListener(shareListener);
        shareService.share(activity, com.tencent.tmf.common.BuildConfig.SHARE_ALIPAY_APP_ID,
                getAlipayShareType(activity, timeline), shareProperty);
    }

    public static void shareWordToDingDing(Activity activity, ITMFShareListener shareListener) {
        String content = activity.getResources().getString(R.string.share_manager_share_to_ding_talk);
        ITMFShareProperty shareProperty = TMFShareService.createSharePropertyBuilder()
                .setContent(content)
                .build();

        TMFShareService shareService = TMFShareService.getInstance();
        shareService.setShareListener(shareListener);
        shareService.share(activity, BuildConfig.SHARE_DINGDING_APP_ID, TMFShareContants.ShareType.Ding_FRIEND,
                shareProperty);
    }

    public static void shareImageToAlipay(Activity activity, ITMFShareListener shareListener, boolean timeline) {
        // 分享图片有3种方式，优先级为：本地图片>图片url>二进制图片
        // 分享图片URL
        ITMFShareProperty shareProperty = TMFShareService.createSharePropertyBuilder()
                .setImageUrl("http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg")
                .build();
        // 分享本地图片
        copyAssetsFileToSdcard(activity, IMAGE_NAME, IMAGE_PATH);
        ITMFShareProperty shareProperty1 = TMFShareService.createSharePropertyBuilder()
                .setLocalImageUrl(IMAGE_PATH)
                .build();
        // 分享bitmap
        ITMFShareProperty sharePropert2 = TMFShareService.createSharePropertyBuilder()
                .setBitmap(
                        BitmapFactory.decodeResource(activity.getResources(), R.mipmap.icon_more_operation_share_weibo))
                .build();
        TMFShareService shareService = TMFShareService.getInstance();
        shareService.setShareListener(shareListener);
        shareService.share(activity, com.tencent.tmf.common.BuildConfig.SHARE_ALIPAY_APP_ID,
                getAlipayShareType(activity, timeline), shareProperty);
    }

    public static void shareImageToDingDing(Activity activity, ITMFShareListener shareListener) {
        // 分享图片有3种方式，优先级为：本地图片>图片url>二进制图片
        // 分享图片URL
        ITMFShareProperty shareProperty = TMFShareService.createSharePropertyBuilder()
                .setImageUrl("http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg")
                .build();
        // 分享本地图片
        copyAssetsFileToSdcard(activity, IMAGE_NAME, IMAGE_PATH);
        ITMFShareProperty shareProperty1 = TMFShareService.createSharePropertyBuilder()
                .setLocalImageUrl(IMAGE_PATH)
                .build();
        // 分享bitmap
        ITMFShareProperty sharePropert2 = TMFShareService.createSharePropertyBuilder()
                .setBitmap(
                        BitmapFactory.decodeResource(activity.getResources(), R.mipmap.icon_more_operation_share_weibo))
                .build();
        TMFShareService shareService = TMFShareService.getInstance();
        shareService.setShareListener(shareListener);
        shareService.share(activity, BuildConfig.SHARE_DINGDING_APP_ID, TMFShareContants.ShareType.Ding_FRIEND,
                shareProperty);
    }


    public static void shareWebpageToAlipay(Activity activity, ITMFShareListener shareListener, boolean timeline) {
        String title = activity.getResources().getString(R.string.share_manager_share_to_zfb_friend);
        String content = activity.getResources().getString(R.string.share_manager_share_to_zfb);

        ITMFShareProperty shareProperty = TMFShareService.createSharePropertyBuilder()
                .setTitle(title).setContent(content)
                .setImageUrl("http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg")
                .setJumpUrl("https://www.qq.com/")
                .build();

        TMFShareService shareService = TMFShareService.getInstance();
        shareService.setShareListener(shareListener);
        shareService.share(activity, com.tencent.tmf.common.BuildConfig.SHARE_ALIPAY_APP_ID,
                getAlipayShareType(activity, timeline), shareProperty);
    }

    public static void shareWebpageToDingDing(Activity activity, ITMFShareListener shareListener) {
        String title = activity.getResources().getString(R.string.share_manager_share_to_ding_friend);
        String content = activity.getResources().getString(R.string.share_manager_share_to_ding_talk);

        ITMFShareProperty shareProperty = TMFShareService.createSharePropertyBuilder()
                .setTitle(title).setContent(content)
                .setImageUrl("http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg")
                .setJumpUrl("https://www.qq.com/")
                .build();

        TMFShareService shareService = TMFShareService.getInstance();
        shareService.setShareListener(shareListener);
        shareService.share(activity, BuildConfig.SHARE_DINGDING_APP_ID, TMFShareContants.ShareType.Ding_FRIEND,
                shareProperty);
    }


    public static void shareToWeibo(Activity activity, ITMFShareListener shareListener, boolean hasText,
            boolean hasImage) {
        ITMFSharePropertyBuilder sharePropertyBuilder = TMFShareService.createSharePropertyBuilder()
                .setWeiboRedirectUrl(com.tencent.tmf.common.BuildConfig.SHARE_WEIBO_REDIRECT_URL)
                .setWeiboScope(com.tencent.tmf.common.BuildConfig.SHARE_WEIBO_SCOPE);

        if (hasText) {
            String title = activity.getResources().getString(R.string.share_manager_share_to_weibo_friend);
            String content = activity.getResources().getString(R.string.share_manager_share_to_weibo);

            sharePropertyBuilder.setContent(content).setTitle(title)
                    .setJumpUrl("https://www.qq.com/?fromdefault");
        }
        if (hasImage) {
            sharePropertyBuilder.setBitmap(
                    BitmapFactory.decodeResource(activity.getResources(), R.mipmap.icon_more_operation_share_weibo));
        }

        if (hasText || hasImage) {
            TMFShareService shareService = TMFShareService.getInstance();
            shareService.setShareListener(shareListener);
            shareService.share(activity, com.tencent.tmf.common.BuildConfig.SHARE_WEIBO_APP_ID,
                    TMFShareContants.ShareType.SINA_WEIBO, sharePropertyBuilder.build());
        } else {
            String content = activity.getResources().getString(R.string.share_manager_share_to_at_least_one);

            Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
        }
    }

    public static void shareToWeibo(Activity activity, ITMFShareListener shareListener, ShareParam param) {
        ITMFSharePropertyBuilder sharePropertyBuilder = TMFShareService.createSharePropertyBuilder()
                .setWeiboRedirectUrl(com.tencent.tmf.common.BuildConfig.SHARE_WEIBO_REDIRECT_URL)
                .setWeiboScope(com.tencent.tmf.common.BuildConfig.SHARE_WEIBO_SCOPE);

        boolean isValid = false;
        if (!TextUtils.isEmpty(param.desc)) {
            sharePropertyBuilder.setContent(param.desc).setTitle(param.title).setJumpUrl(param.link);
            isValid = true;
        }
        if (!TextUtils.isEmpty(param.imgUrl)) {
            Bitmap bitmap = base64ToBitmap(param.imgUrl);
            if (bitmap != null) {
                sharePropertyBuilder.setBitmap(bitmap);
                isValid = true;
            }
        }

        if (isValid) {
            TMFShareService shareService = TMFShareService.getInstance();
            shareService.setShareListener(shareListener);
            shareService.share(activity, com.tencent.tmf.common.BuildConfig.SHARE_WEIBO_APP_ID,
                    TMFShareContants.ShareType.SINA_WEIBO, sharePropertyBuilder.build());
        }
    }

    public static void shareImageToQQ(Activity activity, ITMFShareListener shareListener) {

        // QQ 适配了FileProvider分享，传入文件路径即可。图片限制大小为5M
        String imagePath = activity.getExternalFilesDir("img").getAbsolutePath() + File.separator + IMAGE_NAME;
//        imagePath = activity.getFilesDir().getAbsolutePath() + File.separator + IMAGE_NAME;
//        imagePath = activity.getCacheDir().getAbsolutePath() + File.separator + IMAGE_NAME;
//      imagePath = IMAGE_PATH;
        if (copyAssetsFileToSdcard(activity, IMAGE_NAME, imagePath)) {
            ITMFShareProperty shareProperty = TMFShareService.createSharePropertyBuilder()
                    .setLocalImageUrl(imagePath)
                    .build();
            TMFShareService shareService = TMFShareService.getInstance();
            shareService.setShareListener(shareListener);
            shareService
                    .share(activity, BuildConfig.SHARE_QQ_APP_ID, TMFShareContants.ShareType.QQ_FRIEND, shareProperty);
        } else {
            String content = activity.getResources().getString(R.string.share_manager_create_local_pic_failed);

            Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
        }


    }

    public static void shareWordAndImageToQQ(Activity activity, ITMFShareListener shareListener) {
        String title = activity.getResources().getString(R.string.share_manager_share_to_qq);
        String content = activity.getResources().getString(R.string.share_manager_share_to_qq_content);

        ITMFShareProperty shareProperty = TMFShareService.createSharePropertyBuilder()
                .setAppName("TMFDemo").setTitle(title).setContent(content)
                .setImageUrl("http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg")
                .setJumpUrl("https://www.qq.com/?fromdefault")
                .build();
        TMFShareService shareService = TMFShareService.getInstance();
        shareService.setShareListener(shareListener);
        shareService.share(activity, com.tencent.tmf.common.BuildConfig.SHARE_QQ_APP_ID,
                TMFShareContants.ShareType.QQ_FRIEND, shareProperty);
    }

    public static void shareToSMS(Activity activity) {
        // 没有发送成功，失败的监听
        TMFShareService shareService = TMFShareService.getInstance();
        shareService.share(activity, "", TMFShareContants.ShareType.SMS, null);
    }

    private static int getWxShareType(boolean timeline) {
        return timeline ? TMFShareContants.ShareType.WX_TIMELINE : TMFShareContants.ShareType.WX_FRIEND;
    }

    private static int getAlipayShareType(Context context, boolean timeline) {
        return (!TMFShareService.isIgnoreAlipayShareChannel(context) && timeline)
                ? TMFShareContants.ShareType.ALI_PAY_TIMELINE : TMFShareContants.ShareType.ALI_PAY_FRIEND;
    }

    public static void shareToWx(Activity activity, ITMFShareListener shareListener, boolean timeline,
            ShareParam param) {
        ITMFSharePropertyBuilder sharePropertyBuilder = null;
        if (!TextUtils.isEmpty(param.link)) {
            sharePropertyBuilder = TMFShareService.createSharePropertyBuilder()
                    .setTitle(param.title).setContent(param.desc)
                    .setJumpUrl(param.link);
            if (!TextUtils.isEmpty(param.imgUrl)) {
                Bitmap bitmap = base64ToBitmap(param.imgUrl);
                if (bitmap != null) {
                    sharePropertyBuilder.setBitmap(bitmap);
                }
            }
        } else {
            if (!TextUtils.isEmpty(param.imgUrl)) {
                Bitmap bitmap = base64ToBitmap(param.imgUrl);
                if (bitmap != null) {
                    sharePropertyBuilder = TMFShareService.createSharePropertyBuilder().setBitmap(bitmap);
                }
            } else if (!TextUtils.isEmpty(param.desc)) {
                sharePropertyBuilder = TMFShareService.createSharePropertyBuilder()
                        .setTitle(param.title).setContent(param.desc);
            }
        }
        if (sharePropertyBuilder != null) {
            TMFShareService shareService = TMFShareService.getInstance();
            shareService.setShareListener(shareListener);
            shareService.share(activity, com.tencent.tmf.common.BuildConfig.SHARE_WX_APP_ID, getWxShareType(timeline),
                    sharePropertyBuilder.build());
        }
    }

    public static void shareToCwx(Activity activity, ITMFShareListener shareListener, boolean timeline,
            ShareParam param) {
        ITMFSharePropertyBuilder sharePropertyBuilder = null;
        if (!TextUtils.isEmpty(param.link)) {
            sharePropertyBuilder = TMFShareService.createSharePropertyBuilder()
                    .setTitle(param.title).setContent(param.desc)
                    .setJumpUrl(param.link);
            if (!TextUtils.isEmpty(param.imgUrl)) {
                Bitmap bitmap = base64ToBitmap(param.imgUrl);
                if (bitmap != null) {
                    sharePropertyBuilder.setBitmap(bitmap);
                }
            }
        } else {
            if (!TextUtils.isEmpty(param.imgUrl)) {
                Bitmap bitmap = base64ToBitmap(param.imgUrl);
                if (bitmap != null) {
                    sharePropertyBuilder = TMFShareService.createSharePropertyBuilder().setBitmap(bitmap);
                }
            } else if (!TextUtils.isEmpty(param.desc)) {
                sharePropertyBuilder = TMFShareService.createSharePropertyBuilder()
                        .setTitle(param.title).setContent(param.desc);
            }
        }
        if (sharePropertyBuilder != null) {
            TMFShareService shareService = TMFShareService.getInstance();
            shareService.setShareListener(shareListener);
            shareService.share(activity, SharePlatform.CWx_Friend,
                    sharePropertyBuilder.build());
        }
    }

    public static void shareToAlipay(Activity activity, ITMFShareListener shareListener, boolean timeline,
            ShareParam param) {
        ITMFSharePropertyBuilder sharePropertyBuilder = null;
        if (!TextUtils.isEmpty(param.link)) {
            sharePropertyBuilder = TMFShareService.createSharePropertyBuilder()
                    .setTitle(param.title).setContent(param.desc)
                    .setJumpUrl(param.link);
            if (!TextUtils.isEmpty(param.imgUrl)) {
                if (isImgBase64(param.imgUrl)) {
                    Bitmap bitmap = base64ToBitmap(param.imgUrl);
                    if (bitmap != null) {
                        sharePropertyBuilder.setBitmap(bitmap);
                    }
                } else if (isNetworkUrl(param.imgUrl)) {
                    sharePropertyBuilder.setImageUrl(param.imgUrl); // param.imgUrl需要是图片的url
                }
            }
        } else {
            if (!TextUtils.isEmpty(param.imgUrl)) {
                if (isImgBase64(param.imgUrl)) {
                    Bitmap bitmap = base64ToBitmap(param.imgUrl);
                    if (bitmap != null) {
                        sharePropertyBuilder = TMFShareService.createSharePropertyBuilder().setBitmap(bitmap);
                    }
                } else if (isNetworkUrl(param.imgUrl)) {
                    sharePropertyBuilder = TMFShareService.createSharePropertyBuilder().setImageUrl(param.imgUrl);
                }
            } else if (!TextUtils.isEmpty(param.desc)) {
                sharePropertyBuilder = TMFShareService.createSharePropertyBuilder().setContent(param.desc);
            }
        }
        if (sharePropertyBuilder != null) {
            TMFShareService shareService = TMFShareService.getInstance();
            shareService.setShareListener(shareListener);
            shareService.share(activity, com.tencent.tmf.common.BuildConfig.SHARE_ALIPAY_APP_ID,
                    getAlipayShareType(activity, timeline), sharePropertyBuilder.build());
        }
    }

    public static void shareToDing(Activity activity, ITMFShareListener shareListener, ShareParam param) {
        ITMFSharePropertyBuilder sharePropertyBuilder = null;
        if (!TextUtils.isEmpty(param.link)) {
            sharePropertyBuilder = TMFShareService.createSharePropertyBuilder()
                    .setTitle(param.title).setContent(param.desc)
                    .setJumpUrl(param.link);
            if (!TextUtils.isEmpty(param.imgUrl)) {
                if (isImgBase64(param.imgUrl)) {
                    Bitmap bitmap = base64ToBitmap(param.imgUrl);
                    if (bitmap != null) {
                        sharePropertyBuilder.setBitmap(bitmap);
                    }
                } else if (isNetworkUrl(param.imgUrl)) {
                    sharePropertyBuilder.setImageUrl(param.imgUrl); // param.imgUrl需要是图片的url
                }
            }
        } else {
            if (!TextUtils.isEmpty(param.imgUrl)) {
                if (isImgBase64(param.imgUrl)) {
                    Bitmap bitmap = base64ToBitmap(param.imgUrl);
                    if (bitmap != null) {
                        sharePropertyBuilder = TMFShareService.createSharePropertyBuilder().setBitmap(bitmap);
                    }
                } else if (isNetworkUrl(param.imgUrl)) {
                    sharePropertyBuilder = TMFShareService.createSharePropertyBuilder().setImageUrl(param.imgUrl);
                }
            } else if (!TextUtils.isEmpty(param.desc)) {
                sharePropertyBuilder = TMFShareService.createSharePropertyBuilder().setContent(param.desc);
            }
        }
        if (sharePropertyBuilder != null) {
            TMFShareService shareService = TMFShareService.getInstance();
            shareService.setShareListener(shareListener);
            shareService.share(activity, BuildConfig.SHARE_DINGDING_APP_ID, TMFShareContants.ShareType.Ding_FRIEND,
                    sharePropertyBuilder.build());
        }
    }

    public static void shareToQQ(Activity activity, ITMFShareListener shareListener, ShareParam param) {
        ITMFShareProperty shareProperty = TMFShareService.createSharePropertyBuilder()
                .setTitle(param.title).setContent(param.desc)
                .setImageUrl(param.imgUrl)
                .setJumpUrl(param.link)
                .build();
        TMFShareService shareService = TMFShareService.getInstance();
        shareService.setShareListener(shareListener);
        shareService.share(activity, com.tencent.tmf.common.BuildConfig.SHARE_QQ_APP_ID,
                TMFShareContants.ShareType.QQ_FRIEND, shareProperty);
    }

    private static Bitmap base64ToBitmap(String base64) {
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(base64) && base64.startsWith(BASE64_PREFIX)) {
            base64 = base64.substring(BASE64_PREFIX.length());
            try {
                byte[] bitmapArray;
                bitmapArray = Base64.decode(base64, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
                bitmap = null;
            }
        }
        return bitmap;
    }

    private static boolean isImgBase64(String img) {
        return img.startsWith(BASE64_PREFIX);
    }

    private static boolean isNetworkUrl(String img) {
        return img.startsWith("http://") || img.startsWith("https://");
    }

    private static boolean copyAssetsFileToSdcard(Context context, String assetsFileName, String desPath) {
        File file = new File(desPath);
        if (file.exists()) {
            return true;
        }
        //将内容写入到文件中
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = context.getAssets().open(assetsFileName);
            out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteCount);
            }
            out.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

}
