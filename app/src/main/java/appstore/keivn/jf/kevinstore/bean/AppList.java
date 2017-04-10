package appstore.keivn.jf.kevinstore.bean;

import java.util.ArrayList;

/**
 * Created by Kevin on 2016/3/4.
 * 首页信息的类
 */
public class AppList {
    public ArrayList<String> picture;
    public ArrayList<AppInfo> list;
    public class AppInfo {
        public String id; //1525490,
        public String name; //"有缘网",
        public String packageName; //"com.youyuan.yyhl",
        public String iconUrl; //"app/com.youyuan.yyhl/icon.jpg",
        public String stars; //4,
        public String size; //3876203,
        public String downloadUrl; //"app/com.youyuan.yyhl/com.youyuan.yyhl.apk",
        public String des; //"产品介绍：有缘是时下最受大众单身男女亲睐的婚恋交友软件。有缘网专注于通过轻松、"

        public String author;// 亚马逊,
        public String date;// 2014-05-30,
        public String downloadNum;// 100万+,
        public String version;// 2.10.0

        public ArrayList<String> screen;
        public ArrayList<Safe> safe;

        public class Safe {
            public String safeUrl,safeDesUrl,safeDes,safeDesColor;
        }
    }

}
