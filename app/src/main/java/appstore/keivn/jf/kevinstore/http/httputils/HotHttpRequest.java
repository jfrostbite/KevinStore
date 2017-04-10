package appstore.keivn.jf.kevinstore.http.httputils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.bean.CategoryList;

/**
 * Created by Kevin on 2016/3/4.
 * 应用页面特有的请求网路的方式
 */
public class HotHttpRequest extends BaseHttpRequest<ArrayList<String>> {

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
        return "hot";
    }

    @Override
    protected String getValue() {
        return "";
    }

    @Override
    public ArrayList<String> processJson(String str) {
        try {
            ArrayList<String> hotList = new ArrayList<String>();
            JSONArray ja = new JSONArray(str);
            for (int i = 0; i < ja.length(); i++) {
                String name = ja.getString(i);
                hotList.add(name);
            }
            return hotList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
