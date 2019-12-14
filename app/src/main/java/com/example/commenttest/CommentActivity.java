package com.example.commenttest;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    private String news_id;
    private String string_allnum;
    private String long_comment_num = "1";
    private String short_comment_num = "23";
    private List<Map<String, Object>> list1 = new ArrayList<>();
    //    private String newsname;
//    private String images;
//    private String url;
    private TextView text_allnum;
    private ImageButton comment_back;
    private Map map;
    private RecyclerView recyclerView;


    List<Map<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = getIntent();
//        news_id = intent.getStringExtra("news_id");
//        string_allnum = intent.getStringExtra("allnum");
        news_id = "9717889";
        string_allnum = "9";
//        newsname = intent.getStringExtra("newsname");
//        url = intent.getStringExtra("url");
//        images = intent.getStringExtra("images");

        recyclerView = findViewById(R.id.comment_recyclerview);
//        comment_back = findViewById(R.id.comment_back);
        text_allnum = findViewById(R.id.comment_allnum);
        text_allnum.setText(string_allnum);

//        //返回功能
//        comment_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CommentActivity.this, ContentActivity.class);
//                intent.putExtra("id", news_id);
//                intent.putExtra("url", url);
//                intent.putExtra("newsname", newsname);
//                intent.putExtra("images", images);
//                startActivity(intent);
//                finish();
//            }
//        });
        if ( !long_comment_num.equals("0")) {
            map = new HashMap();
            map.put("cheak", "1");
            map.put("item", long_comment_num);
            list.add(map);
        }

//        if (!short_comment_num.equals("0")) {
//            map = new HashMap();
//            map.put("cheak", "2");
//            map.put("item", long_comment_num);
//            list.add(map);
//        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    //获取长评
                    URL url = new URL("https://news-at.zhihu.com/api/4/story/" + news_id + "/long-comments");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponseLong(response.toString());

                    //短评
                    URL url1 = new URL("https://news-at.zhihu.com/api/4/story/" + news_id + "/short-comments");
                    connection = (HttpURLConnection) url1.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in1 = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in1));
                    StringBuilder response1 = new StringBuilder();
                    String line1;
                    while ((line1 = reader.readLine()) != null) {
                        response1.append(line1);
                    }
                    showResponseShort(response1.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });

        thread.start();
    }

    //长评的
    public void showResponseLong(final String string) {

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
//            int ii = 0;
//            if (jsonArray.length() != 0 && ii == 0) {
//                ii = 1;
//                map = new HashMap();
//                map.put("cheak", "1");
//                map.put("item", long_comment_num);
//                list.add(map);
//            }
            for (int i = 0; i < jsonArray.length(); i++) {
//                if (i == -1) {
//                    map = new HashMap();
////                    map.put("cheak", "1");
////                    map.put("item", long_comment_num);
////                    list.add(map);
//                } else {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                int news_id = jsonObject1.getInt("news_id");
                    String author = jsonObject1.getString("author");
                    String avatar = jsonObject1.getString("avatar");  //头像
                    String content = jsonObject1.getString("content");
                    String likes = jsonObject1.getString("likes");
//                String news_id = jsonObject1.getString("id");


                    map = new HashMap();

//                map.put("news_id",news_id);
                    map.put("author", author);
                    map.put("avatar", avatar);
                    map.put("content", content);
                    map.put("likes", likes);
                    map.put("cheak", "0");
                    map.put("item", "0");

//                map.put("id", news_id);

                    list.add(map);
//                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));//纵向
                    recyclerView.setAdapter(new CommentAdapter(CommentActivity.this, list));
                    recyclerView.setNestedScrollingEnabled(false);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //长评的
    public void showResponseShort(final String string) {


        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
//            int iii = 0;
//            if (jsonArray.length() != 0 && iii == 0) {
//                iii = 1;
//                map = new HashMap();
//                map.put("cheak", "2");
//                map.put("item", short_comment_num);
//                list.add(map);
//            }
            if (!short_comment_num.equals("0")) {
                map = new HashMap();
                map.put("cheak", "2");
                map.put("item", short_comment_num);
                list.add(map);
            }

            for (int i = 0; i < jsonArray.length(); i++) {
//                if (i == -1) {
//                    map = new HashMap();
//                    map.put("cheak", "2");
//                    map.put("item", long_comment_num);
//                    list.add(map);
//                } else {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String author = jsonObject1.getString("author");
                    String avatar = jsonObject1.getString("avatar");  //头像
                    String content = jsonObject1.getString("content");
                    String likes = jsonObject1.getString("likes");
//                String news_id = jsonObject1.getString("id");


                    map = new HashMap();

//                map.put("news_id",news_id);
                    map.put("author", author);
                    map.put("avatar", avatar);
                    map.put("content", content);
                    map.put("likes", likes);
//                map.put("id", news_id);
                    map.put("cheak", "0");
                    map.put("item", "0");

                    list.add(map);
//                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));//纵向
                    recyclerView.setAdapter(new CommentAdapter(CommentActivity.this, list));
                    recyclerView.setNestedScrollingEnabled(false);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
