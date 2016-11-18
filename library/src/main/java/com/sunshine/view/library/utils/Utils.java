package com.sunshine.view.library.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;

/**
 * Created by Administrator on 2016/11/1.
 */

public class Utils {
    /**
     * 获得缓存图片的文件
     *
     * @param path
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(String path, String uniqueName) {
        return new File(path + File.separator + uniqueName);
    }
    /**
     * 获得缓存图片的地址
     *
     * @param path
     * @param uniqueName
     * @return
     */
    public static String getDiskCachePath(String path, String uniqueName) {
        return  path + File.separator + uniqueName;
    }
    /**
     * 写入文件
     * @author Administrator
     * @date 2016/11/1 17:40
     */
    public static void write2File(String path, Bitmap bm, String key) {
        File file = Utils.getDiskCacheDir(path, key);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
        }
    }
    /**
     * 利用签名辅助类，将字符串字节数组
     *
     * @param str
     * @return
     */
    public static  String md5(String str) {
        byte[] digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            digest = md.digest(str.getBytes());
            return bytes2hex02(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 方式二
     *
     * @param bytes
     * @return
     */
    public static  String bytes2hex02(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (byte b : bytes) {
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1)// 每个字节8为，转为16进制标志，2个16进制位
            {
                tmp = "0" + tmp;
            }
            sb.append(tmp);
        }

        return sb.toString();

    }
    public static int getMemoryCacheSize(Context context) {
        ActivityManager am = getService(context, ACTIVITY_SERVICE);
        boolean largeHeap = (context.getApplicationInfo().flags & FLAG_LARGE_HEAP) != 0;
        int memoryClass = am.getMemoryClass();
        if (largeHeap && SDK_INT >= HONEYCOMB) {
            memoryClass = ActivityManagerHoneycomb.getLargeMemoryClass(am);
        }
//        if(memoryClass>70){
//            memoryClass=10;
//        }
//        else {
//            memoryClass=memoryClass/7;
//        }
        // Target ~15% of the available heap.
        return 1024 * 1024 * memoryClass/7;
    }

    @TargetApi(HONEYCOMB)
    public static class ActivityManagerHoneycomb {
        static int getLargeMemoryClass(ActivityManager activityManager) {
            return activityManager.getLargeMemoryClass();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getService(Context context, String service) {
        return (T) context.getSystemService(service);
    }
    // 判断是否NULL
    public static boolean checkNULL(String str) {
        return str == null || "null".equals(str) || "".equals(str);

    }
}
