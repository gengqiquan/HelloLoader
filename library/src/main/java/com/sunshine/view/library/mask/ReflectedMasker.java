package com.sunshine.view.library.mask;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;

/**
 * Created by Administrator on 2016/11/14.
 */

public class ReflectedMasker implements Masker {

    @Override
    public Bitmap mask(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        Matrix mMatrix= new Matrix();
            // 图片缩放，x轴变为原来的1倍，y轴为-1倍,实现图片的反转
       mMatrix.preScale(1, -1);

        //创建反转后的图片Bitmap对象，图片高是原图的一半。
        //Bitmap mInverseBitmap = Bitmap.createBitmap(mBitmap, 0, height/2, width, height/2, matrix, false);
        //创建标准的Bitmap对象，宽和原图一致，高是原图的1.5倍。
        //注意两种createBitmap的不同
        //Bitmap mReflectedBitmap = Bitmap.createBitmap(width, height*3/2, Config.ARGB_8888);

        Bitmap mInverseBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, mMatrix, false);
        Bitmap mReflectedBitmap = Bitmap.createBitmap(width, height * 2, Bitmap.Config.ARGB_8888);

        // 把新建的位图作为画板
        Canvas mCanvas = new Canvas(mReflectedBitmap);
        //绘制图片
        mCanvas.drawBitmap(bm, 0, 0, null);
        mCanvas.drawBitmap(mInverseBitmap, 0, height, null);

        //添加倒影的渐变效果
        Paint mPaint = new Paint();
        Shader mShader = new LinearGradient(0, height, 0, mReflectedBitmap.getHeight(), 0x70ffffff, 0x00ffffff, Shader.TileMode.MIRROR);
        mPaint.setShader(mShader);
        //设置叠加模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //绘制遮罩效果
        mCanvas.drawRect(0, height, width, mReflectedBitmap.getHeight(), mPaint);

        return mReflectedBitmap;
    }
}
