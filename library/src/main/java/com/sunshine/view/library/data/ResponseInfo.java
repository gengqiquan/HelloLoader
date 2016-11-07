package com.sunshine.view.library.data;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/11/1.
 */

public class ResponseInfo {
    public Bitmap bitmap;
    public boolean success;

    public ResponseInfo(boolean success) {
        this.success = success;
    }
}
