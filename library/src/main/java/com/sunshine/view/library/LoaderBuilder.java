package com.sunshine.view.library;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/11/1.
 */

public class LoaderBuilder {
    ImageView mImageView;
    LoaderConfigure mLoaderConfigure;
    Context mContext;
    HelloLoader mLoader;

    public LoaderBuilder(ImageView mImageView, HelloLoader mLoader) {
        this.mImageView = mImageView;
        this.mLoader = mLoader;
        this.mContext = mImageView.getContext();
    }

    public LoaderBuilder LoaderConfigure(LoaderConfigure mLoaderConfigure) {
        this.mLoaderConfigure = mLoaderConfigure;
        return this;
    }

    public void load(final String uri) {
        checkDefault();
        if (mLoaderConfigure.getLoadListener() != null) {
            mLoaderConfigure.getLoadListener().started();
        }
        mLoader.putTask(mContext, mLoaderConfigure, mImageView, uri);
        //从界面移除后取消加载请求
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
            mImageView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    mLoader.removeTask(mImageView);
                }
            });

    }

    private void checkDefault() {
        if (mLoaderConfigure == null) {
            mLoaderConfigure = mLoader.mDefaultConfigure;
        } else {
            if (mLoaderConfigure.getDisplayer() == null) {
                mLoaderConfigure.displayer(mLoader.mDefaultConfigure.getDisplayer());
            }
        }
    }
}
