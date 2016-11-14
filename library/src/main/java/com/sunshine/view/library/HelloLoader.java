package com.sunshine.view.library;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.sunshine.view.library.cache.Cache;
import com.sunshine.view.library.cache.LruCache;
import com.sunshine.view.library.data.ImageInfo;
import com.sunshine.view.library.data.LoadedFrom;
import com.sunshine.view.library.data.ResponseInfo;
import com.sunshine.view.library.data.TaskInfo;
import com.sunshine.view.library.dispalyer.SimpleImageDisplayer;
import com.sunshine.view.library.dispalyer.Displayer;
import com.sunshine.view.library.download.Downloader;
import com.sunshine.view.library.download.HttpUrlConnectionDownLoader;
import com.sunshine.view.library.utils.ImageSizeUtil;
import com.sunshine.view.library.utils.ImageUtil;
import com.sunshine.view.library.utils.Utils;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/11/1.
 */

public class HelloLoader {
    static volatile HelloLoader mInstance;
    Context mAppliactionContext;
    Cache cache;
    String mDiskCachePath;
    LoaderConfigure mDefaultConfigure;//默认的全局图片加载配置
    Downloader mDownloader;//图片下载器
    LinkedList<TaskInfo> mTaskQueue;//解析请求队列
    ExecutorService mThreadPool;// 线程池
    int threadCount = 4;
    Handler mPoolThreadHandler;//线程池循环句柄
    Thread mPoolThread;//后台任务调度线程
    Type mType = Type.LIFO;//请求加载顺序：正序还是倒序

    public enum Type {
        FIFO, LIFO;
    }

