package com.sunshine.view.library.download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sunshine.view.library.data.ResponseInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/11/2.
 */

public class HttpUrlConnectionDownLoader implements Downloader {
    @Override
    public ResponseInfo downloadImgByUrl(String urlStr) {
        ResponseInfo responseInfo = new ResponseInfo(false);
        InputStream is = null;
        Bitmap bm=null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            if (is != null) {
                 bm = BitmapFactory.decodeStream(is, null, null);
            }
            if(bm!=null){
            responseInfo.success = true;
            responseInfo.bitmap = bm;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e){}
        }
        return responseInfo;
    }
}
