package com.sunshine.view.library.data;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.sunshine.view.library.LoaderConfigure;

/**
 * Created by Administrator on 2016/11/2.
 */

public class ImageInfo {
    ImageView imageView;
    LoadedFrom loadedFrom;
    String tag;
    Bitmap bitmap;
    LoaderConfigure loaderConfigure;

    public ImageInfo(ImageView imageView, String tag, Bitmap bitmap, LoaderConfigure loaderConfigure, LoadedFrom loadedFrom) {
        this.imageView = imageView;
        this.tag = tag;
        this.bitmap = bitmap;
        this.loaderConfigure = loaderConfigure;
        this.loadedFrom = loadedFrom;
    }


    public LoadedFrom getLoadedFrom() {
        return loadedFrom;
    }

    public void setLoadedFrom(LoadedFrom loadedFrom) {
        this.loadedFrom = loadedFrom;
    }

    public LoaderConfigure getLoaderConfigure() {
        return loaderConfigure;
    }

    public void setLoaderConfigure(LoaderConfigure loaderConfigure) {
        this.loaderConfigure = loaderConfigure;
    }


    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
