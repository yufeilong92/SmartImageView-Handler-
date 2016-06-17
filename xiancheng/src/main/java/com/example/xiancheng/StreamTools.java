package com.example.xiancheng;

import java.io.ByteArrayOutputStream;

import java.io.InputStream;

/**
 * Created by Administrator on 2016/6/14.
 */
public class StreamTools {
    //[1]把inoutStrean 转换成String
    public static String ReadStream(InputStream in) {
        //[2]d定义个内存输出流
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //[4] 定义个常量
            int len = -1;
            byte[] bytes = new byte[1024];
//[4.1]判断其是否为空

            while ((len = in.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            in.close();
            /**
             *  byte[] toByteArray()
             创建一个新分配的 byte 数组。
             */
            String content = new String(baos.toByteArray());
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //[3]
}
