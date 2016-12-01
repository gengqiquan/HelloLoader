package com.sunshine.view.library.download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sunshine.view.library.data.ResponseInfo;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/11/3.
 */

public class Okhttp3DownLoader implements Downloader {
    OkHttpClient client;

    public Okhttp3DownLoader(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public ResponseInfo downloadImgByUrl(String urlStr) {
        ResponseInfo responseInfo = new ResponseInfo(false);
        InputStream is = null;
        Bitmap bm = null;
        Request.Builder builder = new Request.Builder().url(urlStr);
        try {
            okhttp3.Response response = client.newCall(builder.build()).execute();
            int responseCode = response.code();
            if (responseCode == 200) {
                is = response.body().byteStream();
                if (is != null) {
                    bm = BitmapFactory.decodeStream(is, null, null);
                }
                if (bm != null) {
                    responseInfo.success = true;
                    responseInfo.bitmap = bm;
                }
            }
        } catch (IOException e) {
        }
        return responseInfo;
    }
}
