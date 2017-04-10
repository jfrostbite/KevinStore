package appstore.keivn.jf.kevinstore.bean;

import java.util.ArrayList;

/**
 * Created by Kevin on 2016/3/8.
 * 分类模块封装bean
 */
public class CategoryBean {

    public ArrayList<Apps> all;

    public static class Apps {

        public ArrayList<Group> infos;
        public static class Group {
            public String title;
            public boolean isTitle;

            public String name1;//出行,
            public String name2;//通讯,
            public String name3;//拍照,
            public String url1;//image/category_app_12.jpg,
            public String url2;//image/category_app_13.jpg,
            public String url3;//image/category_app_14.jpg
        }
    }
}
