package com.sunshine.view.library.dispalyer;

import android.graphics.Bitmap;

import com.sunshine.view.library.data.ImageInfo;

/**
 * Created by Administrator on 2016/11/14.
 */

public class SimpleImageDisplayer implements Displayer {
    @Override
    public void display(Bitmap bm, ImageInfo info) {
        if (bm != null && !bm.isRecycled())
            info.getImageView().setImageBitmap(bm);
    }
}
