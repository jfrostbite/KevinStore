package appstore.keivn.jf.kevinstore.http.httputils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.bean.CategoryList;
import appstore.keivn.jf.kevinstore.bean.SubjectList;

/**
 * Created by Kevin on 2016/3/4.
 * 应用页面特有的请求网路的方式
 */
public class CategoryHttpRequest extends BaseHttpRequest<ArrayList<CategoryList>> {

    private int index;
    /*@Override
    protected String getVirtualDir() {
        return "home?";
    }*/

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    protected String getVirtualDir() {
        return "";
    }

    @Override
    protected String getKey() {
        return "category";
    }

    @Override
    protected String getValue() {
        return "";
    }

    @Override
    public ArrayList<CategoryList> processJson(String str) {

        /*ArrayList<CategoryList> categoryLists = new ArrayList<CategoryList>();
        try {
            JSONArray list = new JSONArray(str);
            //获取每个应用程序信息，类型是AppInfo
            for (int i = 0; i < list.length(); i++) {
                CategoryList category = new CategoryList();
                JSONObject jo = list.getJSONObject(i);
                //该循环中封装 标题对应的类型，共2次
                category.title = jo.getString("title");
                category.isTitle = true;
                categoryLists.add(category);
                //以上操作将标题单独封装在 CateGoryBean中。该Bean中只有Title数据。

                JSONArray infos = jo.getJSONArray("infos");
                for (int j = 0; j < infos.length(); j++) {
                    CategoryList category1 = new CategoryList();
                    JSONObject categoryObject = infos.getJSONObject(i);
                    category1.name1 = categoryObject.getString("name1");
                    category1.url1 = categoryObject.getString("url1");
                    category1.name2 = categoryObject.getString("name2");
                    category1.url2 = categoryObject.getString("url2");
                    category1.name3 = categoryObject.getString("name3");
                    category1.url3 = categoryObject.getString("url3");
                    categoryLists.add(category1);
                }
            }
            return categoryLists;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }*/

        ArrayList<CategoryList> dataList;
        try{
            JSONArray ja = new JSONArray(str);
            dataList = new ArrayList<CategoryList>();
            for(int i=0;i<ja.length();i++) {
                JSONObject jo = ja.getJSONObject(i);

                String title = jo.getString("title");
                CategoryList titleInfo = new CategoryList();
                titleInfo.title = title;
                titleInfo.isTitle = true;
                dataList.add(titleInfo);

                JSONArray ja2 = jo.getJSONArray("infos");
                for(int j=0;j<ja2.length();j++) {
                    JSONObject jo2 = ja2.getJSONObject(j);
                    CategoryList info = new CategoryList();
                    info.name1 = jo2.getString("name1");
                    info.name2 = jo2.getString("name2");
                    info.name3 = jo2.getString("name3");
                    info.url1 = jo2.getString("url1");
                    info.url2 = jo2.getString("url2");
                    info.url3 = jo2.getString("url3");
                    dataList.add(info);
                }


            }
        }catch(Exception e) {
            dataList = null;
        }


        return dataList;
    }
}
