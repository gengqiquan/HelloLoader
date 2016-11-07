package com.sunshine.view.helloloader;

import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.RequestFuture;
import com.sunshine.view.library.data.ResponseInfo;
import com.sunshine.view.library.download.Downloader;

/**
 * Created by Administrator on 2016/11/3.
 */

public class VolleyDownLoader implements Downloader {
    RequestQueue requestQueue;

    public VolleyDownLoader(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public ResponseInfo downloadImgByUrl(String urlStr) {
        RequestFuture future = RequestFuture.newFuture();
        Bitmap bm = null;
        ResponseInfo responseInfo = new ResponseInfo(false);
        ImageRequest request = new ImageRequest(urlStr, future, 0, 0, Bitmap.Config.RGB_565, future);
        requestQueue.add(request);
        try {
            bm= (Bitmap) future.get();
            if (bm != null) {
                responseInfo.success = true;
                responseInfo.bitmap = bm;
            }
        } catch (Exception e) {
        }
        return responseInfo;
    }
}
