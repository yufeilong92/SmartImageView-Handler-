package com.example.xiancheng;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText et_path;
    private TextView tv_ewsuilt;
    protected static final int REQYESTSYCESS = 0;
    protected static final int REQYESTSYCESSNOT = 1;
    protected static final int REQUESTSYCESSNOTS=2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//         super.handleMessage(msg);
            //        [8.1]把msg转换成String
            //        [8.2]区分发送消息
            switch (msg.what) {
                case REQYESTSYCESS:
//                 请求成功
                    String content = (String) msg.obj;
                    //        [8.2]主线程更新ui
                    tv_ewsuilt.setText(content);
                    break;
                case REQYESTSYCESSNOT:
                    Toast.makeText(getApplicationContext(), "请求资源不存在", Toast.LENGTH_SHORT).show();
                    break;
                case REQUESTSYCESSNOTS:
                    Toast.makeText(getApplicationContext(), "服务器忙，请稍后", Toast.LENGTH_SHORT).show();

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_ewsuilt = (TextView) findViewById(R.id.tv_result);
        et_path = (EditText) findViewById(R.id.et_path);
        //        [1]得到路径
        System.out.println("当前线程名字" + Thread.currentThread().getName());

    }

    public void click(View v) {
        new Thread() {
            public void run() {
                String path = et_path.getText().toString();
                //        [2]给定一个url 路径
                try {
                    URL url = new URL(path);
                    //        [3]拿到网址的大httpUrlconnection 对象，发送、接收数据
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    //        [4]设置请求头
                    con.setRequestMethod("GET");
                    //        [5]设置睡眠时间
                    con.setConnectTimeout(5000);
                    //        [6]获取请求内容
                    int code = con.getResponseCode();
                    if (code == 200) {
                        /**
                         * 把服务器的内容转换成字符串
                         */
                        //        [6.1]获得服务器返回的数据以流的形式
                        InputStream in = con.getInputStream();
                        //        [6.2]使用工具装换字符串
                        String content = StreamTools.ReadStream(in);
                        /**
                         * 拿着我们创建的 告诉ui
                         */
                        //        [6.2]创建msg
                        Message msg = new Message();
                        msg.what = REQYESTSYCESS;
                        //        [6.3]发送消息
                        msg.obj = content;
                        //        [6.4]把消息发送到handler 中条到主线程
                        handler.sendMessage(msg);
                        //        [6.3]把流里面的数据展示带Tiew上等于更新ui
//                 tv_ewsuilt.setText(s);
                    } else {
                        Message msg = new Message();
                        msg.what = REQYESTSYCESSNOT;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg=new Message();
                    msg.what=REQUESTSYCESSNOTS;
                    handler.sendMessage(msg);
                }

            }

            ;
        }.start();

    }
}
