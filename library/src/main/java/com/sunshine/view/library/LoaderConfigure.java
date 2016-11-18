package com.sunshine.view.library;

import com.sunshine.view.library.data.Type;
import com.sunshine.view.library.dispalyer.Displayer;
import com.sunshine.view.library.listener.LoadListener;

/**
 * Created by Administrator on 2016/11/1.
 */

public class LoaderConfigure {
    public int loading = 0;//加载占位图资源id
    public int error = 0;//加载失败展位图资源id
    int width = -1;//图片目标宽度
    int height = -1;//图片目标高度
    public boolean roundBitmap = false;//是否圆形图片
    public boolean memoryCache = true;//是否需要内存缓存
    public boolean diskCache = true;//是否需要本地缓存
    public boolean adjust = true;//是否需要按控件大小裁剪
    public boolean cacheBaseImage = false;//是否只缓存原图
    private Displayer mDisplayer;//图片加载器
    LoadListener listener;
    public Type type = Type.FIFO;

    public LoaderConfigure(LoadListener listener) {
        this.listener = listener;
    }

    public LoaderConfigure() {
    }

    public LoaderConfigure(Displayer displayer) {
        this.mDisplayer = displayer;
    }


    public LoadListener getLoadListener() {
        return listener;
    }

    public LoaderConfigure setLoadListener(LoadListener listener) {
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

    public LoaderConfigure roundBitmap(boolean round) {
        roundBitmap = round;
        return this;
    }

    public LoaderConfigure loading(int res) {
        loading = res;
        return this;
    }

    public LoaderConfigure error(int res) {
        error = res;
        return this;
    }

    public LoaderConfigure size(int w, int h) {
        if (w > 0 && h > 0) {
            width = w;
            height = h;
        }
        return this;
    }
}
