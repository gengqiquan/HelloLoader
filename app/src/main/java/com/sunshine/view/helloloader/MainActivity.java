package com.sunshine.view.helloloader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.toolbox.Volley;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    HelloLoader loader;
    LoaderConfigure loaderConfigure;

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
                .loading(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher);
        loader = new HelloLoader
                .Builder(this)
                .downloader(new VolleyDownLoader(Volley.newRequestQueue(this)))
//                .downloader(new Okhttp3DownLoader(client))
                .defaultLoaderConfigure(loaderConfigure)
               .displayer(new FadeInImageDisplayer(2000))
                .build();
        GridView listView = (GridView) findViewById(R.id.list);
        final myAdapter adapter = new myAdapter();
        listView.setAdapter(adapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String str = doRequest("http://www.tngou.net/tnfs/api/list?rows=50&page=7");
                if (str != null) {
                    try {
                        JSONObject obj = new JSONObject(str);
                        JSONArray array = obj.getJSONArray("tngou");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            list.add("http://tnfs.tngou.net/image" + object.getString("img") + "_300x300");
                        }
                    } catch (JSONException e) {

                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
//        for (int i = 0; i < 50; i++) {
//            list.add("/storage/emulated/0/Tencent/QQ_Images/a.jpg");
//        }
//        adapter.notifyDataSetChanged();
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

    ArrayList<String> list = new ArrayList<>();

    class myAdapter extends BaseAdapter {
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item, null);
                holder = new ViewHolder(convertView.findViewById(R.id.img));
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            loader.bind(holder.imageView).load(list.get(position));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ImagePagerActivity.class);
                    // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, list);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                    startActivity(intent);
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }


    }

    class ViewHolder {
        public ViewHolder(View view) {
            this.imageView = (ImageView) view;
        }

        public ImageView imageView;
    }
}
