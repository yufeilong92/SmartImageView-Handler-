package com.example.myandroid;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
/**
 * 一般这种读取循环的事务，源对象的读取函数被设计为：如果遇到数据流尾部返回-1，以通知调用者数据已读完。这样调用者通过返回值判断非-1进行循环，如果遇到-1则跳出循环。
 * <p/>
 * 如果源对象的读取函数始终不会返回-1，调用者将死循环，程序会卡死。在这里-1是一种约定，不过这有个前提，必须保证数据流中不包含-1。
 */

/**
 * Created by Administrator on 2016/6/16.
 */
public class StreamTools {
    public static String ReadStream(InputStream in) {
        try {
            //[☆]内存字节输出流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //[☆]定义常量
            int len = -1;
            //[☆]字节输出
            byte[] bytes = new byte[1024];
            //[☆]判断为空

            while ((len = in.read(bytes)) != -1) {
            //[☆]写入开始（内容，从什么开始，到什么结束）
              baos.write(bytes,0,len);
            }
            in.close();
            //[☆]新的的数据流
            String s = new String(baos.toByteArray());
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
