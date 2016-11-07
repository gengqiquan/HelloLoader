package com.sunshine.view.library.data;

import android.widget.ImageView;

/**
 * Created by Administrator on 2016/11/2.
 */

public class TaskInfo {
    ImageView imageView;
    Runnable runnable;

    public TaskInfo(ImageView imageView, Runnable runnable) {
        this.imageView = imageView;
        this.runnable = runnable;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskInfo taskInfo = (TaskInfo) o;
        return imageView != null ? imageView.getTag().equals(taskInfo.imageView.getTag()) : taskInfo.imageView == null;
    }


}
