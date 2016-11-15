package com.sunshine.view.library.mask;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.sunshine.view.library.utils.ImageUtil;

/**
 * Created by Administrator on 2016/11/14.
 */

public class PorterDuffMasker implements Masker {
    int shadeID;
    Resources res;
    PorterDuff.Mode mode;

    public PorterDuffMasker(Resources res, int shadeID, PorterDuff.Mode mode) {
        this.shadeID = shadeID;
        this.res = res;
        this.mode = mode;
    }


    @Override
    public Bitmap mask(Bitmap bm) {
        //如果不用copy的方法，直接引用会对资源文件进行修改，而Android是不允许在代码里修改res文件里的图片
        Bitmap mShadeBitmap = BitmapFactory.decodeResource(res, shadeID);
        mShadeBitmap = ImageUtil.scaleImg(mShadeBitmap,bm.getWidth(), bm.getHeight());
        bm = bm.copy(Bitmap.Config.ARGB_8888, true);
        Canvas mCanvas = new Canvas(bm);
        Paint mPaint = new Paint();
        mPaint.setXfermode(new PorterDuffXfermode(mode));
        mCanvas.drawBitmap(mShadeBitmap, 0, 0, mPaint);
        return bm;
    }
}
