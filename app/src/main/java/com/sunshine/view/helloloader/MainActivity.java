package com.sunshine.view.helloloader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gengqiquan.adapter.adapter.RBAdapter;
import com.gengqiquan.adapter.adapter.RPAdapter;
import com.gengqiquan.adapter.interfaces.Holder;
import com.gengqiquan.adapter.interfaces.PConverter;
import com.gengqiquan.library.SimpleRefreshLayout;
import com.sunshine.view.library.HelloLoader;
import com.sunshine.view.library.LoaderConfigure;
import com.sunshine.view.library.dispalyer.FadeInImageDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MainActivity extends FragmentActivity {
    HelloLoader loader;
    LoaderConfigure loaderConfigure;
    RPAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
        loaderConfigure = new LoaderConfigure()
                .memoryCache(true)
                .diskCache(true)
                //  .loading(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        loader = new HelloLoader
                .Builder(this)
//                .downloader(new VolleyDownLoader(Volley.newRequestQueue(this)))
//                .downloader(new Okhttp3DownLoader(client))
                .defaultLoaderConfigure(loaderConfigure)
//                .displayer(new FadeInImageDisplayer(500))
                .allowDiskThreadPool(true)
                .build();
        final RecyclerView type = (RecyclerView) findViewById(R.id.type);
        type.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false));
        List<String> tags = new ArrayList<>();
        tags.add("诱惑");
        tags.add("长腿");
        tags.add("性感");
        tags.add("写真");
        tags.add("气质");
        tags.add("车模");
        tags.add("唯美");
        tags.add("小清新");
        tags.add("清纯");
        tags.add("校花");
        tags.add("少女");
        tags.add("美腿");
        tags.add("内衣");
        tags.add("宅男女神");
        type.setAdapter(new RPAdapter<String>(this)
                .list(tags)
                .layout(R.layout.text_drag_thumbnail)
                .bindPositionData(new PConverter<String>() {
                    @Override
                    public void convert(Holder holder, final String url, final int position) {
                        TextView iv_img = (TextView) holder.getItemView();
                        iv_img.setText(url);
                        holder.getItemView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                page = 0;
                                tag = url;
                                load();

                            }
                        });
                    }
                }));
        final RecyclerView listView = (RecyclerView) findViewById(R.id.list);
        adapter = new RPAdapter<String>(this)
                .layout(R.layout.list_item)
                .bindPositionData(new PConverter<String>() {
                    @Override
                    public void convert(Holder holder, final String url, final int position) {
                        ImageView iv_img = (ImageView) holder.getItemView();
//                        loader.bind(iv_img).load(url);
                        Glide.with(MainActivity.this).load(url).into(iv_img);
                        holder.getItemView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, ImagePagerActivity.class);
                                // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) adapter.getList());
                                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                                startActivity(intent);
                            }
                        });
                    }
                });
        listView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        listView.setLayoutManager(layoutManager);
        load();
        final TextView tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                tv_next.setText("页码" + page);
                load();
            }
        });
    }

    int page = 0;
    String tag = "全部";

    private void load() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> list = new ArrayList<>();
                final String str = doRequest("http://image.baidu.com/channel/listjson?pn=" + page + "&rn=30&tag1=美女&tag2=" + tag + "&ie=utf8");
                if (str != null) {
                    try {
                        JSONObject obj = new JSONObject(str);
                        JSONArray array = obj.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            list.add(object.getString("image_url"));
                        }
                    } catch (JSONException e) {

                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (page == 0) {
                            adapter.appendList(list);
                        } else {
                            adapter.addList(list);
                        }
                    }
                });
            }
        }).start();
    }

    private String doRequest(String urlStr) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null)
                continue;

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value, "UTF-8"));
        }

        String str = result.toString();
        Log.d("DataLoader", "post params is " + str);
        return str;
    }


}
