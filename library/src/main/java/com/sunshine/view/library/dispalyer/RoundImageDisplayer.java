package com.sunshine.view.library.dispalyer;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.sunshine.view.library.data.ImageInfo;

/**
 * Created by Administrator on 2016/11/14.
 */


public class RoundImageDisplayer implements Displayer {
    protected final int cornerRadius;
    protected final int margin;

    public RoundImageDisplayer(int cornerRadiusPixels) {
        this(cornerRadiusPixels, 0);
    }

    public RoundImageDisplayer(int cornerRadiusPixels, int marginPixels) {
        this.cornerRadius = cornerRadiusPixels;
        this.margin = marginPixels;
    }

    public void display(Bitmap bitmap, ImageInfo info) {
        ImageView imageView = info.getImageView();
        imageView.setImageDrawable(new RoundImageDisplayer.RoundedDrawable(bitmap, this.cornerRadius, this.margin));

    }

    public static class RoundedDrawable extends Drawable {
        protected final float cornerRadius;
        protected final int margin;
        protected final RectF mRect = new RectF();
        protected final RectF mBitmapRect;
        protected final BitmapShader bitmapShader;
        protected final Paint paint;

        public RoundedDrawable(Bitmap bitmap, int cornerRadius, int margin) {
            this.cornerRadius = (float) cornerRadius;
            this.margin = margin;
            this.bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            this.mBitmapRect = new RectF((float) margin, (float) margin, (float) (bitmap.getWidth() - margin), (float) (bitmap.getHeight() - margin));
            this.paint = new Paint();
            this.paint.setAntiAlias(true);
            this.paint.setShader(this.bitmapShader);
        }

        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            this.mRect.set((float) this.margin, (float) this.margin, (float) (bounds.width() - this.margin), (float) (bounds.height() - this.margin));
            Matrix shaderMatrix = new Matrix();
            shaderMatrix.setRectToRect(this.mBitmapRect, this.mRect, Matrix.ScaleToFit.FILL);
            this.bitmapShader.setLocalMatrix(shaderMatrix);
        }

        public void draw(Canvas canvas) {
            canvas.drawRoundRect(this.mRect, this.cornerRadius, this.cornerRadius, this.paint);
        }

        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        public void setAlpha(int alpha) {
            this.paint.setAlpha(alpha);
        }

        public void setColorFilter(ColorFilter cf) {
            this.paint.setColorFilter(cf);
        }
    }
}
