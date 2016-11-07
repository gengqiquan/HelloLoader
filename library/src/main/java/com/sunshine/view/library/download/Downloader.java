package com.sunshine.view.library.download;

import com.sunshine.view.library.data.ResponseInfo;

/**
 * Created by Administrator on 2016/11/2.
 */

public interface Downloader {
    ResponseInfo downloadImgByUrl(String urlStr);
}
