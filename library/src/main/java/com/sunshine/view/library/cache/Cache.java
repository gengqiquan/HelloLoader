package com.sunshine.view.library.cache;

/**
 * Created by Administrator on 2016/11/1.
 */

import android.graphics.Bitmap;

public interface Cache {
    /** Retrieve an image for the specified {@code key} or {@code null}. */
    Bitmap get(String key);
    /** Store an image in the cache for the specified {@code key}. */
    Bitmap put(String key, Bitmap bitmap);
}