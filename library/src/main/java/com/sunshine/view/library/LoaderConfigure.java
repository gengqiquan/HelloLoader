package com.sunshine.view.library;

import com.sunshine.view.library.data.Type;
import com.sunshine.view.library.dispalyer.Displayer;
import com.sunshine.view.library.listener.LoadListener;

/**
 * Created by Administrator on 2016/11/1.
 */

public class LoaderConfigure {
    private int loading = 0;//加载占位图资源id
    private int error = 0;//加载失败展位图资源id
    private boolean memoryCache = true;//是否需要内存缓存
    private boolean diskCache = true;//是否需要本地缓存
    private boolean adjust = false;//是否需要按控件大小裁剪
    private boolean cacheBaseImage = true;//是否缓存原图
    private Displayer mDisplayer;//图片加载器
    private LoadListener listener;
    public Type type = Type.LIFO;
    public LoaderConfigure() {
    }
    public LoaderConfigure(LoadListener listener) {
        this.listener = listener;
    }
    public LoaderConfigure(Displayer displayer) {
        this.mDisplayer = displayer;
    }


    public LoadListener getLoadListener() {
        return listener;
    }

    public LoaderConfigure loadListener(LoadListener listener) {
        this.listener = listener;
        return this;
    }

    public Displayer getDisplayer() {
        return mDisplayer;
    }

    public LoaderConfigure displayer(Displayer displayer) {
        this.mDisplayer = displayer;
        return this;
    }

    public LoaderConfigure loadType(Type type) {
        this.type = type;
        return this;
    }

    public LoaderConfigure cacheBaseImage(boolean cache) {
        cacheBaseImage = cache;
        return this;
    }

    public LoaderConfigure adjust(boolean cache) {
        adjust = cache;
        return this;
    }

    public LoaderConfigure memoryCache(boolean cache) {
        memoryCache = cache;
        return this;
    }

    public LoaderConfigure diskCache(boolean cache) {
        diskCache = cache;
        return this;
    }

    public boolean isMemoryCache() {
        return memoryCache;
    }

    public boolean isDiskCache() {
        return diskCache;
    }

    public boolean isAdjust() {
        return adjust;
    }

    public boolean isCacheBaseImage() {
        return cacheBaseImage;
    }


    public LoaderConfigure loading(int res) {
        loading = res;
        return this;
    }

    public LoaderConfigure error(int res) {
        error = res;
        return this;
    }

    public int getError() {
        return error;
    }

    public int getLoading() {
        return loading;
    }
}
