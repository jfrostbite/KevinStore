package appstore.keivn.jf.kevinstore.http.httputils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.bean.CategoryBean;

/**
 * Created by Kevin on 2016/3/8.
 */
public class MyCategoryHttpRequest extends BaseHttpRequest<CategoryBean.Apps> {
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
    protected CategoryBean.Apps processJson(String string) {
        CategoryBean.Apps Apps = new CategoryBean.Apps();
        try{
            //解析整个Json文本
            JSONArray ja = new JSONArray(string);
            //遍历获得的2个数组（每个数组中就是应用，和游戏的总分类）
            for (int i = 0; i < ja.length(); i++) {
                Apps.infos = new ArrayList<CategoryBean.Apps.Group>();
                //创建每个
                CategoryBean.Apps.Group infosTitle = new CategoryBean.Apps.Group();
                JSONObject appsObj = ja.getJSONObject(i);
                infosTitle.title = appsObj.getString("title");
                infosTitle.isTitle = true;
                Apps.infos.add(infosTitle);
                //获取到信息集合
                JSONArray infoArr = ja.getJSONArray(i);
                for (int j = 0; j < infoArr.length(); j++) {
                    CategoryBean.Apps.Group infos = new CategoryBean.Apps.Group();
                    JSONObject infoObj = ja.getJSONObject(j);
                    infos.name1 = infoObj.getString("name1");
                    infos.name2 = infoObj.getString("name2");
                    infos.name3 = infoObj.getString("name3");
                    infos.url1 = infoObj.getString("url1");
                    infos.url2 = infoObj.getString("url2");
                    infos.url3 = infoObj.getString("url3");
                    Apps.infos.add(infos);
                }
            }
            return Apps;
        } catch (Exception e){
            return null;
        }
    }
}
