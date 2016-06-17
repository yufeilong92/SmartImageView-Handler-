package com.example.myandroid;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/6/16.
 */
public class XmlParserUtils {
    public static List<NewXml> parserXml(InputStream in) throws IOException, XmlPullParserException {
        //[☆]设置list集合
        List<NewXml> newlist=null;
        //[☆]创建解析对
        NewXml newXml=null;
            //[☆]获取解析Xml解析器
            XmlPullParser parser = Xml.newPullParser();
            //[☆]设置解析器要解析内容
            parser.setInput(in, "utf-8");
            //[☆]获取解析的事件类型
            int type = parser.getEventType();
            //[☆]不停向下解析
            while (type!=XmlPullParser.END_DOCUMENT){
                //[☆]判读解析开始还是结束
                switch (type) {
                    case XmlPullParser.START_TAG ://解析开始节点
                        //[☆]具体判断解析那个是开始标签
                       if ("channerl".equals(parser.getName())){
                           //[☆]创建一个list集合
                           newlist=new ArrayList<NewXml>();
                       }else  if("item".equals(parser.getName())){
                           newXml=new NewXml();
                       }else  if ("title".equals(parser.getName())){
                           newXml.setTitle(parser.nextText());

                       }else  if ("description".equals(parser.getName())){
                           newXml.setDexcription(parser.nextText());

                       }else  if ("image".equals(parser.getName())){
                           newXml.setImage(parser.nextText());

                       }else  if ("type".equals(parser.getName())){
                           newXml.setType(parser.nextText());

                       }else  if ("comment".equals(parser.getName())){
                           newXml.setComment(parser.nextText());

                       }

                        break;
                    //[☆]判断解析结束
                    case XmlPullParser.END_TAG://解析结束节点
                        if ("item".equals(parser.getName())){
                            //[☆]把javabean 添加到集合
                            newlist.add(newXml);
                        }
                    break;

                }
                type = parser.next();
            }
        return newlist;
    }
}
