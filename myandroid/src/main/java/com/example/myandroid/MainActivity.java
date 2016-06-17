package com.example.myandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<NewXml> newList;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //        [1]找到控件
        lv = (ListView) findViewById(R.id.lv);
     //        [2]准备listview要显示的数据，封装数据
     initListData();
    }

    private void initListData() {
        //[☆]去服务器取数据http://192.168.1:8080/new.xml
      new Thread(new Runnable() {
          @Override
          public void run() {
              try {
//                  获取要解析的xml文件路径
                  String path="";
                  //[☆]获取路径
                  URL url = new URL(path);
                  //[☆]h获取HttpURLConnection对象，用于发送解说数据
                  HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                  //[☆]设置请求头
                  conn.setRequestMethod("GET");
                  //[☆]设置时间
                  conn.setConnectTimeout(5000);
                  //[☆]获取浏览器响应
                  int code = conn.getResponseCode();
                  //[☆]判断其值
                  if(code==200){
                      //[☆]获取服务器返回数据，以流形式返回，把流转换字符串
                      InputStream in=conn.getInputStream();
                      //[☆]解析xml 抽取方法 返回一个集合
                      newList = XmlParserUtils.parserXml(in);

                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          //[☆]跟新UI 把数据展示到ListView
                          lv.setAdapter(new MyAdapter());
                      }
                  });

                  }
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
      }).start();
    }
    //[☆]配置数据识别器
    private  class  MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return newList.size() ;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           View view;
            if (convertView==null){
                //[☆]获得打气筒发法
               view =View.inflate(getApplicationContext(),R.layout.item,null);
           }else {
                view=convertView;
            }
               //[☆]找到控件，显示集合数据
            //[☆]导入SmartImageView包
            //[☆]在activity中设置<com.loopj.android.image.SmartImageView
            SmartImageView  iv_icon=  (SmartImageView)view.findViewById(R.id.iv_icon);
            TextView  tv_desc= (TextView) view.findViewById(R.id.tv_desc);
            TextView tv_type= (TextView) view.findViewById(R.id.tv_type);
            TextView tv_title= (TextView) findViewById(R.id.tv_title);
           //[☆.1]展示图片的数据
            String image = newList.get(position).getImage();
            iv_icon.setImageUrl(image);
            //[☆]显示数据(position代表其位置)

            tv_title.setText(newList.get(position).getTitle());
            tv_desc.setText(newList.get(position).getDexcription());
//            tv_type.setText(newList.get(position).getType());
            String type = newList.get(position).getType();
            String comment = newList.get(position).getComment();
            int i = Integer.parseInt(type);
            switch (i) {
                case 1:
                    tv_type.setText(comment+"国内");
                break;
                case 2:
                    tv_type.setText(comment+"跟帖");
                    break;
                case 3:
                    tv_type.setText("国外");
                    break;
            }

            return view;
        }
    }

}
