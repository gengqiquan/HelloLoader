package com.sunshine.view.library.dispalyer;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.sunshine.view.library.data.ImageInfo;
import com.sunshine.view.library.mask.Masker;

/**
 * Created by Administrator on 2016/11/14.
 */

public class MaskDisplayer implements Displayer {
    public MaskDisplayer(Masker mMasker) {
        this.mMasker = mMasker;
    }

    Masker mMasker;

    @Override
    public void display(Bitmap bm, ImageInfo info) {
        ImageView imageView = info.getImageView();
        bm = mMasker.mask(bm);
        if (bm != null && !bm.isRecycled()) {
            imageView.setImageBitmap(bm);
        }
    }
}
