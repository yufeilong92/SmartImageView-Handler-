package com.example.androidimglook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private EditText et_img;
    private ImageView imageView;
    private Button btn_img;
    protected final int REQUESTSUCCESS = 0;
    protected final int REQUESTSUCCESSNOT = 1;
    protected final int REQUESTSUCCESSNOTS = 1;
    //        []创建handler
//    private Handler handler = new Handler() {
//        @Override
//
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case REQUESTSUCCESS:
//                    Bitmap bitmap = (Bitmap) msg.obj;
//                    imageView.setImageBitmap(bitmap);
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_img = (EditText) findViewById(R.id.et_img);
        btn_img = (Button) findViewById(R.id.btn_img);
        imageView = (ImageView) findViewById(R.id.i_vimage);
    }

    public void click(View v) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String path = et_img.getText().toString().trim();

                    //        []base64加密
//                  File file1 = new File(getCacheDir(),"test.png");
                    File file = new File(getCacheDir(), Base64.encodeToString(path.getBytes(), Base64.DEFAULT));
                    //        []判断其是否是存在和为空
                    if (file.exists() && file.length() > 0) {
                        System.out.println("使用缓存图片");
//                    通过Bitmap   拿到缓存图片路径

                        final Bitmap cacheBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(cacheBitmap);
                            }
                        });


                        //        []把cacheBitmap显示到iv上
//                      Mesags的静态方法代替了new Message
//                        Message obtain = Message.obtain();
//                        obtain.obj = cacheBitmap;
//                        obtain.what = REQUESTSUCCESS;
//                        handler.sendMessage(obtain);
                    } else {
                        System.out.println("第一次访问网络");

                        //        [2.1]获取路径
//              String path = et_img.getText().toString().trim();
                        //        [2.2]创建URL


                        URL url = new URL(path);
                        //        [2.3]获取HttpURLConnection
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        //        [2.4]设置请求方式
                        conn.setRequestMethod("GET");
                        //        [2.5]浏览器返回请求
                        //        [2.5.0]设置超时时间
                        conn.setConnectTimeout(5000);
                        int code = conn.getResponseCode();
                        if (code == 200) {
                            //        [2.6]得到请求资源不管什么数据都是流的形式
                            InputStream inputStream = conn.getInputStream();
                            //        [2.6.1]缓存图片
//                      谷歌提供的一个方法getCacheDir的到目录
//                      File file=new File(getCacheDir(),"test.png");
                            FileOutputStream fos = new FileOutputStream(file);
                            int len = -1;
                            byte[] buffer = new byte[1024];
                            while ((len = inputStream.read(buffer)) != -1) {
                                fos.write(buffer, 0, len);
                            }
                            fos.close();
                            inputStream.close();
                            //        [2.7]把资源转换成字符串【位图bitmap】
                            //        []通过位图工厂获取bitmap
//                      Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                            final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                            不管你在什么位置调用action 都运行在ui线程里
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //        []run方法一定在ui线程里
                                    //        []bitmap显示iv上
                                    imageView.setImageBitmap(bitmap);
                                }
                            });

//                      Message message =Message.obtain();
//                      message.what=REQUESTSUCCESS;
//                      message.obj=bitmap;
//                      handler.sendMessage(message);
                        } else {
//                      使用msg静态方法
//                            Message message = Message.obtain();
//                            message.what = REQUESTSUCCESSNOT;
//                            handler.sendMessage(message);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"系统更新",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
               /*     Message message = new Message();
                    message.what = REQUESTSUCCESSNOTS;
                    handler.sendMessage(message);*/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"系统繁忙",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }.start();
    }

}
