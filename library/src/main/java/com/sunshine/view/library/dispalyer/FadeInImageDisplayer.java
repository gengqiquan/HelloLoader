package com.sunshine.view.library.dispalyer;

import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.sunshine.view.library.data.ImageInfo;
import com.sunshine.view.library.data.LoadedFrom;

/**
 * Created by Administrator on 2016/11/14.
 */

public class FadeInImageDisplayer implements Displayer {
    private final int durationMillis;
    private final boolean animateFromNetwork;
    private final boolean animateFromDisk;
    private final boolean animateFromMemory;

    public FadeInImageDisplayer(int durationMillis) {
        this(durationMillis, true, true, true);
    }

    public FadeInImageDisplayer(int durationMillis, boolean animateFromNetwork, boolean animateFromDisk, boolean animateFromMemory) {
        this.durationMillis = durationMillis;
        this.animateFromNetwork = animateFromNetwork;
        this.animateFromDisk = animateFromDisk;
        this.animateFromMemory = animateFromMemory;
    }
@Override
    public void display(Bitmap bitmap, ImageInfo info) {
        ImageView imageView = info.getImageView();
        LoadedFrom loadedFrom = info.getLoadedFrom();
        imageView.setImageBitmap(bitmap);
        if (this.animateFromNetwork && loadedFrom == LoadedFrom.NETWORK || this.animateFromDisk && (loadedFrom == LoadedFrom.DISC_CACHE || loadedFrom == LoadedFrom.DISC) || this.animateFromMemory && loadedFrom == LoadedFrom.MEMORY_CACHE) {
            animate(imageView, this.durationMillis);
        }

    }

    public static void animate(View imageView, int durationMillis) {
        if (imageView != null) {
            AlphaAnimation fadeImage = new AlphaAnimation(0.0F, 1.0F);
            fadeImage.setDuration((long) durationMillis);
            fadeImage.setInterpolator(new DecelerateInterpolator());
            imageView.startAnimation(fadeImage);
        }

    }
}