    //主线程设置图片句柄
    @SuppressLint("HandlerLeak")
    private Handler UIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ImageInfo imageInfo = (ImageInfo) msg.obj;
                    Bitmap bm = imageInfo.getBitmap();
                    LoaderConfigure configure = imageInfo.getLoaderConfigure();
                    ImageView imageView = imageInfo.getImageView();
                    if (imageView.getTag() != null && imageView.getTag().equals(imageInfo.getTag())) {
                        if (bm == null) {
                            if (configure.error > 0) {
                                bm = ImageUtil.ReadBitmapById(mAppliactionContext, configure.error);
                            }
                        }
                        if (bm != null) {
                            bm = ImageUtil.configureImage(bm, imageView, configure);
                            configure.getDisplayer().display(bm, imageInfo);
                        }
                    }
                    break;
            }
        }

    };

    public HelloLoader(Context mAppliactionContext, Cache cache, String mDiskCachePath, LoaderConfigure mDefaultConfigure
            , Downloader downloader, Displayer displayer) {
        this.mAppliactionContext = mAppliactionContext;
        this.cache = cache;
        this.mDiskCachePath = mDiskCachePath;
        this.mDefaultConfigure = mDefaultConfigure;
        this.mDownloader = downloader;
        this.mDefaultConfigure.setDisplayer(displayer);
        mTaskQueue = new LinkedList<>();
        threadCount = Runtime.getRuntime().availableProcessors();
        mThreadPool = Executors.newFixedThreadPool(threadCount + 1);
        initBackThread();
    }

    public static LoaderBuilder bind(ImageView imageView) {
        if (imageView == null)
            throw new NullPointerException("imageView can not be null");
        if (mInstance == null) {
            mInstance = new Builder(imageView.getContext()).build();
        }
        return new LoaderBuilder(imageView, mInstance);
    }

    private Bitmap memoryCacheCheck(String key) {
        Bitmap cached = cache.get(key);
        return cached;
    }

    private Bitmap put2MemoryCache(String key, Bitmap bitmap) {
        if (key == null) {
            throw new NullPointerException("cache key can not be null");
        }
        if (bitmap == null) {
            throw new NullPointerException("the bitmap put to cache is null");
        }
        Bitmap cached = cache.put(key, bitmap);
        return cached;
    }

    /**
     * 初始化后台轮询线程
     */
    private void initBackThread() {
        mPoolThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 线程池去取出一个任务进行执行
                        Runnable runnable = getTask();
                        if (runnable != null) {
                            mThreadPool.execute(runnable);
                        }
                    }

                };
                Looper.loop();
            }
        };

        mPoolThread.start();
    }

    protected void runTask() {
        mPoolThreadHandler.sendEmptyMessage(0);
    }

    private Runnable getTask() {
        synchronized (mTaskQueue) {
            if (mTaskQueue.isEmpty())
                return null;
            if (mType == Type.FIFO) {
                return mTaskQueue.removeFirst().getRunnable();
            } else if (mType == Type.LIFO) {
                return mTaskQueue.removeLast().getRunnable();
            }
        }
        return null;
    }


    protected void removeTask(ImageView imageView) {
        synchronized (mTaskQueue) {
            mTaskQueue.remove(new TaskInfo(imageView, null));
        }
    }

    /**
     * 从内存取，，取到直接加载。因为不占据请求和解析资源，而且刚刚加载过的图片滑动过来再次加载也不合理
     * 没取到则放入解析和请求队列
     *
     * @date 2016/11/3 9:45
     */
    @NonNull
    public void putTask(final Context context, final LoaderConfigure configure, final ImageView imageView, final String uri) {
        //加时间戳，防止列表加载的时候有相同路径的多张图片重复加载错乱问题
        final String key = Utils.md5(uri);
        final String tag = key + System.currentTimeMillis();
        Bitmap bm = null;
        bm = memoryCacheCheck(key);
        if (bm != null) {
            imageView.setTag(tag);
            ImageInfo imageInfo = new ImageInfo(imageView, tag, bm, configure, LoadedFrom.MEMORY_CACHE);
            Message msg = new Message();
            msg.what = 0;
            msg.obj = imageInfo;
            UIHandler.sendMessageAtFrontOfQueue(msg);//内存找到优先直接加载。速度快
        } else {
            if (imageView.getTag() != null) {
                removeTask(imageView);//顺序不能错，先从任务队列移除这个imageView之前所绑定的任务
            }
            imageView.setTag(tag);
            Runnable runnable;
            if (uri.startsWith("http")) {
                runnable = getNetImageRunnable(context, configure, imageView, uri, tag, key);
            } else {
                runnable = getLocalImageRunnable(context, configure, imageView, uri, tag, key);
            }
            TaskInfo info = new TaskInfo(imageView, runnable);
            synchronized (mTaskQueue) {
                if (mTaskQueue.contains(info)) {
                    mTaskQueue.remove(info);
                    mTaskQueue.add(info);
                } else {
                    mTaskQueue.add(info);
                }
            }
        }
    }

    @NonNull
    public Runnable getNetImageRunnable(final Context context, final LoaderConfigure configure, final ImageView imageView, final String url, final String tag, final String key) {
        return new Runnable() {

            @Override
            public void run() {
                // TODO: 这里其实应该加入优先级。本地如果有这个文件，应该优先级高一点，或者额外给个本地解析任务队列
                Bitmap bm = getBitmapFromDiskCache(context, key, imageView);
                LoadedFrom from = LoadedFrom.DISC_CACHE;
                if (bm == null) {
                    ResponseInfo responseInfo = mInstance.mDownloader.downloadImgByUrl(url);
                    if (responseInfo.success)// 如果下载成功
                    {
                        bm = dealImage(configure, imageView, key, responseInfo.bitmap);
                        from = LoadedFrom.NETWORK;
                    }
                }
                ImageInfo imageInfo = new ImageInfo(imageView, tag, bm, configure, from);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = imageInfo;
                UIHandler.sendMessage(msg);
            }


        };
    }

    @NonNull
    public Runnable getLocalImageRunnable(final Context context, final LoaderConfigure configure, final ImageView imageView, final String path, final String tag, final String key) {
        return new Runnable() {

            @Override
            public void run() {
                Bitmap bm = getBitmapFromDiskCache(context, key, imageView);
                LoadedFrom from = LoadedFrom.DISC_CACHE;
                if (bm == null) {
                    bm = getBitmapFromDisk(context, path, imageView);
                    if (bm != null) {
                        bm = dealImage(configure, imageView, key, bm);
                        from = LoadedFrom.DISC;
                    }
                }
                ImageInfo imageInfo = new ImageInfo(imageView, tag, bm, configure, from);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = imageInfo;
                UIHandler.sendMessage(msg);
            }


        };
    }

    private Bitmap getBitmapFromDiskCache(Context mContext, String key, ImageView imageView) {
        Bitmap bm = null;
        File file = Utils.getDiskCacheDir(mInstance.mDiskCachePath, key);
        if (file.exists())// 如果在本地缓存文件中发现
        {
            ImageSizeUtil.ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
            // 根据控件大小获取本地压缩处理后的图片
            bm = ImageUtil.decodeSampledBitmapFromPath(file.getAbsolutePath(), imageSize);
        }
        return bm;
    }

    private Bitmap getBitmapFromDisk(Context mContext, String path, ImageView imageView) {
        Bitmap bm = null;
        if (!Utils.checkNULL(path))// 如果文件路径不为空
        {
            ImageSizeUtil.ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
            // 根据控件大小获取本地压缩处理后的图片
            bm = ImageUtil.decodeSampledBitmapFromPath(path, imageSize);
        }
        return bm;
    }

    private Bitmap dealImage(LoaderConfigure configure, ImageView imageView, String key, Bitmap bm) {
        if (configure.cacheBaseImage || !configure.adjust) {//缓存原图,或者不压缩的情况下
            putIntoCache(configure, key, bm);
        }
        if (configure.adjust) {//是否根据控件大小压缩图片大小
            bm = ImageUtil.scaleImg(bm, imageView.getWidth(), imageView.getHeight());
            if (!configure.cacheBaseImage) {//缓存压缩后图片
                putIntoCache(configure, key, bm);
            }
        }
        return bm;
    }

    private void putIntoCache(LoaderConfigure configure, String key, Bitmap bm) {
        if (configure.memoryCache) {
            put2MemoryCache(key, bm);//放入内存缓存
        }
        if (configure.diskCache) {
            Utils.write2File(mInstance.mDiskCachePath, bm, key);//写入本地文件
        }
    }

    public static class Builder {
        Context mContext;
        LoaderConfigure mDefaultConfigure;
        Cache mCache;
        String mDiskCachePath;
        Downloader mDownloader;
        Displayer mDisplayer;

        public Builder(Context context) {
            try {//防止传入的是activity的上下文
                Activity activity = (Activity) context;
                this.mContext = context.getApplicationContext();
            } catch (Exception e) {
                e.printStackTrace();
                this.mContext = context;
            }
        }

        public Builder defaultLoaderConfigure(LoaderConfigure loaderConfigure) {
            this.mDefaultConfigure = loaderConfigure;
            return this;
        }

        public Builder downloader(Downloader downloader) {
            this.mDownloader = downloader;
            return this;
        }

        public Builder displayer(Displayer displayer) {
            this.mDisplayer = displayer;
            return this;
        }

        public Builder cache(Cache cache) {
            this.mCache = cache;
            return this;
        }

        public Builder diskCachePath(String loaderConfigure) {
            this.mDiskCachePath = loaderConfigure;
            return this;
        }

        public HelloLoader build() {
            if (this.mDefaultConfigure == null) {
                this.mDefaultConfigure = new LoaderConfigure();
            }
            if (this.mCache == null) {
                this.mCache = createDefaultCache();
            }
            if (Utils.checkNULL(this.mDiskCachePath)) {
                this.mDiskCachePath = this.mContext.getCacheDir().getPath();
            }
            if (this.mDownloader == null) {
                this.mDownloader = createDefaultDownloader();
            }
            if (this.mDisplayer == null) {
                this.mDisplayer = createDefaultDisplayer();
            }
            mInstance = new HelloLoader(this.mContext, this.mCache, this.mDiskCachePath, this.mDefaultConfigure, this.mDownloader, this.mDisplayer);
            return mInstance;
        }

        private Displayer createDefaultDisplayer() {
            Displayer dispalyer = new SimpleImageDisplayer();
            return dispalyer;
        }

        private Downloader createDefaultDownloader() {
            return new HttpUrlConnectionDownLoader();
        }

        private Cache createDefaultCache() {
            int memoryCacheSize = Utils.getMemoryCacheSize(mContext);
            return new LruCache(memoryCacheSize);
        }
    }
}
