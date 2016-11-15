package com.sunshine.view.helloloader;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sunshine.view.library.HelloLoader;
import com.sunshine.view.library.LoaderConfigure;
import com.sunshine.view.library.dispalyer.MaskDisplayer;
import com.sunshine.view.library.listener.LoadListener;
import com.sunshine.view.library.mask.PorterDuffMasker;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar progressBar;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);
        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final PhotoViewAttacher attacher = new PhotoViewAttacher(mImageView);
        LoaderConfigure configure = new LoaderConfigure()
       .displayer(new MaskDisplayer(new PorterDuffMasker(getResources(),R.mipmap.ic_launcher,PorterDuff.Mode.DST_IN)));
        configure.setLoadListener(new LoadListener() {
            @Override
            public void started() {
                Log.e("image","started");
            }

            @Override
            public void completed() {
                attacher.update();
                Log.e("image","completed");
                progressBar.setVisibility(View.GONE);
            }
        });
        HelloLoader.bind(mImageView).LoaderConfigure(configure).load(mImageUrl);
    }
}
