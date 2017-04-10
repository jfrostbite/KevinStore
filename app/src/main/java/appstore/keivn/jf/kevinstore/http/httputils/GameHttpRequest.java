package appstore.keivn.jf.kevinstore.http.httputils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.bean.AppList;
import appstore.keivn.jf.kevinstore.ui.utils.MyLog;

/**
 * Created by Kevin on 2016/3/4.
 * 应用页面特有的请求网路的方式
 */
public class GameHttpRequest extends BaseHttpRequest<AppList> {

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
        return "game";
    }

    @Override
    protected String getValue() {
        return "?index="+index;
    }

    @Override
    public AppList processJson(String str) {


        AppList appList = new AppList();
        appList.picture = new ArrayList<String>();
        appList.list = new ArrayList<AppList.AppInfo>();

        try {
            JSONArray list = new JSONArray(str);
            //获取每个应用程序信息，类型是AppInfo
            for (int i = 0; i < list.length(); i++) {
                AppList.AppInfo appInfo = appList.new AppInfo();
                //appInfo = appList.new AppInfo();
                JSONObject appObject = list.getJSONObject(i);
                appInfo.des = appObject.getString("des");
                appInfo.downloadUrl = appObject.getString("downloadUrl");
                appInfo.iconUrl = appObject.getString("iconUrl");
                appInfo.id = appObject.getString("id");
                appInfo.name = appObject.getString("name");
                appInfo.packageName = appObject.getString("packageName");
                appInfo.size = appObject.getString("size");
                appInfo.stars = appObject.getString("stars");
                appList.list.add(appInfo);
            }

            if (appList != null) {
                return appList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 1、{} ---JSONObject
     * 2、[] ---JSONArray
     * 3、无任何符号---基础数据类型
     */
    /*private ArrayList<AppInfo> dataList;
    private ArrayList<String> picList;

    public ArrayList<AppInfo> processJson(String jsonString) {
        try{
            JSONObject jo = new JSONObject(jsonString);
            JSONArray ja = jo.getJSONArray("list");
            dataList = new ArrayList<AppInfo>();
            for(int i=0;i<ja.length();i++) {
                JSONObject jo2 = ja.getJSONObject(i);
                AppInfo info = new AppInfo();
                info.des = jo2.getString("des");
                info.downloadUrl = jo2.getString("downloadUrl");
                info.iconUrl = jo2.getString("iconUrl");
                info.id = jo2.getString("id");
                info.name = jo2.getString("name");
                info.packageName = jo2.getString("packageName");
                info.size = jo2.getString("size");
                info.stars = jo2.getString("stars");
                dataList.add(info);
            }


            JSONArray ja2 = jo.getJSONArray("picture");
            picList = new ArrayList<String>();
            for(int i=0;i<ja2.length();i++) {
                String strPic = ja2.getString(i);
                picList.add(strPic);
            }

        }catch(Exception e) {

        }

        return dataList;
    }*/
}
