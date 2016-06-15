//package com.yunmall.ymsdk.utility.thirdparty.wx;
//
//
//import java.io.File;
//import java.net.URL;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.text.TextUtils;
//
//import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.sdk.modelmsg.WXImageObject;
//import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.sdk.modelmsg.WXTextObject;
//import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.yunmall.ymsdk.R;
//import com.yunmall.ymsdk.utility.Utils;
//import com.yunmall.ymsdk.utility.YmLog;
//import com.yunmall.ymsdk.utility.YmToastUtils;
//
///**
// * 特别提醒，微信分享的缩略图不能大于32k
// */
//public class WXUtility {
//	
//	private IWXAPI api;
//	
//	
//	
//    private static WXUtility instance = null;
//    
//    private final int THUMB_SIZE = 150;
//    private final int MAX_SINA_PHOTO_SIZE = 32 * 1024; // 32KB
//    
//    private WXUtility(){}
//    
//    public static WXUtility getInstance(){
//    	if(instance == null){
//    		instance = new WXUtility();
//    	}
//    	return instance;
//    }
//
//    public void openWeiXin() {
//        if (api != null) {
//            api.openWXApp();
//        }
//    }
//
//    /**
//	 * 注册app to 微信
//	 * @param context
//	 * @param appId
//	 */
//	public void registerToWeiXin(Context context,String appId){
//		api = WXAPIFactory.createWXAPI(context, appId, false);
//		api.registerApp(appId);
//	}
//	
//	public void sendMessage(Context context, WXSendParam param) {
//        if (api == null) {
//            YmLog.e("wx", "wxapi is null");
//            YmToastUtils.showToast(context, R.string.Weixin_register_fail);
//            return;
//        }
//        if (!api.isWXAppInstalled()) {
//            YmLog.e("wx", "wx is not installed or support Api");
//            YmToastUtils.showToast(context, R.string.Weixin_not_install_notify);
//            return;
//        }
//
//        if (!api.isWXAppSupportAPI()) {
//            YmToastUtils.showToast(context, R.string.Weixin_not_support_API);
//        }
//
//        if (param.getType() == WXSendParam.TYPE_TEXT) {
//            YmLog.e("weixin", "进来");
//            WXTextObject textObj = new WXTextObject();
//            textObj.text = param.getDescription();
//
//            WXMediaMessage msg = new WXMediaMessage();
//            msg.mediaObject = textObj;
//            msg.description = param.getDescription();
//            if (param.getBitmap() != null) {
//                Bitmap bitmap = param.getBitmap();
//                Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
//                msg.thumbData = Utils.bmpToByteArray(thumbBmp, true);
//            }
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//            req.transaction = String.valueOf(System.currentTimeMillis());
//            req.message = msg;
//            req.scene = param.isTimeLine() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//            api.sendReq(req);
//        } else if (param.getType() == WXSendParam.TYPE_IMAGE) {
//            if (param.getBitmap() != null) {
//                Bitmap bitmap = param.getBitmap();
//                WXImageObject imgObj = new WXImageObject(param.getBitmap());
//
//                WXMediaMessage msg = new WXMediaMessage();
//                msg.mediaObject = imgObj;
//
//                Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
//                bitmap.recycle();
//                msg.thumbData = Utils.bmpToByteArray(thumbBmp, true);
//
//                SendMessageToWX.Req req = new SendMessageToWX.Req();
//                req.transaction = String.valueOf(System.currentTimeMillis());
//                req.message = msg;
//                req.scene = param.isTimeLine() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//                api.sendReq(req);
//            } else if (!TextUtils.isEmpty(param.getImagePath())) {
//                String path = param.getImagePath();
//                File file = new File(path);
//                if (!file.exists()) {
//                    return;
//                }
//
//                WXImageObject imgObj = new WXImageObject();
//                imgObj.setImagePath(path);
//
//                WXMediaMessage msg = new WXMediaMessage();
//                msg.mediaObject = imgObj;
//
//                Bitmap bmp = BitmapFactory.decodeFile(path);
//                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
//                bmp.recycle();
//                msg.thumbData = Utils.bmpToByteArray(thumbBmp, true);
//
//                SendMessageToWX.Req req = new SendMessageToWX.Req();
//                req.transaction = String.valueOf(System.currentTimeMillis());
//                req.message = msg;
//                req.scene = param.isTimeLine() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//                api.sendReq(req);
//            } else if (!TextUtils.isEmpty(param.getImageUrl())) {
//                try {
//                    WXImageObject imgObj = new WXImageObject();
//                    imgObj.imageUrl = param.getImageUrl();
//
//                    WXMediaMessage msg = new WXMediaMessage();
//                    msg.mediaObject = imgObj;
//
//                    Bitmap bmp = BitmapFactory.decodeStream(new URL(param.getImageUrl()).openStream());
//                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
//                    if (thumbBmp != null) {
//                        int size = THUMB_SIZE;
//                        byte[] data = Utils.bmpToByteArray(thumbBmp, false);
//                        int byteCount = data.length;
//                        while (byteCount > MAX_SINA_PHOTO_SIZE) {
//                            size = (int) size * 4 / 5;
//                            thumbBmp = Bitmap.createScaledBitmap(thumbBmp,
//                                    size, size, true);
//                            byteCount = Utils.bmpToByteArray(thumbBmp, false).length;
//                        }
//                        bmp.recycle();
//                        msg.thumbData = Utils.bmpToByteArray(thumbBmp, true);
//                    }
//
//                    SendMessageToWX.Req req = new SendMessageToWX.Req();
//                    req.transaction = String.valueOf(System.currentTimeMillis());
//                    req.message = msg;
//                    req.scene = param.isTimeLine() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//                    api.sendReq(req);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//        } else if (param.getType() == WXSendParam.TYPE_WEB) {
//            WXWebpageObject webpage = new WXWebpageObject();
//            webpage.webpageUrl = param.getWebUrl();
//            WXMediaMessage msg = new WXMediaMessage(webpage);
//            msg.title = param.getTitle();
//            msg.description = param.getDescription();
//			if (param.getBitmap() != null) {
//				Bitmap bitmap = param.getBitmap();
//				Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
//                if (thumbBmp != null) {
//                    int size = THUMB_SIZE;
//                    byte[] data = Utils.bmpToByteArray(thumbBmp, false);
//                    int byteCount = data.length;
//                    while (byteCount > MAX_SINA_PHOTO_SIZE) {
//                        size = (int) size * 4 / 5;
//                        thumbBmp = Bitmap.createScaledBitmap(thumbBmp,
//                                size, size, true);
//                        byteCount = Utils.bmpToByteArray(thumbBmp, false).length;
//                    }
//                    msg.thumbData = Utils.bmpToByteArray(thumbBmp, true);
//                }
//			}
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//            req.transaction = String.valueOf(System.currentTimeMillis());
//            req.message = msg;
//            req.scene = param.isTimeLine() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//            api.sendReq(req);
//        }
//    }
//
//
//}
