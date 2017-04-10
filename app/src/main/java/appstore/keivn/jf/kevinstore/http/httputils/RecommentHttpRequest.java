package appstore.keivn.jf.kevinstore.http.httputils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.bean.SubjectList;

/**
 * Created by Kevin on 2016/3/4.
 * 应用页面特有的请求网路的方式
 */
public class RecommentHttpRequest extends BaseHttpRequest<ArrayList<String>> {

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
        return "recommend";
    }

    @Override
    protected String getValue() {
        return "";
    }

    @Override
    public ArrayList<String> processJson(String str) {


        ArrayList<String> recomList = new ArrayList<String>();
        try {
            JSONArray list = new JSONArray(str);
            //获取每个应用程序信息，类型是AppInfo
            for (int i = 0; i < list.length(); i++) {
                String reString = list.getString(i);
                recomList.add(reString);
            }

            if (recomList != null) {
                return recomList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

}
